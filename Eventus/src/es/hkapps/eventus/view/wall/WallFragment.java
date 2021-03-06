package es.hkapps.eventus.view.wall;

import java.util.ArrayList;

import es.hkapps.eventus.R;
import es.hkapps.eventus.api.Util;
import es.hkapps.eventus.model.Event;
import es.hkapps.eventus.model.User;
import es.hkapps.eventus.model.WallEntry;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class WallFragment extends ListFragment {
	private User user;
	TextView nombre, email;

	private ArrayList<Event> eventList;
	private BaseAdapter adapter;
	private ArrayList<WallEntry> wall;

	/* Singleton */
	public static WallFragment newInstance() {
		WallFragment fragment = new WallFragment();
		return fragment;
	}
	
	/* Singleton */
	public static WallFragment newInstance(Event event) {
		WallFragment fragment = new WallFragment();
		return fragment;
	}

	public WallFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		user = Util.getUser(this.getActivity());
		wall = WallEntry.retrieveWallEntries(user.getUsername(),  user.getToken(), "bla");
		adapter = new WallAdapter(this.getActivity(),wall);
		this.setListAdapter(adapter);
		/*
		eventList = Event.retrieveEventList(user.getUsername(), user.getToken());
		if(eventList == null){
			eventList = new ArrayList<Event>();
		}
		adapter = new EventListAdapter(this.getActivity(),eventList);
        this.setListAdapter(adapter);
        */
	}
	
	@Override
	public void onResume(){
		super.onResume();

	}
	
	@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_event_list, container, false);
	}
}
