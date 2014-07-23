package es.hkapps.eventus;

import es.hkapps.eventus.api.User;
import es.hkapps.eventus.api.Util;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class UserFragment extends Fragment {
	private User user;
	private EditText nombre, apellidos, email;

	/* Singleton */
	public static UserFragment newInstance() {
		UserFragment fragment = new UserFragment();
		return fragment;
	}

	public UserFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		user = Util.getUser(this.getActivity());
		if(user.retrieveInfo()) Util.setUser(this.getActivity(), user);
		
		nombre = (EditText) getView().findViewById(R.id.info_nombre);
		nombre.setText(user.getNombre());
		nombre.setEnabled(false);
		
		apellidos = (EditText) getView().findViewById(R.id.info_apellidos);
		apellidos.setText(user.getApellidos());
		apellidos.setEnabled(false);
		
		email = (EditText) getView().findViewById(R.id.info_email);
		email.setText(user.getEmail());
		email.setEnabled(false);

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