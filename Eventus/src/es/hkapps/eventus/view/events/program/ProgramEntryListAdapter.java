package es.hkapps.eventus.view.events.program;

import java.util.ArrayList;

import es.hkapps.eventus.R;
import es.hkapps.eventus.model.ProgramEntry;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class ProgramEntryListAdapter extends BaseAdapter implements OnClickListener {

    private LayoutInflater layoutInflater;
    private ArrayList<ProgramEntry> feed;
    private boolean edit = false;
	private Button remove;

    public ProgramEntryListAdapter(Activity activity, ArrayList<ProgramEntry> feed) {

        this.feed = feed;

        layoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public ProgramEntryListAdapter(Activity activity, ArrayList<ProgramEntry> feed, boolean edit) {
    	this(activity,feed);
    	this.edit = edit;
    }

    @Override
    public int getCount() {
        // Set the total list item count
        return feed.size();
    }

    @Override
    public ProgramEntry getItem(int position) {
        return feed.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Inflate the item layout and set the views
        View listItem = convertView;
        ProgramEntry item = this.getItem(position);
        if (listItem == null) {
        	if(edit){
        		listItem = layoutInflater.inflate(R.layout.program_entry_list_item_edit, null);
	        	remove = (Button) listItem.findViewById(R.id.program_remove);
	            remove.setTag(position);
	            remove.setOnClickListener(this);
	            
	            TimePicker time = (TimePicker) listItem.findViewById(R.id.program_time);
	            time.setIs24HourView(true);
	            time.setCurrentHour(item.getHour());
	            time.setCurrentMinute(item.getMinutes());
	            
	            EditText act = (EditText) listItem.findViewById(R.id.program_act);
                act.setText(item.getAct());
        	}
        	else{
        		listItem = layoutInflater.inflate(R.layout.program_entry_list_item, null);
        		TextView time = (TextView) listItem.findViewById(R.id.program_time);
        		time.setText(item.getTime("HH:mm"));
        		TextView act = (TextView) listItem.findViewById(R.id.program_act);
                act.setText(item.getAct());
        	}
        		
        }
        return listItem;
    }
    
    
    public ArrayList<ProgramEntry> getList(){
		return feed;
    }

	@Override
	public void onClick(View v) {
		int position = (Integer) v.getTag();
		this.feed.remove(position);
		this.notifyDataSetChanged();
	}
}
