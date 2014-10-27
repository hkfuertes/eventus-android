package es.hkapps.eventus.view.events.create;


import es.hkapps.eventus.R;
import es.hkapps.eventus.api.Util;
import es.hkapps.eventus.model.Event;
import es.hkapps.eventus.model.User;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EventCreateFragment extends Fragment {
	private static final String EVENT = "event";
	TextView name, place;
	Spinner type;
	DatePicker date;
	private EventStepListener listener;
	private Event event;

	/* Singleton */
	public static EventCreateFragment newInstance(Event event) {
		EventCreateFragment fragment = new EventCreateFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(EVENT, event);
		fragment.setArguments(bundle);
		return fragment;
	}

	public EventCreateFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		event = this.getEvent();
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_event_create, container,
				false);

		name = (TextView) v.findViewById(R.id.fragment_event_create_event_name);
		place = (TextView) v
				.findViewById(R.id.fragment_event_create_event_place);

		date = (DatePicker) v
				.findViewById(R.id.fragment_event_create_event_date);
		type = (Spinner) v.findViewById(R.id.fragment_event_create_event_type);

		type.setAdapter(new ArrayAdapter<String>(this.getActivity(),
				android.R.layout.simple_list_item_1, Event.EVENT_TYPES));
		
		if(event!=null){
			name.setText(event.getName());
			name.setVisibility(View.GONE);
			place.setText(event.getPlace());
			type.setSelection(event.getEventTypeId(event.getType()));
			int[] values = event.getDateNumbers();
			date.updateDate(values[0], values[1], values[2]);
		}

		return v;
	}

	private Event getEvent() {
		return (Event) getArguments().getSerializable(EVENT);
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
			Toast.makeText(this.getActivity(), "Creando evento.",
					Toast.LENGTH_LONG).show();

			String nameStr,
			placeStr;
			if ((nameStr = name.getText().toString()) != ""
					&& (placeStr = place.getText().toString()) != "") {

				String date = this.date.getYear() + "-" + this.date.getMonth() + "-" +  this.date.getDayOfMonth();
				
				User user = Util.getUser(getActivity());
				if(event == null) event = new Event();
				event.setName(nameStr);
				event.setPlace(placeStr);
				event.setAdmin(user.getUsername());
				event.setDate(date);
				event.setType(Event.EVENT_TYPES[type.getSelectedItemPosition()]);
		
				event = event.saveEvent(user);
				// Toast.makeText(getActivity(), event.getKey(),
				// Toast.LENGTH_LONG).show();
				if (listener != null)
					listener.onStepCompleted(this, event);
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void setStepListener(EventStepListener listener) {
		this.listener = listener;
	}

}
