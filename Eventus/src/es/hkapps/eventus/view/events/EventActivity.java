package es.hkapps.eventus.view.events;

import java.io.File;

import es.hkapps.eventus.R;
import es.hkapps.eventus.api.Util;
import es.hkapps.eventus.camera.CameraActivity;
import es.hkapps.eventus.model.Event;
import es.hkapps.eventus.model.Photo;
import es.hkapps.eventus.model.User;
import es.hkapps.eventus.view.wall.WallFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EventActivity extends ActionBarActivity {

	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;

	protected Event event;
	private TextView name;
	private TextView date;
	private Spinner spinner;
	private TextView place;

	private Photo current;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);

		Intent intent = this.getIntent();
		event = (Event) intent.getExtras().getSerializable(Util.pGeneral);
		Toast.makeText(this, event.toString(), Toast.LENGTH_LONG).show();

		// Si no hay evento... finalizamos la actividad.
		if (event == null)
			finish();

		this.setTitle("Evento");

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageSelected(int pos) {
				//spinner.setSelection(pos);
				getActionBar().setSelectedNavigationItem(pos);
			}

		});

		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
				R.layout.spinner_item, mSectionsPagerAdapter.getTitles());

		/** Enabling dropdown list navigation for the action bar */
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		/** Defining Navigation listener */
		OnNavigationListener navigationListener = new OnNavigationListener() {

			@Override
			public boolean onNavigationItemSelected(int itemPosition,
					long itemId) {
				mViewPager.setCurrentItem(itemPosition);
				return false;
			}
		};

		/**
		 * Setting dropdown items and item navigation listener for the actionbar
		 */
		getActionBar()
				.setListNavigationCallbacks(
						spinnerAdapter,navigationListener);
		getActionBar().setDisplayShowTitleEnabled(false);

		name = (TextView) findViewById(R.id.event_title_name);
		date = (TextView) findViewById(R.id.event_title_date);
		place = (TextView) findViewById(R.id.event_title_place);

		name.setText(event.getName());
		date.setText(event.getDate());
		place.setText(event.getPlace());

	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		private static final int FRAGMENT_COUNT = 3;

		Fragment[] fragment = new Fragment[FRAGMENT_COUNT];
		String[] title = new String[FRAGMENT_COUNT];

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			fragment[0] = WallFragment.newInstance(event);
			title[0] = "Muro";
			fragment[1] = EventProgramFragment.newInstance(event);
			title[1] = "Programa";
			fragment[2] = ParticipantsListFragment.newInstance(event);
			title[2] = "Invitados";
		}

		public String[] getTitles() {
			return title;
		}

		@Override
		public Fragment getItem(int position) {
			return fragment[position];
		}

		@Override
		public int getCount() {
			return fragment.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return title[position];
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.event, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.event_activity_action_leave:
			User user = Util.getUser(this);
			event.removeUser(user.getUsername(), user.getToken());
			if (event.removeUser(user.getUsername(), user.getToken()))
				finish();
			else
				Toast.makeText(this, "Ha habido un problema con la conexion.",
						Toast.LENGTH_LONG).show();
			break;
		case R.id.event_activity_action_photo:
			Intent intent = new Intent(this, CameraActivity.class).putExtra(
					Util.pGeneral, event);
			startActivity(intent);
			break;
		default:
			this.mViewPager.setCurrentItem(0);
		}
		return super.onOptionsItemSelected(item);
	}

}
