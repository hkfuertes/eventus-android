package es.hkapps.eventus.view.events.create;

import android.support.v4.app.Fragment;
import es.hkapps.eventus.model.Event;

public interface EventStepListener{
		public void onStepCompleted(Fragment fragment,Event event);
	}