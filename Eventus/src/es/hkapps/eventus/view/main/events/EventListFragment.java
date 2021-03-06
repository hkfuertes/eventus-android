package es.hkapps.eventus.view.main.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import es.hkapps.eventus.R;
import es.hkapps.eventus.api.Util;
import es.hkapps.eventus.model.Event;
import es.hkapps.eventus.model.EventHelper;
import es.hkapps.eventus.model.User;
import es.hkapps.eventus.view.events.EventActivity;
import es.hkapps.eventus.view.events.create.EventEditActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
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
			Collections.sort(eventList,new EventComparator());
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
		selected.retrieveInfo(user);
		Event event = eHelper.retrieveEventByKey(selected.getKey());
		if(event == null){
			eHelper.save(selected);
		}
		
		EventActivity.launch(this.getActivity(), selected);
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
		switch(id){
		case R.id.event_list_refresh: this.refreshList(); break;
		case R.id.event_list_create: EventEditActivity.launch(this.getActivity()); break;
		case R.id.event_list_join: createDialog().show(); break;
		}
		if (id == R.id.event_list_refresh) {
			this.refreshList();
			return true;
		}else{
			
		}
		return super.onOptionsItemSelected(item);
	}
	
	public Dialog createDialog() {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    
	    builder.setTitle(R.string.dialog_join_title);
	    //builder.setIcon(R.drawable.ic_action_new);
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(inflater.inflate(R.layout.dialog_event_join, null))
	    // Add action buttons
	           .setPositiveButton(R.string.dialog_join, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   Dialog d = (Dialog) dialog;
	            	   EditText dJoin = (EditText) d.findViewById(R.id.dialog_event_join_key);
	                   Event event = new Event(dJoin.getText().toString());
	                   Toast.makeText(getActivity(), dJoin.getText().toString(), Toast.LENGTH_LONG).show();
	                   event.join(user);
	                   if(event != null){
	                	   //Join devuelve null si ha habido un error.
		                   eventList.add(event);
		                   adapter.notifyDataSetChanged();
	                   }
	               }
	           })
	           .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   dialog.cancel();
	               }
	           });      
	    return builder.create();
	}
	
}

class EventComparator implements Comparator<Event> {
    @Override
    public int compare(Event o1, Event o2) {
        return o1.getDateObject().compareTo(o2.getDateObject());
    }
}
