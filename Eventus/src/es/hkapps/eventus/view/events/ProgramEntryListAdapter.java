package es.hkapps.eventus.view.events;

import java.util.ArrayList;

import es.hkapps.eventus.R;
import es.hkapps.eventus.model.Event;
import es.hkapps.eventus.model.ProgramEntry;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ProgramEntryListAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private ArrayList<ProgramEntry> feed;

    public ProgramEntryListAdapter(Activity activity, ArrayList<ProgramEntry> feed) {

        this.feed = feed;

        layoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        int pos = position;
        if (listItem == null) {
            listItem = layoutInflater.inflate(R.layout.program_entry_list_item, null);
        }

        TextView time = (TextView) listItem.findViewById(R.id.program_time);
        TextView act = (TextView) listItem.findViewById(R.id.program_act);

        // Set the views in the layout
        time.setText(this.getItem(position).getTime("HH:mm"));
        act.setText(this.getItem(position).getAct());

        return listItem;
    }
}
