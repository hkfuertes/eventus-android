package es.hkapps.eventus.view.events;
import java.util.ArrayList;

import es.hkapps.eventus.R;
import es.hkapps.eventus.api.Util;
import es.hkapps.eventus.model.Event;
import es.hkapps.eventus.model.User;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.ListFragment;

public class EventListFragment extends ListFragment {

	private User user;
	TextView nombre, email;

	private ArrayList<Event> eventList;

	/* Singleton */
	public static EventListFragment newInstance() {
		EventListFragment fragment = new EventListFragment();
		return fragment;
	}

	public EventListFragment() {
		// Required empty public constructor
	}

	@Override
	public void onResume() {
		super.onResume();
		user = Util.getUser(this.getActivity());
		eventList = Event.retrieveEventList(user.getUsername(), user.getToken());
        this.setListAdapter(new EventListAdapter(this.getActivity(),eventList));

	}
	
	@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
 
        Event selected = eventList.get(position);
        Intent intent = new Intent(this.getActivity(),EventActivity.class).putExtra("event",selected);
        this.getActivity().startActivity(intent);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_event_list, container, false);
	}
}
