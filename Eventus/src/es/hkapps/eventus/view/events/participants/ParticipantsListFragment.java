package es.hkapps.eventus.view.events.participants;

import java.util.ArrayList;

import es.hkapps.eventus.R;
import es.hkapps.eventus.api.Util;
import es.hkapps.eventus.model.Event;
import es.hkapps.eventus.model.User;
import es.hkapps.eventus.view.user.UserActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ParticipantsListFragment extends ListFragment {
	public static final String ARGUMENT_ID = "asistentes";

	TextView nombre, email;
	Event event;
	private ArrayList<String> parts;

	private ArrayAdapter<String> adapter;

	/* Singleton */
	public static ParticipantsListFragment newInstance(Event event) {
		ParticipantsListFragment fragment = new ParticipantsListFragment();
		Bundle bundle = new Bundle();
	    bundle.putSerializable(ARGUMENT_ID, event);
	    fragment.setArguments(bundle);
		return fragment;
	}

	public ParticipantsListFragment() {
		// Required empty public constructor
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    Bundle args = getArguments();
	    if (args != null) {
	    	event = (Event) args.getSerializable(ARGUMENT_ID);
	    	if(event != null){
	    		parts = event.getParticipants();
	            this.setListAdapter(new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, parts.toArray(new String[0])));
	    	}else{
	    		parts = new ArrayList<String>();
	    	}
	    }
	    adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, parts.toArray(new String[0]));
	    this.setListAdapter(adapter);
	}
	

	@Override
	public void onResume() {
		super.onResume();
		if(event != null){
			parts = event.getParticipants();
			adapter.notifyDataSetChanged();
		}
	}
	
	@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
 
        User current = Util.getUser(this.getActivity());
        
        Intent i=new Intent(this.getActivity(),UserActivity.class);
        i.putExtra(Util.pUser, current.retrieveUser(parts.get(position)));
        this.getActivity().startActivity(i);
        
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_participants_list, container, false);
	}
}

