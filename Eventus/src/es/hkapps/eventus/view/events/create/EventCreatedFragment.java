package es.hkapps.eventus.view.events.create;

import es.hkapps.eventus.R;
import es.hkapps.eventus.model.Event;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class EventCreatedFragment extends Fragment implements OnClickListener {
	TextView name;
	private Event event;
	private Button close;

	/* Singleton */
	public static EventCreatedFragment newInstance(Event event) {
		EventCreatedFragment fragment = new EventCreatedFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("event", event);
		fragment.setArguments(bundle);
		return fragment;
	}

	public EventCreatedFragment() {
		// Required empty public constructor
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle args = getArguments();
		event = (Event) args.getSerializable("event");
		
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_event_created, container,
				false);

		name = (TextView) v.findViewById(R.id.fragment_event_create_event_name);
		if (event != null) name.setText(event.getName().toString());
		
		close = (Button) v.findViewById(R.id.fragment_event_created_close);
		close.setOnClickListener(this);

		return v;
	}

	@Override
	public void onClick(View v) {
		this.getActivity().finish();
		//Igual podriamos abrir el evento recien creado.
	}

}
