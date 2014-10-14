package es.hkapps.eventus.view;

import es.hkapps.eventus.R;
import es.hkapps.eventus.model.User;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewUserActivity extends ActionBarActivity implements OnClickListener {
	EditText username, password, repassword, nombre, apellidos, email;
	Button go;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_user);
		
		//username = (EditText) findViewById(R.id.create_username);
		password = (EditText) findViewById(R.id.create_password);
		repassword = (EditText) findViewById(R.id.create_repassword);
		
		nombre = (EditText) findViewById(R.id.create_nombre);
		apellidos = (EditText) findViewById(R.id.create_apellidos);
		email = (EditText) findViewById(R.id.create_email);
		
		go = (Button) findViewById(R.id.create_signup);
		go.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		boolean bPassword = !password.getText().toString().equals("");
		boolean bUsername = !username.getText().toString().equals("");
		boolean bRepassword = password.getText().toString().equals(repassword.getText().toString());
		if(bPassword && bUsername && bRepassword){
			User user = new User(username.getText().toString(),
					nombre.getText().toString(),
					apellidos.getText().toString(),
					email.getText().toString());
			if(user.register(password.getText().toString(),true)){
				Toast.makeText(this, "Usuario registrado", Toast.LENGTH_LONG).show();
				finish();
			}else Toast.makeText(this, "¡Algo pasa con el servidor!", Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(this, "Comprueba los Datos!", Toast.LENGTH_LONG).show();
		}
		
	}
}
