package es.hkapps.eventus.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class User implements Serializable {
	private String username;
	private String token;
	private String email, nombre, apellidos;

	public User(String username, String nombre, String apellidos, String email) {
		this.username = username;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
	}

	public String toString() {
		return username;
	}

	public String getUsername() {
		return username;
	}

	public String getToken() {
		return token;
	}

	public String getNombre() {
		return nombre;
	}

	public String getNombreCompleto() {
		return nombre + " " + apellidos;
	}

	private boolean infoRetrieved() {
		boolean a = (nombre != null) && (apellidos != null) && (email != null);
		boolean b = (nombre != "") && (apellidos != "") && (email != "");
		return a && b;
	}

	/**
	 * Recupera la informacion de un usuario contra la API de Eventus
	 * 
	 * @return Devuelve true o false si obtiene la informacion, y añade la
	 *         informacion.
	 */
	public boolean retrieveInfo() {
		if (infoRetrieved())
			return true;
		try {
			String url = Util.server_addr + Util.app_token + "/user/info/"
					+ username;

			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("username", username));
			nameValuePairs.add(new BasicNameValuePair("token", token));

			RequestTaskPost task = new RequestTaskPost(nameValuePairs);
			String response;
			response = task.execute(url).get();
			JSONObject jObj = new JSONObject(response);
			boolean success = jObj.getBoolean("success");
			if (success) {
				JSONObject user = jObj.getJSONObject("user");
				nombre = user.getString("nombre");
				apellidos = user.getString("apellidos");
				email = user.getString("email");
			}
			return success;
		} catch (Exception e) {
			Log.d("Getting info [" + username + "]", e.toString());
			return false;
		}
	}

	/**
	 * Valida un usuario contra la API de Eventus
	 * 
	 * @param password
	 * @return Devuelve true o false si esta logeado y añade el token al
	 *         usuario.
	 */
	public boolean validate(String password) {
		if (token != null)
			return true;
		try {
			String url = Util.server_addr + Util.app_token + "/user/validate/"
					+ username;

			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("username", username));
			nameValuePairs.add(new BasicNameValuePair("password", password));

			RequestTaskPost task = new RequestTaskPost(nameValuePairs);
			String response;
			response = task.execute(url).get();
			JSONObject jObj = new JSONObject(response);
			boolean success = jObj.getBoolean("success");
			if (success) {
				JSONObject user = jObj.getJSONObject("user");
				token = user.getString("token");
			}
			return success;
		} catch (Exception e) {
			Log.d("Validating [" + username + "]", e.toString());
			return false;
		}
	}

	/**
	 * Valida un usuario contra la API de Eventus
	 * 
	 * @param password
	 * @return Devuelve true o false si esta logeado y añade el token al
	 *         usuario.
	 */
	public boolean register(String password, boolean validate) {

		// Add your data
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("username", username));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		nameValuePairs.add(new BasicNameValuePair("nombre", nombre));
		nameValuePairs.add(new BasicNameValuePair("apellidos", apellidos));
		nameValuePairs.add(new BasicNameValuePair("email", email));

		Log.d("Registering", nameValuePairs.toString());
		
		if (token != null)
			return true;
		try {
			String url = Util.server_addr + Util.app_token + "/user/create";

			RequestTaskPost task = new RequestTaskPost(nameValuePairs);
			String response = task.execute(url).get();
			Log.d("Registering [RESP]", response);
			JSONObject jObj = new JSONObject(response);
			boolean success = jObj.getBoolean("success");
			if (success) {
				return validate(password);
			}
			return false;
		} catch (Exception e) {
			Log.d("Registering [" + username + "]", e.toString());
			return false;
		}
	}

	public String getApellidos() {
		return apellidos;
	}

	public String getEmail() {
		return email;
	}

}
