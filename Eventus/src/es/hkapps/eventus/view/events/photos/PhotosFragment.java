package es.hkapps.eventus.view.events.photos;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import es.hkapps.eventus.R;
import es.hkapps.eventus.api.Util;
import es.hkapps.eventus.model.Event;
import es.hkapps.eventus.model.Photo;
import es.hkapps.eventus.model.PhotoHelper;
import es.hkapps.eventus.model.User;
import es.hkapps.eventus.view.events.photos.gallery.GalleryActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class PhotosFragment extends Fragment implements OnItemClickListener {	
	private static final String ARGUMENT_ID = "evento";

	Event event;
	User user;

	private ArrayList<Photo> photos;
	private PhotosAdapter adapter;
	private GridView gridview;

	private PhotoHelper helper;

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
	
	private void refresh() {
		//Refrescamos la lista.
		 photos = helper.retrievePhotosFromEvent(event);
		 adapter.notifyDataSetChanged();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setHasOptionsMenu(true);
	    //init image library
	    ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this.getActivity()));
	}
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_photos, container, false);
		
		helper = new PhotoHelper(this.getActivity());
	    Bundle args = getArguments();
	    if (args != null) {
	    	event = (Event) args.getSerializable(ARGUMENT_ID);
	    	user = Util.getUser(getActivity());
	    	Photo.updateDatabase(this.getActivity(), user, event);
	    }
	    photos = helper.retrievePhotosFromEvent(event);
		adapter = new PhotosAdapter(this.getActivity(), photos);
		
		gridview = (GridView) v.findViewById(R.id.fragment_photos_view);
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(this);

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
		Toast.makeText(this.getActivity(), "foto touched: "+position, Toast.LENGTH_LONG).show();
		GalleryActivity.launch(getActivity(), photos, position);
	}
	
}

