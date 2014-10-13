package es.hkapps.eventus.view.events.create;

import es.hkapps.eventus.R;
import es.hkapps.eventus.R.id;
import es.hkapps.eventus.R.layout;
import es.hkapps.eventus.R.menu;
import es.hkapps.eventus.R.string;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EventCreateActivity extends ActionBarActivity implements
		ActionBar.OnNavigationListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_create);

		setTitle("Nuevo Evento");

		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.container,
				EventCreateFragment.newInstance(null)).commit();


	}



	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		/*
		 * Voy a usar este metodo para cambiar entre los diferentes pasos.
		 */
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.container,
				EventCreateFragment.newInstance(null)).commit();
		return true;
	}
}
