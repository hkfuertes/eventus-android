package es.hkapps.eventus.view.main.events;

import java.util.ArrayList;

import es.hkapps.eventus.R;
import es.hkapps.eventus.api.Util;
import es.hkapps.eventus.model.Event;
import es.hkapps.eventus.model.EventHelper;
import es.hkapps.eventus.model.User;
import es.hkapps.eventus.view.events.EventActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.ListFragment;

public class EventListFragment extends ListFragment {

	private User user;
	TextView nombre, email;

	private ArrayList<Event> eventList;
	private EventListAdapter adapter;
	private EventHelper eHelper;

	/* Singleton */
	public static EventListFragment newInstance() {
		EventListFragment fragment = new EventListFragment();
		return fragment;
	}

	public EventListFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		user = Util.getUser(this.getActivity());
		eventList = Event
				.retrieveEventList(user.getUsername(), user.getToken());
		if (eventList == null) {
			eventList = new ArrayList<Event>();
		}
		adapter = new EventListAdapter(this.getActivity(), eventList);
		this.setListAdapter(adapter);
		setHasOptionsMenu(true);
		
		eHelper = new EventHelper(this.getActivity());
	}
	
	private void refreshList(){
		eventList = Event
				.retrieveEventList(user.getUsername(), user.getToken());
		if (eventList != null) {
			adapter.refresh(eventList);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		this.refreshList();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		Event selected = eventList.get(position);
		selected.retrieveInfo(user.getUsername(), user.getToken());
		Event event = eHelper.retrieveEventByKey(selected.getKey());
		if(event == null){
			eHelper.save(selected);
		}
		
		Intent intent = new Intent(this.getActivity(), EventActivity.class)
				.putExtra(Util.pGeneral, selected);
		this.getActivity().startActivity(intent);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_event_list, container, false);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.event_list, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.event_list_refresh) {
			this.refreshList();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}