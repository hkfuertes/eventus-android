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

public class EventCreateActivity extends ActionBarActivity implements EventCreationStepListener {
	
	Event event;
	int currentStep = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_create);
		
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
			EventCreateFragment step = EventCreateFragment.newInstance(null);
			step.setStepListener(this);
			this.setFragment("Nuevo Evento", step);
		}else if(currentStep == 1){
			InviteParticipantsFragment step = InviteParticipantsFragment.newInstance(event);
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
		Intent intent = new Intent(activity,EventCreateActivity.class);
		activity.startActivity(intent);
	}
}
