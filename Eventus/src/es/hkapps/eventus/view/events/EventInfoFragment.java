package es.hkapps.eventus.view.events;

import es.hkapps.eventus.R;
import es.hkapps.eventus.model.Event;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class EventInfoFragment extends Fragment {	
	private static final String ARGUMENT_ID = "evento";

	TextView fecha, lugar;
	Event event;

	private TextView name;

	private TextView key;

	/* Singleton */
	public static EventInfoFragment newInstance(Event event) {
		EventInfoFragment fragment = new EventInfoFragment();
		Bundle bundle = new Bundle();
	    bundle.putSerializable(ARGUMENT_ID, event);
	    fragment.setArguments(bundle);
		return fragment;
	}

	public EventInfoFragment() {
		// Required empty public constructor
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    Bundle args = getArguments();
	    if (args != null) {
	    	event = (Event) args.getSerializable(ARGUMENT_ID);
	    }    
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_event_info, container, false);
		
		if(event != null){
    		fecha = (TextView) v.findViewById(R.id.event_info_fecha);
    		lugar = (TextView) v.findViewById(R.id.event_info_lugar);
    		name = (TextView) v.findViewById(R.id.event_info_name);
    		key = (TextView) v.findViewById(R.id.event_info_key);
    		
    		fecha.setText(event.getDate());
    		lugar.setText(event.getPlace());
    		name.setText(event.getName());
    		key.setText(event.getKey());
    	}
		
		return v;
	}
}

