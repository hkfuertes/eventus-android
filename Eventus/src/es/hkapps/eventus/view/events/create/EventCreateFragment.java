package es.hkapps.eventus.view.events.create;

import es.hkapps.eventus.R;
import es.hkapps.eventus.api.Util;
import es.hkapps.eventus.model.Event;
import es.hkapps.eventus.model.User;
import es.hkapps.eventus.view.events.program.EventProgramFragment;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EventCreateFragment extends Fragment {
	TextView name, place;
	Spinner type;
	CalendarView date;
	
	/* Singleton */
	public static EventCreateFragment newInstance(Event event) {
		EventCreateFragment fragment = new EventCreateFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("event", event);
		fragment.setArguments(bundle);
		return fragment;
	}

	public EventCreateFragment() {
		// Required empty public constructor
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v =  inflater.inflate(R.layout.fragment_event_create, container, false);
		
		name = (TextView) v.findViewById(R.id.fragment_event_create_event_name);
		place = (TextView) v.findViewById(R.id.fragment_event_create_event_place);
		
		date = (CalendarView) v.findViewById(R.id.fragment_event_create_event_date);
		type = (Spinner) v.findViewById(R.id.fragment_event_create_event_type);
		
		type.setAdapter(new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, Event.EVENT_TYPES));
		
		return v;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_event_create, menu);
	    super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.fragment_event_create:
			Toast.makeText(this.getActivity(), "Creando evento.", Toast.LENGTH_LONG).show();
			
			User user = Util.getUser(getActivity());
			Event event = new Event(
					name.getText().toString(),
					place.getText().toString(),
					user,
					date.getDate(),
					Event.EVENT_TYPES[type.getSelectedItemPosition()]
					);
			
			Event.createEvent(user, event);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
}
