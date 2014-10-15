package es.hkapps.eventus.view.main;

/**
 * https://developer.android.com/training/basics/actionbar/styling.html
 */

import es.hkapps.eventus.R;
import es.hkapps.eventus.api.Util;
import es.hkapps.eventus.model.User;
import es.hkapps.eventus.view.main.events.EventListFragment;
import es.hkapps.eventus.view.user.LoginActivity;
import es.hkapps.eventus.view.user.UserFragment;
import es.hkapps.eventus.view.wall.WallFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

public class StartActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	private User user;

	private static final int FRAGMENT_COUNT = 4;
	private static final int EXIT_POSITION = FRAGMENT_COUNT - 1;

	private static final int LOGIN = 42;
	private Fragment[] fragment = new Fragment[FRAGMENT_COUNT];
	private String[] title = new String[FRAGMENT_COUNT];
	
	public void login() {
		startActivityForResult(new Intent(this, LoginActivity.class), LOGIN);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!isLoggedIn()) {
			login();
		} else {
			startup();
		}
	}
	
	private boolean isLoggedIn() {
		return (user = Util.getUser(this)) != null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (isLoggedIn()) {
			startup();
		} else {
			Toast.makeText(this, "Login canceled", Toast.LENGTH_SHORT).show();
			finish();
		}
	}
	
	protected void startup() {
		user = Util.getUser(this);
		user.retrieveInfo();
		Util.setUser(this, user);

		fragment[0] = UserFragment.newInstance(user);
		title[0] = user.getNombreCompleto();
		fragment[1] = EventListFragment.newInstance();
		title[1] = "Eventos";
		fragment[2] = WallFragment.newInstance();
		title[2] = "Muro";

		// El ultimo no cierra sesion.
		title[EXIT_POSITION] = "Cerrar Sesion";

		setContentView(R.layout.activity_start);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

		mNavigationDrawerFragment.setTitles(title);
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		if (position >= EXIT_POSITION) {
			Util.clearUser(this);
			finish();
		} else {
			// update the main content by replacing fragments
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.container, fragment[position]).commit();
		}
	}

	public void onSectionAttached(int number) {
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}
}
