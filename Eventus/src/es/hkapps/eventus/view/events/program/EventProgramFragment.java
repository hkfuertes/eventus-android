package es.hkapps.eventus.view.events.program;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import es.hkapps.eventus.R;
import es.hkapps.eventus.api.Util;
import es.hkapps.eventus.model.Event;
import es.hkapps.eventus.model.ProgramEntry;
import es.hkapps.eventus.model.User;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class EventProgramFragment extends ListFragment {
	private static final String ARGUMENT_ID = "evento";
	
	private static final String FORMAT = "HH:mm:ss "+Event.FORMAT;

	TextView nombre, email;
	Event event;
	private ArrayList<ProgramEntry> program;
	private ProgramEntryListAdapter adapter;

	private TimePicker time;

	private EditText act;

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
	    setHasOptionsMenu(true);
	    
	    Bundle args = getArguments();
	    if (args != null) {
	    	event = (Event) args.getSerializable(ARGUMENT_ID);
	    	if(event != null){
		    	program = event.getProgram();
				ProgramEntry.sort(program);
	    	}else{
	    		program = new ArrayList<ProgramEntry>();
	    	}
	    	adapter = new ProgramEntryListAdapter(this.getActivity(),program);
			this.setListAdapter(adapter);
	    }    
	}
	

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		ProgramEntry pe = program.get(position);
		String actTime = pe.getTime("HH:mm:ss");
		String eventDate = event.getDate();
		String toParse = actTime + " "+eventDate;
		String toParseNext = toParse;
		
		if(program.size() < position + 1){
			ProgramEntry peNext = program.get(position + 1);
			String actTimeNext = peNext.getTime("HH:mm:ss");
			String eventDateNext = event.getDate();
			toParseNext = actTime + " "+eventDate;
		}
		
		SimpleDateFormat format = new SimpleDateFormat(FORMAT);
		try {
		    Date peDate = format.parse(toParse);
		    Date peNextDate = format.parse(toParseNext);
		    this.addToCalendar(peDate.getTime(), peNextDate.getTime(), pe.getAct(), event.getPlace(), event.getName());
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		// Mostramos un mensaje con el elemento pulsado
		/*Toast.makeText(getActivity(),
				"Abriendo Calendar para añadir notificacion.",
				Toast.LENGTH_SHORT).show();*/
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_program_list, container,
				false);
		return view;
	}
	
	public void addToCalendar(long time, long timeNext, String text, String place, String description){
		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra("beginTime", time);
		intent.putExtra("allDay", false);
		//intent.putExtra("rrule", "FREQ=YEARLY");
		//intent.putExtra("endTime", timeNext);
		intent.putExtra("title", text);
		intent.putExtra("eventLocation", place);
		intent.putExtra("description",description);
		startActivity(intent);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_program, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.fragment_program_edit:
			//createDialog().show();
			this.getActivity().startActionMode( mActionModeCallback);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

	    // Called when the action mode is created; startActionMode() was called
	    @Override
	    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
	        // Inflate a menu resource providing context menu items
	        MenuInflater inflater = mode.getMenuInflater();
	        inflater.inflate(R.menu.program_context_menu, menu);
	        return true;
	    }

	    // Called each time the action mode is shown. Always called after onCreateActionMode, but
	    // may be called multiple times if the mode is invalidated.
	    @Override
	    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
	        return false; // Return false if nothing is done
	    }

	    // Called when the user selects a contextual menu item
	    @Override
	    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
	        switch (item.getItemId()) {
	        case R.id.fragment_program_context_add:
	        	createDialog().show();
	            default:
	                return false;
	        }
	    }

	    // Called when the user exits the action mode
	    @Override
	    public void onDestroyActionMode(ActionMode mode) {
	    }
	};

	
	public Dialog createDialog() {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    
	    builder.setTitle(R.string.dialog_add_title);
	    //builder.setIcon(R.drawable.ic_action_new);
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    
	    View v = inflater.inflate(R.layout.program_entry_list_item_edit, null);
	    
	    time = (TimePicker) v.findViewById(R.id.program_time);
	    time.setIs24HourView(true);
	    act = (EditText) v.findViewById(R.id.program_act);
	    
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(v)
	    // Add action buttons
	           .setPositiveButton(R.string.dialog_add, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   String date = time.getCurrentHour()+":"+time.getCurrentMinute()+":00 "+event.getDate();
	            	   //Log.d("fecha",date);
	            	   ProgramEntry pEntry = new ProgramEntry(event.getKey(), date ,act.getText().toString());
	            	   program.add(pEntry);
	            	   ProgramEntry.sort(program);
	            	   adapter.notifyDataSetChanged();
	            	   ProgramEntry.updateProgram(Util.getUser(getActivity()), event, program);
	            	   //adapter.notifyDataSetChanged();
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
