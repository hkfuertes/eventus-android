package es.hkapps.eventus.view.events.program;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import es.hkapps.eventus.R;
import es.hkapps.eventus.api.Util;
import es.hkapps.eventus.model.Event;
import es.hkapps.eventus.model.ProgramEntry;
import es.hkapps.eventus.model.User;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EventProgramFragment extends ListFragment {
	private static final String ARGUMENT_ID = "evento";
	
	private static final String FORMAT = "HH:mm:ss "+Event.FORMAT;

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
	    setHasOptionsMenu(true);
	    
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
			ProgramEditActivity.launch(getActivity(), event);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
