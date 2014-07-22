package es.hkapps.eventus.api;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class User implements Serializable {
	private String username;
	private String token;

	public User(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public String toString() {
		return username;
	}

	/**
	 * Valida un usuario contra la API de Eventus
	 * @param password
	 * @return
	 * Devuelve true o false si esta logeado y añade el token al usuario.
	 */
	public boolean validate(String password) {
		try {
			String url = "http://192.168.1.15/eventus/proyecto/web/validate/"
					+ username + "/" + password;

			RequestTask task = new RequestTask();
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
			Log.d("LoginActivity [" + username + "]", e.toString());
			return false;
		}
	}

	// DE PRUEBA
	public static String URL = "http://192.168.1.15/eventus/proyecto/web/getUsers";

	public static String[] getUsers() throws InterruptedException,
			ExecutionException, JSONException {
		RequestTask task = new RequestTask();
		String response = task.execute(User.URL).get();

		JSONObject jObj = new JSONObject(response);
		JSONArray usuarios = jObj.getJSONArray("usuarios");
		String[] values = new String[usuarios.length()];
		for (int i = 0; i < usuarios.length(); i++) {
			values[i] = usuarios.getString(i);
		}
		return values;
	}

	public String getToken() {
		return token;
	}

}
