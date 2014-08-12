package es.hkapps.eventus.view;

import es.hkapps.eventus.R;
import es.hkapps.eventus.api.Util;
import es.hkapps.eventus.model.User;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends ActionBarActivity implements OnClickListener{
	Intent intent;
	private EditText user, pass;
	private Button submit;
	private Button signup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		/* Si existe el usuario no tiene sentido que le preguntemos las credenciales.
		 * Pensando en volver de registrarnos.
		 */
		if (Util.checkUser(this)) finish();
		
		submit = (Button) this.findViewById(R.id.email_sign_in_button);
		signup = (Button) this.findViewById(R.id.create_signup);
		submit.setOnClickListener(this);
		signup.setOnClickListener(this);
		user = (EditText) this.findViewById(R.id.email);
		pass = (EditText) this.findViewById(R.id.password);
	}

	public void login(View target) {
		User usuario = new User(user.getText().toString(),null,null,null);
		if (usuario.validate(pass.getText().toString())) {
			Util.setUser(this, usuario);
			setResult(RESULT_OK);
			finish();
		} else {
			Toast.makeText(this, "Login failed, please try again",
					Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void onBackPressed() {
	}

	@Override
	public void onClick(View v) {
		if((Button) v == submit){
			login(v);
		}else if((Button) v == signup){
			startActivity(new Intent(this, NewUserActivity.class));
		}
		
	}
}