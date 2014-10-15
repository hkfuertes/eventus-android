package es.hkapps.eventus.view.events.photos;

import java.util.ArrayList;

import es.hkapps.eventus.R;
import es.hkapps.eventus.api.Util;
import es.hkapps.eventus.model.Event;
import es.hkapps.eventus.model.Photo;
import es.hkapps.eventus.model.PhotoHelper;
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
import android.widget.GridView;
import android.widget.Spinner;

public class PhotosFragment extends Fragment implements OnItemClickListener {	
	private static final String ARGUMENT_ID = "evento";

	Event event;
	User user;

	private ArrayList<Photo> photos;
	private PhotosAdapter adapter;
	private GridView gridview;

	private PhotoHelper helper;

	private Button actualizar;

	public static PhotosFragment newInstance(Event event) {
		PhotosFragment fragment = new PhotosFragment();
		Bundle bundle = new Bundle();
	    bundle.putSerializable(ARGUMENT_ID, event);
	    fragment.setArguments(bundle);
		return fragment;
	}

	public static PhotosFragment newInstance() {
		PhotosFragment fragment = new PhotosFragment();
		return fragment;
	}

	public PhotosFragment() {
		// Required empty public constructor
	}
	
	 @Override
	  public void onResume() {
	     this.refresh();
	     super.onResume();
	  }
	
	private void refresh() {
		//Refrescamos la lista.
		 photos = helper.retrievePhotos();
		 adapter.notifyDataSetChanged();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    helper = new PhotoHelper(this.getActivity());
	    Bundle args = getArguments();
	    if (args != null) {
	    	event = (Event) args.getSerializable(ARGUMENT_ID);
	    	user = Util.getUser(getActivity());
	    	//photos = helper.retrievePhotosFromEvent(event);
	    	photos = Photo.getFromEvent(user, event);
	    }else{
	    	photos = helper.retrievePhotos();
	    }
	    setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_photos, container, false);
		
		String[] categories = new String[]{"Todas", "En Dispositivo","Subidas"};
		
		Spinner spinner = (Spinner) v.findViewById(R.id.fragment_photos_category);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, categories); //selected item will look like a spinner set from XML
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(spinnerArrayAdapter);
		
		adapter = new PhotosAdapter(this.getActivity(), photos);
		
		gridview = (GridView) v.findViewById(R.id.fragment_photos_view);
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(this);
		
		actualizar = (Button) v.findViewById(R.id.fragment_photos_refresh_button);
		actualizar.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				refresh();
			}
			
		});
		
		return v;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_photos, menu);
	    super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.fragment_photos_refresh:
			this.refresh();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Photo p = photos.get(position);
		
		Bundle bundle = new Bundle();  
		bundle.putSerializable("event",event);
	}
	
}

