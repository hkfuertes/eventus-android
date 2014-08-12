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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ParticipantsListFragment extends ListFragment {

	private User user;
	TextView nombre, email;
	Event event;

	/* Singleton */
	public static ParticipantsListFragment newInstance(Event event) {
		ParticipantsListFragment fragment = new ParticipantsListFragment();
		Bundle bundle = new Bundle();
	    bundle.putSerializable("event", event);
	    fragment.setArguments(bundle);
		return fragment;
	}

	public ParticipantsListFragment() {
		// Required empty public constructor
	}

	@Override
	public void onResume() {
		super.onResume();
		user = Util.getUser(this.getActivity());
		event = (Event) this.getArguments().getSerializable("event");
		event.retrieveInfo(user.getUsername(), user.getToken());
		ArrayList<String> program = event.getParticipants();
		if (program == null) Log.d("program", "null");
        this.setListAdapter(new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, program.toArray(new String[0])));

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
		return inflater.inflate(R.layout.fragment_participants_list, container, false);
	}
}

