package es.hkapps.eventus.view;

import es.hkapps.eventus.R;
import es.hkapps.eventus.api.Util;
import es.hkapps.eventus.model.User;
import es.hkapps.eventus.view.events.EventProgramFragment;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class UserFragment extends Fragment {
	private User user;
	TextView nombre, email;

	/* Singleton */
	public static EventProgramFragment newInstance(User user) {
		EventProgramFragment fragment = new EventProgramFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("user", user);
		fragment.setArguments(bundle);
		return fragment;
	}

	public UserFragment() {
		// Required empty public constructor
	}
	
	public UserFragment(User user) {
		this.user = user;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		if(user == null)
			user = (User) this.getArguments().getSerializable("user");
		
		nombre = (TextView) getView().findViewById(R.id.event_info_name);
		nombre.setText(user.getNombreCompleto());
		
		email = (TextView) getView().findViewById(R.id.info_mail);
		email.setText(user.getEmail());

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_user, container, false);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}
}
