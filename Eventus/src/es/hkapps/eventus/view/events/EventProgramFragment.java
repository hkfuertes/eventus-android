package es.hkapps.eventus.view.events;

import java.util.ArrayList;

import es.hkapps.eventus.R;
import es.hkapps.eventus.api.Util;
import es.hkapps.eventus.model.Event;
import es.hkapps.eventus.model.ProgramEntry;
import es.hkapps.eventus.model.User;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EventProgramFragment extends ListFragment {

	private User user;
	TextView nombre, email;
	Event event;

	/* Singleton */
	public static EventProgramFragment newInstance(Event event) {
		EventProgramFragment fragment = new EventProgramFragment();
		Bundle bundle = new Bundle();
	    bundle.putSerializable("event", event);
	    fragment.setArguments(bundle);
		return fragment;
	}

	public EventProgramFragment() {
		// Required empty public constructor
	}

	@Override
	public void onResume() {
		super.onResume();
		user = Util.getUser(this.getActivity());
		event = (Event) this.getArguments().getSerializable("event");
		event.retrieveInfo(user.getUsername(), user.getToken());
		ArrayList<ProgramEntry> program = event.getProgram();
		if (program == null) Log.d("program", "null");
        this.setListAdapter(new ProgramEntryListAdapter(this.getActivity(),program));

	}
	
	@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
 
        // Mostramos un mensaje con el elemento pulsado
        Toast.makeText(getActivity(), "Abriendo Calendar para añadir notificacion.",
                Toast.LENGTH_SHORT).show();
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_program_list, container, false);
	}
}

