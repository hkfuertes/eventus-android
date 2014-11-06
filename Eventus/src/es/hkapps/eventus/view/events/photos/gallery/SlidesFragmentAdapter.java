package es.hkapps.eventus.view.events.photos.gallery;

import java.util.ArrayList;

import es.hkapps.eventus.model.Photo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SlidesFragmentAdapter extends FragmentPagerAdapter  {
	
	private ArrayList<Photo> feed;

	public SlidesFragmentAdapter(FragmentManager fm, ArrayList<Photo> feed) {
		super(fm);
		this.feed = feed;
	}

	@Override
	public Fragment getItem(int position) {
		return new SlideFragment(this.feed.get(position));
	}

	@Override
	public int getCount() {
		return this.feed.size();
	}
}