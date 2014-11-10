package es.hkapps.eventus.view.events.photos.gallery;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import es.hkapps.eventus.R;
import es.hkapps.eventus.api.Util;
import es.hkapps.eventus.camera.CameraActivity;
import es.hkapps.eventus.model.Photo;
import es.hkapps.eventus.model.User;
import es.hkapps.eventus.view.events.create.EventEditActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class GalleryActivity extends FragmentActivity implements OnPageChangeListener {


	private TouchImageAdapter mAdapter;
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

		mAdapter = new TouchImageAdapter(photos);

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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.gallery, menu);
		return true;
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.gallery_action_share:
			Intent share = new Intent(Intent.ACTION_SEND);
			share.setType("image/*");
			share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			share.putExtra(Intent.EXTRA_STREAM, Uri.parse(photos.get(mPager.getCurrentItem()).getUrl()));
			startActivity(Intent.createChooser(share, getResources().getString(R.string.share_using)));
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
}
