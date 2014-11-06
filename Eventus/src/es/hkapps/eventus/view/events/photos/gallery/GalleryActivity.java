package es.hkapps.eventus.view.events.photos.gallery;

import java.util.ArrayList;

import es.hkapps.eventus.R;
import es.hkapps.eventus.model.Photo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

public class GalleryActivity extends FragmentActivity implements OnPageChangeListener {


	private SlidesFragmentAdapter mAdapter;
	private ViewPager mPager;
	private ArrayList<Photo> photos;
	private int position;
	
	private void retrieveParams(){
		Intent self = this.getIntent();
		photos = (ArrayList<Photo>)self.getSerializableExtra(Photo.ARRAY_TAG);
		position = self.getExtras().getInt(Photo.POSITION_TAG);
		if(photos == null) photos = new ArrayList<Photo>();
	}
	
	public static void launch(Activity act, ArrayList<Photo> photos, int position){
		Intent gallery = new Intent(act,GalleryActivity.class);
		gallery.putExtra(Photo.ARRAY_TAG, photos);
		gallery.putExtra(Photo.POSITION_TAG, position);
		act.startActivity(gallery);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_gallery);
		
		this.retrieveParams();

		final View contentView = findViewById(R.id.fullscreen_content);

		mAdapter = new SlidesFragmentAdapter(this.getSupportFragmentManager(),
				photos);

		mPager = (ViewPager) contentView;
		mPager.setAdapter(mAdapter);
		
		mPager.setOnPageChangeListener(this);		
		mPager.setCurrentItem(position);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int pos) {
		Photo current = photos.get(pos);
		
		this.setTitle(current.getDate(false));
	}
}
