package es.hkapps.eventus.view.events.create;

import java.util.ArrayList;

import es.hkapps.eventus.R;
import es.hkapps.eventus.api.Util;
import es.hkapps.eventus.model.Event;
import es.hkapps.eventus.model.User;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class InviteParticipantsFragment extends Fragment implements OnClickListener, OnItemClickListener {
	TextView name, place;
	Spinner type;
	CalendarView date;
	private EventCreationStepListener listener;

	Event event;
	ArrayList<String> participants = new ArrayList<String>();
	private Button add;
	private ListView list;
	private ArrayAdapter<String> adapter;
	private User user;

	/* Singleton */
	public static InviteParticipantsFragment newInstance(Event event) {
		InviteParticipantsFragment fragment = new InviteParticipantsFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("event", event);
		fragment.setArguments(bundle);
		return fragment;
	}

	public InviteParticipantsFragment() {
		// Required empty public constructor
	}

	public void onResume() {
		Bundle args = this.getArguments();
		event = (Event) args.getSerializable("event");
		
		user = Util.getUser(getActivity());
		
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_invite_participants,
				container, false);

		name = (TextView) v.findViewById(R.id.fragment_invite_participants_name);
		add = (Button) v.findViewById(R.id.fragment_invite_participants_add);
		add.setOnClickListener(this);
		list = (ListView) v.findViewById(R.id.fragment_invite_participants_list);

		adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, participants);
		
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
		
		return v;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_event_create, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.fragment_event_create:
			Toast.makeText(this.getActivity(), "Invitando: " + event.getName(),
					Toast.LENGTH_LONG).show();
			
			event.inviteParticipants(user, participants);

			if (listener != null)
				listener.onStepCompleted(this, event);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void setStepListener(EventCreationStepListener listener) {
		this.listener = listener;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		participants.remove(position);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		String nameStr;
		if((nameStr = name.getText().toString())!= ""){
			participants.add(nameStr);
			adapter.notifyDataSetChanged();
			name.setText("");
		}
	}

}
