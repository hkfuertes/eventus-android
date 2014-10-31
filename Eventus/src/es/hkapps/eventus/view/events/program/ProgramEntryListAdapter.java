package es.hkapps.eventus.view.events.program;

import java.util.ArrayList;

import es.hkapps.eventus.R;
import es.hkapps.eventus.model.Event;
import es.hkapps.eventus.model.ProgramEntry;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class ProgramEntryListAdapter extends BaseAdapter implements OnCheckedChangeListener {

	private LayoutInflater layoutInflater;
	private ArrayList<ProgramEntry> feed;
	private boolean editing = false;
	private ArrayList<ProgramEntry> forRemove;

	public ProgramEntryListAdapter(Activity activity,
			ArrayList<ProgramEntry> feed) {
		this.forRemove = new ArrayList<ProgramEntry>();
		this.feed = feed;

		layoutInflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public ProgramEntryListAdapter(Activity activity,
			ArrayList<ProgramEntry> program, boolean editing) {
		this(activity,program);
		this.editing = editing;
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
			listItem = layoutInflater.inflate(R.layout.program_entry_list_item,
					null);
			TextView time = (TextView) listItem.findViewById(R.id.program_time);
			time.setText(item.getTime("HH:mm"));
			TextView act = (TextView) listItem.findViewById(R.id.program_act);
			act.setText(item.getAct());

			CheckBox delete = (CheckBox) listItem.findViewById(R.id.program_delete);
			delete.setOnCheckedChangeListener(this);
			delete.setTag(position);
			if(!editing) delete.setVisibility(View.GONE);
		}
		return listItem;
	}

	public ArrayList<ProgramEntry> getList() {
		return feed;
	}

	public void addNew(Event event) {
		feed.add(new ProgramEntry(event.getKey()));
		this.notifyDataSetChanged();
	}

	public ArrayList<ProgramEntry> getForRemove() {
		// TODO Auto-generated method stub
		return forRemove;
	}

	@Override
	public void onCheckedChanged(CompoundButton view, boolean isChecked) {
		ProgramEntry entry = this.getItem((Integer) view.getTag());
		if(isChecked){
			forRemove.add(entry);
		}else{
			forRemove.remove(entry);
		}
	}
}
