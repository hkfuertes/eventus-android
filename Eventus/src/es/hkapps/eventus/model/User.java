package es.hkapps.eventus.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import es.hkapps.eventus.api.RequestTaskPost;
import es.hkapps.eventus.api.Util;
import android.util.Log;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	private String username;
	private String token;
	private String email, nombre, apellidos;

	private int id = -1;

	public User(String username, String nombre, String apellidos, String email) {
		this.username = username;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
	}

	public User() {
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
		this.setFromUser(this.retrieveUser(this.username));
		return true;
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
			Log.d("Registering [URL]", url);
			RequestTaskPost task = new RequestTaskPost(nameValuePairs);
			String response = task.execute(url).get();
			// Log.d("Registering [RESP]", response);
			JSONObject jObj = new JSONObject(response);
			boolean success = jObj.getBoolean("success");
			if (success) {
				return validate(password);
			}
			return false;
		} catch (Exception e) {
			// Log.d("Registering [" + username + "]", e.toString());
			e.printStackTrace();
			return false;
		}
	}
	
	private void setFromUser(User user){
		this.nombre = user.getNombre();
		this.apellidos = user.getApellidos();
		this.email = user.getEmail();
	}

	public String getApellidos() {
		return apellidos;
	}

	public String getEmail() {
		return email;
	}

	public int getId() {
		return id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User retrieveUser(String user) {
		User usr = new User();
		String url = Util.server_addr + Util.app_token + "/user/info/"
				+ user;
		// Add your data
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("username", username));
		nameValuePairs.add(new BasicNameValuePair("token", token));
		try {
			RequestTaskPost task = new RequestTaskPost(nameValuePairs);
			String response;
			response = task.execute(url).get();
			JSONObject jObj = new JSONObject(response);
			boolean success = jObj.getBoolean("success");
			if (success) {
				JSONObject u = jObj.getJSONObject("user");
				usr.setNombre(u.getString("firstname"));
				usr.setApellidos(u.getString("lastname"));
				usr.setEmail(u.getString("email"));
			}
			return usr;
		} catch (Exception e) {
			Log.d("Getting info [" + username + "]", e.toString());
			Log.d("Getting info [" + username + "]", url);
			Log.d("Getting info [" + username + "]", nameValuePairs.toString());
			return null;
		}
	}
	
	public boolean checkTocken() {
		String url = Util.server_addr + Util.app_token + "/user/token/check/"
				+ this.getUsername();
		// Add your data
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("username", username));
		nameValuePairs.add(new BasicNameValuePair("token", token));
		try {
			RequestTaskPost task = new RequestTaskPost(nameValuePairs);
			String response;
			response = task.execute(url).get();
			JSONObject jObj = new JSONObject(response);
			boolean success = jObj.getBoolean("success");
			return success;
		} catch (Exception e) {
			Log.d("Checking Token [" + username + "]", e.toString());
			Log.d("Checking Token [" + username + "]", url);
			Log.d("Checking Token [" + username + "]", nameValuePairs.toString());
			return false;
		}
	}

}
