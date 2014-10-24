package es.hkapps.eventus.view.events.create;

import es.hkapps.eventus.R;
import es.hkapps.eventus.api.Util;
import es.hkapps.eventus.model.Event;
import es.hkapps.eventus.view.events.EventActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class EventCreatedFragment extends Fragment implements OnClickListener {
	private static final String EVENT = "event";
	TextView name;
	private Event event;
	private Button close;
	private boolean standalone = false;
	private TextView key;

	/* Singleton */
	public static EventCreatedFragment newInstance(Event event) {
		EventCreatedFragment fragment = new EventCreatedFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(EVENT, event);
		fragment.setArguments(bundle);
		return fragment;
	}

	public EventCreatedFragment() {
		// Required empty public constructor
	}

	private Event getEvent() {
		return (Event) getArguments().getSerializable(EVENT);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			// We have different layouts, and in one of them this
			// fragment's containing frame doesn't exist. The fragment
			// may still be created from its saved state, but there is
			// no reason to try to create its view hierarchy because it
			// won't be displayed. Note this is not needed -- we could
			// just run the code below, where we would create and return
			// the view hierarchy; it would just never be used.
			return null;
		}

		event = getEvent();

		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_event_created, container,
				false);

		name = (TextView) v
				.findViewById(R.id.fragment_event_created_event_name);
		key = (TextView) v.findViewById(R.id.fragment_event_created_event_key);
		if (event != null && event instanceof Event) {
			// Toast.makeText(getActivity(), "Evento Creado: "+ event,
			// Toast.LENGTH_LONG).show();
			name.setText(event.getName().toString());
			key.setText(event.getKey().toString());
		}

		close = (Button) v.findViewById(R.id.fragment_event_created_close);
		close.setOnClickListener(this);

		if(!standalone) close.setText("Ir al evento");
		
		return v;
	}

	@Override
	public void onClick(View v) {
		if (standalone){
			this.getActivity().finish();
		}else{
			Intent intent = new Intent(this.getActivity(), EventActivity.class)
				.putExtra(Util.pGeneral, event);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			this.getActivity().startActivity(intent);
		}
	}

}
