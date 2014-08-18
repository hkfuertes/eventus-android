package es.hkapps.eventus.view.events;

import java.util.ArrayList;

import es.hkapps.eventus.R;
import es.hkapps.eventus.model.Event;
import es.hkapps.eventus.model.ProgramEntry;
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
	private static final String ARGUMENT_ID = "evento";

	TextView nombre, email;
	Event event;
	private ArrayList<ProgramEntry> program;
	private ProgramEntryListAdapter adapter;

	/* Singleton */
	public static EventProgramFragment newInstance(Event event) {
		EventProgramFragment fragment = new EventProgramFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(ARGUMENT_ID, event);
		fragment.setArguments(bundle);
		Log.d("eventlist",event.getName());
		return fragment;
	}

	public EventProgramFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    Bundle args = getArguments();
	    if (args != null) {
	    	event = (Event) args.getSerializable(ARGUMENT_ID);
	    	if(event != null){
		    	program = event.getProgram();
				program = ProgramEntry.sort(program);
	    	}else{
	    		program = new ArrayList<ProgramEntry>();
	    	}
	    	adapter = new ProgramEntryListAdapter(this.getActivity(),program);
			this.setListAdapter(adapter);
	    }    
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(event != null){
			program = event.getProgram();
			program = ProgramEntry.sort(program);
			adapter.notifyDataSetChanged();
		}
	}
	

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		// Mostramos un mensaje con el elemento pulsado
		Toast.makeText(getActivity(),
				"Abriendo Calendar para añadir notificacion.",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_program_list, container,
				false);
		return view;
	}
}
