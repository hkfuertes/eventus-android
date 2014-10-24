package es.hkapps.eventus.view.events.create;

import es.hkapps.eventus.R;
import es.hkapps.eventus.model.Event;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class EventEditActivity extends ActionBarActivity implements EventStepListener {
	
	Event event;
	int currentStep = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_create);
		
		//Recuperamos y vemos si nos han pasado un evento.
		Intent intent = this.getIntent();
		event = (Event) intent.getSerializableExtra(Event.EVENT_TAG);
		
		/*
		//for testing purposes
		event = new Event("9fd11b0a5b");
		event.setName("prueba");
		event.setPlace("prueba");
		event.setType(Event.EVENT_TYPES[1]);
		event.setDate("10-10-2014");
		*/
		
		this.onStepCompleted(null, event);
	}
	
	private void setFragment(String title, Fragment fragment){
		setTitle(title);
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.container,fragment);
		//ft.addToBackStack(null);
		ft.commit();
	}

	@Override
	public void onStepCompleted(Fragment fragment, Event event) {
		this.event = event;
		if(currentStep == 0){
			EventCreateFragment step = EventCreateFragment.newInstance(event);
			step.setStepListener(this);
			if(event != null) this.setFragment("Editar "+event.getName(), step);
			else this.setFragment("Nuevo Evento", step);
		}else if(currentStep == 1){
			InviteParticipantsListFragment step = InviteParticipantsListFragment.newInstance(event);
			step.setStepListener(this);
			setFragment("Invita a gente", step);
		}else if(currentStep == 2){
			EventCreatedFragment step = EventCreatedFragment.newInstance(event);
			setFragment("Terminado", step);
		}
		currentStep++;
		//Toast.makeText(EventCreateActivity.this, event.getName(), Toast.LENGTH_LONG).show();
	}

	public static void launch(Activity activity) {
		Intent intent = new Intent(activity,EventEditActivity.class);
		activity.startActivity(intent);
	}
	
	public static void launch(Activity activity, Event event) {
		Intent intent = new Intent(activity,EventEditActivity.class);
		intent.putExtra(Event.EVENT_TAG, event);
		activity.startActivity(intent);
	}
}
