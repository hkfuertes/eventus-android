package es.hkapps.eventus;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public abstract class LoginableActivity extends ActionBarActivity {
	private static final int LOGIN = 42;
	public static boolean loggedIn = false;
	public String token;

	public void login(View target) {
		startActivityForResult(new Intent(this, LoginActivity.class), LOGIN);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == LOGIN) {
			if (resultCode == RESULT_OK) {
				loggedIn = true;
			} else {
				loggedIn = false;
			}
			// ...
		}
	}
}