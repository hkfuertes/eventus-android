package es.hkapps.eventus.api;

import es.hkapps.eventus.model.User;
import es.hkapps.eventus.model.UserHelper;
import android.app.Activity;
import android.content.SharedPreferences;

public class Util {
	public static final String app_token = "NsBHwgaksWYKnYHFB3AS2DSk";

	//public static final String server_addr = "http://192.168.1.41/eventus/proyecto/web/";
	public static final String server_addr = "http://hkfuertes.ddns.net/API/";
	public static final String pGeneral = "eventus_preferences_general";
	public static final String pUser = "eventus_preferences_general_user";

	public static void setUser(Activity act, User user) {
		// We save the username on prefs...
		SharedPreferences settings = act.getSharedPreferences(Util.pGeneral, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(pUser, user.getUsername());
		editor.commit();

		// ... and the actual user on db.
		UserHelper uHelper = new UserHelper(act);
		uHelper.save(user);
	}

	public static User getUser(Activity act) {
		// We recover the username from prefs...
		SharedPreferences settings = act.getSharedPreferences(Util.pGeneral, 0);
		String username = settings.getString(pUser, null);

		// ... and the whole user from db
		if (username != null) {
			UserHelper uHelper = new UserHelper(act);
			return uHelper.retrieveUserByUsername(username);
		} else
			return null;
	}

	public static void clearUser(Activity act) {
		//We delete the user from db...
		UserHelper uHelper = new UserHelper(act);
		uHelper.delete(Util.getUser(act));

		//... and from prefs.
		SharedPreferences settings = act.getSharedPreferences(Util.pGeneral, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(pUser, null);
		editor.commit();
	}

	public static boolean checkUser(Activity act) {
		return getUser(act) != null;
	}
}
