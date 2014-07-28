package es.hkapps.eventus.api;

import com.google.gson.Gson;

import android.app.Activity;
import android.content.SharedPreferences;

public class Util {
	public static final String app_token="app_token_here";
	
	public static final String server_addr = "http://192.168.1.15/eventus/proyecto/web/";
	public static final String pGeneral = "eventus_preferences_general";
	public static final String pUser = "eventus_preferences_general_user";

	public static void setUser(Activity act, User user) {
		SharedPreferences settings = act.getSharedPreferences(Util.pGeneral, 0);
		SharedPreferences.Editor editor = settings.edit();
		String json = new Gson().toJson(user);
		editor.putString(pUser, json);
		editor.commit();
	}
	
	public static User getUser(Activity act) {
		SharedPreferences settings = act.getSharedPreferences(Util.pGeneral, 0);
		String json = settings.getString(pUser, null);
	    return new Gson().fromJson(json, User.class);
	}
	
	public static void clearUser(Activity act){
		SharedPreferences settings = act.getSharedPreferences(Util.pGeneral, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(pUser, null);
		editor.commit();
	}
	public static boolean checkUser(Activity act){
		return getUser(act) != null;
	}
}
