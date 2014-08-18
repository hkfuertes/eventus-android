package es.hkapps.eventus.view.main;

import es.hkapps.eventus.R;
import es.hkapps.eventus.api.Util;
import es.hkapps.eventus.model.User;
import es.hkapps.eventus.view.LoginActivity;
import es.hkapps.eventus.view.UserFragment;
import es.hkapps.eventus.view.events.EventListFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	private static final int FRAGMENT_COUNTS = 3;
	Fragment[] fragment = new Fragment[FRAGMENT_COUNTS];

	private NavigationDrawerFragment mNavigationDrawerFragment;
	private DrawerLayout drawer;

	private User user = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected void onResume() {
		super.onResume();
		// Restore preferences
		if (!Util.checkUser(this)) {
			startActivity(new Intent(this, LoginActivity.class));
		} else {
			user = Util.getUser(this);
			user.retrieveInfo();
			Util.setUser(this, user);
			onAllowedCreate(null);
		}
	}

	protected void onAllowedCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_wall);

		fragment[0] = UserFragment.newInstance(user);
		fragment[1] = EventListFragment.newInstance();

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);

		// Set up the drawer.
		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,drawer);
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		if (position == FRAGMENT_COUNTS) {
			Util.clearUser(this);
			finish();
		}
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.container, fragment[position]).commit();
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
	}
}
