package es.hkapps.eventus.view.events;

import java.util.ArrayList;

import es.hkapps.eventus.R;
import es.hkapps.eventus.api.Util;
import es.hkapps.eventus.model.Event;
import es.hkapps.eventus.model.ProgramEntry;
import es.hkapps.eventus.model.User;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EventInfoFragment extends Fragment {

	private User user;
	TextView name;
	Event event;

	/* Singleton */
	public static EventInfoFragment newInstance(Event event) {
		EventInfoFragment fragment = new EventInfoFragment();
		Bundle bundle = new Bundle();
	    bundle.putSerializable("event", event);
	    fragment.setArguments(bundle);
		return fragment;
	}

	public EventInfoFragment() {
		// Required empty public constructor
	}

	@Override
	public void onResume() {
		super.onResume();
		user = Util.getUser(this.getActivity());
		event = (Event) this.getArguments().getSerializable("event");
		event.retrieveInfo(user.getUsername(), user.getToken());
		ArrayList<ProgramEntry> program = event.getProgram();
		
		name = (TextView) getView().findViewById(R.id.event_info_name);
		name.setText(event.getName());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_event_info, container, false);
	}
}
