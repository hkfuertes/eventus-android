package es.hkapps.eventus;

import es.hkapps.eventus.api.User;
import es.hkapps.eventus.api.Util;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends ActionBarActivity {
	Intent intent;
	private EditText user, pass;
	private Button submit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		submit = (Button) this.findViewById(R.id.email_sign_in_button);
		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				login(v);
			}
		};
		submit.setOnClickListener(listener);
		user = (EditText) this.findViewById(R.id.email);
		pass = (EditText) this.findViewById(R.id.password);
	}

	public void login(View target) {
		User usuario = new User(user.getText().toString());
		if (usuario.validate(pass.getText().toString())) {
			setResult(RESULT_OK);
			finish();
		} else {
			Toast.makeText(this, "Login failed, please try again",
					Toast.LENGTH_SHORT).show();
		}
	}
}