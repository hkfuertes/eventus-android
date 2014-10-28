package es.hkapps.eventus.view.events.participants;

import java.util.ArrayList;

import es.hkapps.eventus.R;
import es.hkapps.eventus.api.Util;
import es.hkapps.eventus.model.Event;
import es.hkapps.eventus.model.User;
import es.hkapps.eventus.view.events.create.EventStepListener;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.Fragment;
import android.util.Log;
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

public class InviteParticipantsListFragment extends Fragment implements OnItemClickListener {
	private static final int CONTACT_PICKER_RESULT = 42;
	private static final String DEBUG_TAG = "picker";
	TextView name, place;
	Spinner type;
	CalendarView date;
	private EventStepListener listener;

	Event event;
	ArrayList<String> participants = new ArrayList<String>();
	private ListView list;
	private ArrayAdapter<String> adapter;
	private User user;

	/* Singleton */
	public static InviteParticipantsListFragment newInstance(Event event) {
		InviteParticipantsListFragment fragment = new InviteParticipantsListFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("event", event);
		fragment.setArguments(bundle);
		return fragment;
	}

	public InviteParticipantsListFragment() {
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
		inflater.inflate(R.menu.fragment_event_invite, menu);
		//menu.add(Menu.NONE, R.id.fragment_event_invite_pick, Menu.NONE, "+");
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
		case R.id.fragment_event_invite_pick:
			this.doLaunchContactPicker(null);
		}
		return super.onOptionsItemSelected(item);
	}

	public void setStepListener(EventStepListener listener) {
		this.listener = listener;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		participants.remove(position);
		adapter.notifyDataSetChanged();
	}

	
	public void doLaunchContactPicker(View view) {
	    Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,Contacts.CONTENT_URI);
	    startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
	    this.getActivity();
		if (resultCode == Activity.RESULT_OK) {
	        switch (requestCode) 
	        {
	        case CONTACT_PICKER_RESULT:
	            Cursor cursor = null;
	            String email = "", name = "";
	            try {
	                Uri result = data.getData();
	                Log.v(DEBUG_TAG, "Got a contact result: " + result.toString());

	                // get the contact id from the Uri
	                String id = result.getLastPathSegment();

	                // query for everything email
	                cursor = this.getActivity().getContentResolver().query(Email.CONTENT_URI,  null, Email.CONTACT_ID + "=?", new String[] { id }, null);
	                int nameId = cursor.getColumnIndex(Contacts.DISPLAY_NAME);
	                int emailIdx = cursor.getColumnIndex(Email.DATA);

	                // let's just get the first email
	                if (cursor.moveToFirst()) {
	                    email = cursor.getString(emailIdx);
	                    name = cursor.getString(nameId);
	                    Log.v(DEBUG_TAG, "Got email: " + email);
	                } else {
	                    Log.w(DEBUG_TAG, "No results");
	                }
	            } catch (Exception e) {
	                Log.e(DEBUG_TAG, "Error obteniendo email.", e);
	            } finally {
	                if (cursor != null) {
	                    cursor.close();
	                }
	                participants.add(email);
	    			adapter.notifyDataSetChanged();
	                if (email.length() == 0 && name.length() == 0) 
	                {
	                    Toast.makeText(this.getActivity(), "El contacto no tiene Email.",Toast.LENGTH_LONG).show();
	                }
	            }
	            break;
	        }
			Toast.makeText(this.getActivity(),"Contacto añadido | Seleccionar en la lista para cancelar.", Toast.LENGTH_LONG).show();
	    }
	}

}
