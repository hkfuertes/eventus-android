package es.hkapps.eventus.view;

import es.hkapps.eventus.R;
import es.hkapps.eventus.api.Util;
import es.hkapps.eventus.model.User;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;


public class UserActivity extends ActionBarActivity {

	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		user = (User) intent.getSerializableExtra(Util.pUser);
		
		this.setTitle("Informacion de Usuario");
		
		setContentView(R.layout.activity_user);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new UserFragment(user)).commit();
		}
	}
}
