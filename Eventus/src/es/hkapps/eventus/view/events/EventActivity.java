package es.hkapps.eventus.view.events;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import es.hkapps.eventus.R;
import es.hkapps.eventus.api.Util;
import es.hkapps.eventus.camera.CameraActivity;
import es.hkapps.eventus.model.Event;
import es.hkapps.eventus.model.User;
import es.hkapps.eventus.view.wall.WallFragment;
import es.hkapps.eventus.view.events.create.EventEditActivity;
import es.hkapps.eventus.view.events.participants.*;
import es.hkapps.eventus.view.events.program.*;
import es.hkapps.eventus.view.events.photos.PhotosFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class EventActivity extends ActionBarActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	EventActivityPagerAdapter mSectionsPagerAdapter;
	
	private static final int FRAGMENT_COUNT = 4;

	Fragment[] fragment = new Fragment[FRAGMENT_COUNT];
	String[] title = new String[FRAGMENT_COUNT];
	int[] resource = new int[FRAGMENT_COUNT];

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	private Event event;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_activity_new);
		
		Intent intent = this.getIntent();
		event = (Event) intent.getExtras().getSerializable(Util.pGeneral);
		//Toast.makeText(this, event.toString(), Toast.LENGTH_LONG).show();

		// Si no hay evento... finalizamos la actividad.
		if (event == null)
			finish();

		this.setTitle(event.getName());
		
		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		
		
		fragment[0] = EventInfoFragment.newInstance(event);
		title[0] = "Info";
		resource[0] = R.drawable.ic_action_chat;
		fragment[1] = EventProgramFragment.newInstance(event);
		title[1] = "Programa";
		resource[1] = R.drawable.ic_action_event_green;
		fragment[2] = ParticipantsListFragment.newInstance(event);
		title[2] = "Invitados";
		resource[2] = R.drawable.ic_action_person;
		fragment[3] = PhotosFragment.newInstance(event);
		title[3] = "Fotos de este evento";
		resource[3] = R.drawable.ic_action_picture;

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new EventActivityPagerAdapter(
				getSupportFragmentManager(),fragment,title,resource);

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					//.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setIcon(this.mSectionsPagerAdapter.getPageIcon(i))
					.setTabListener(this));
		}
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		setHasEmbeddedTabs(actionBar,false);
	}
	
	

    public static void setHasEmbeddedTabs(Object inActionBar, final boolean inHasEmbeddedTabs)
    {
        // get the ActionBar class
        Class<?> actionBarClass = inActionBar.getClass();

        // if it is a Jelly Bean implementation (ActionBarImplJB), get the super class (ActionBarImplICS)
        if ("android.support.v7.app.ActionBarImplJB".equals(actionBarClass.getName()))
        {
            actionBarClass = actionBarClass.getSuperclass();
        }

        // if Android 4.3 >
        if ("android.support.v7.app.ActionBarImplJBMR2".equals(actionBarClass.getName())){
            actionBarClass = actionBarClass.getSuperclass().getSuperclass();
        }

        try
        {
            // try to get the mActionBar field, because the current ActionBar is probably just a wrapper Class
            // if this fails, no worries, this will be an instance of the native ActionBar class or from the ActionBarImplBase class
            final Field actionBarField = actionBarClass.getDeclaredField("mActionBar");
            actionBarField.setAccessible(true);
            inActionBar = actionBarField.get(inActionBar);
            actionBarClass = inActionBar.getClass();
        }
        catch (IllegalAccessException e) {}
        catch (IllegalArgumentException e) {}
        catch (NoSuchFieldException e) {}

        try
        {
            // now call the method setHasEmbeddedTabs, this will put the tabs inside the ActionBar
            // if this fails, you're on you own <img class="wp-smiley" alt=";-)" src="http://www.blogc.at/wp-includes/images/smilies/icon_wink.gif">
            final Method method = actionBarClass.getDeclaredMethod("setHasEmbeddedTabs", new Class[] { Boolean.TYPE });
            method.setAccessible(true);
            method.invoke(inActionBar, new Object[]{ inHasEmbeddedTabs });
        }
        catch (NoSuchMethodException e)        {}
        catch (InvocationTargetException e) {}
        catch (IllegalAccessException e) {}
        catch (IllegalArgumentException e) {}
    }


	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.event, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		User user = Util.getUser(this);
		//Si no soy administrador, no puedo cambiar nada.
	    if(!user.getUsername().equals(event.getAdmin())){
	    	menu.removeItem(R.id.event_activity_action_edit);
	    }else{
	    	menu.findItem(R.id.event_activity_action_leave).setTitle(R.string.event_activity_leave_admin);
	    }

	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.event_activity_action_leave:
			User user = Util.getUser(this);
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
		case R.id.event_activity_action_edit:
			EventEditActivity.launch(this, event);
			break;
		case R.id.event_activity_action_share:
			shareEvent();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void shareEvent(){
		String shareBody = getString(R.string.event_share)+" "+event.getWebJoinLink(false);
	    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
	        //sharingIntent.setType("text/html");
	    sharingIntent.setType("text/plain");
	        //sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
	        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
	        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
	}


	public static void launch(Activity activity, Event event) {
		Intent intent = new Intent(activity, EventActivity.class)
		.putExtra(Util.pGeneral, event);
		activity.startActivity(intent);
	}
}
