package es.hkapps.eventus.api;

import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class User {
	private String name;
	public static String URL = "http://192.168.1.15/eventus/proyecto/web/getUsers";
	
	public User(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public String toString(){
		return name;
	}
	
	public static String[] getUsers() throws InterruptedException, ExecutionException, JSONException{
		RequestTask task = new RequestTask();
		String response = task.execute(User.URL).get();
		
		JSONObject jObj = new JSONObject(response);
		JSONArray usuarios = jObj.getJSONArray("usuarios");
		String[]  values = new String[usuarios.length()];
		for(int i=0; i<usuarios.length(); i++){
			values[i] = usuarios.getString(i);
		}
		return values;
	}
}
