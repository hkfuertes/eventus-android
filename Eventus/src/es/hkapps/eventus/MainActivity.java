package es.hkapps.eventus;

import java.util.concurrent.ExecutionException;

import es.hkapps.eventus.api.RequestTask;
import es.hkapps.eventus.api.RequestTask.RequestListener;
import es.hkapps.eventus.api.User;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends LoginableActivity implements RequestListener {
	TextView texto;
	RequestTask task;
	String token;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Restore preferences
	       SharedPreferences settings = getSharedPreferences("user", 0);
	       token = settings.getString("user_token", null);
		if (token == null) {
			login(null);
		} else {
			startup();
		}
	}

	protected void startup() {
		setContentView(R.layout.activity_main);
		texto = ((TextView) this.findViewById(R.id.test));
		texto.setText(token);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onRequestCompleted(String response) {
		texto.setText(response);
	}
}
