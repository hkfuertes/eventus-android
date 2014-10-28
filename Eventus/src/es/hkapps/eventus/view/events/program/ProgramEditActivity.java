package es.hkapps.eventus.view.events.program;

import java.util.ArrayList;

import es.hkapps.eventus.R;
import es.hkapps.eventus.api.Util;
import es.hkapps.eventus.camera.CameraActivity;
import es.hkapps.eventus.model.Event;
import es.hkapps.eventus.model.ProgramEntry;
import es.hkapps.eventus.model.User;
import es.hkapps.eventus.view.events.create.EventEditActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class ProgramEditActivity extends ActionBarActivity {

	Event event;
	private ArrayList<ProgramEntry> program;
	private ProgramEntryListAdapter adapter;

	private ListView list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_program_list_edit);

	    Bundle args = this.getIntent().getExtras();
	    if (args != null) {
	    	event = (Event) args.getSerializable(Event.EVENT_TAG);
	    	if(event != null){
		    	program = event.getProgram();
				program = ProgramEntry.sort(program);
	    	}else{
	    		program = new ArrayList<ProgramEntry>();
	    	}
	    	adapter = new ProgramEntryListAdapter(this,program, true);
			
	    	list = (ListView) findViewById(R.id.activity_program_list_edit);
	    	list.setAdapter(adapter);
	    }    
	}
	
	public static void launch(Activity activity, Event event) {
		Intent intent = new Intent(activity,ProgramEditActivity.class);
		intent.putExtra(Event.EVENT_TAG, event);
		activity.startActivity(intent);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.program_edit, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.program_edit_save:

			break;
		case R.id.program_edit_add_entry:

			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
