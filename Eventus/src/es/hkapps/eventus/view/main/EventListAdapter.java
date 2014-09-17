package es.hkapps.eventus.view.main;

import java.util.ArrayList;

import es.hkapps.eventus.R;
import es.hkapps.eventus.model.Event;
import android.app.Activity;
import android.content.Context;
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EventListAdapter extends BaseAdapter  {

        private LayoutInflater layoutInflater;
        private ArrayList<Event> feed;

        public EventListAdapter(Activity activity, ArrayList<Event> feed) {

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
        public Event getItem(int position) {
            return feed.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        
        public void refresh(ArrayList<Event> feed){
        	this.feed = feed;
        	this.notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // Inflate the item layout and set the views
            View listItem = convertView;
            if (listItem == null) {
                listItem = layoutInflater.inflate(R.layout.event_list_item, null);
            }

            TextView name = (TextView) listItem.findViewById(R.id.program_act);
            TextView date = (TextView) listItem.findViewById(R.id.event_date);
            TextView type = (TextView) listItem.findViewById(R.id.program_time);
            TextView place = (TextView) listItem.findViewById(R.id.event_place);

            // Set the views in the layout
            name.setText(this.getItem(position).getName());
            date.setText(this.getItem(position).getDate());
            type.setText(this.getItem(position).getType());
            place.setText(this.getItem(position).getPlace());

            return listItem;
        }
    }