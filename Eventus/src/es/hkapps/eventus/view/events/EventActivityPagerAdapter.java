package es.hkapps.eventus.view.events;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class EventActivityPagerAdapter extends FragmentPagerAdapter {
	Fragment[] fragment;
	String[] title;
	int[] resource;

	public EventActivityPagerAdapter(FragmentManager fm, Fragment[] fragments, String[] titles, int[] resources) {
		super(fm);
		this.fragment = fragments;
		this.title = titles;
		this.resource = resources;
	}

	public String[] getTitles() {
		return title;
	}

	@Override
	public Fragment getItem(int position) {
		return fragment[position];
	}

	@Override
	public int getCount() {
		return fragment.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return title[position];
	}
	
	public int getPageIcon(int position) {
		return resource[position];
	}
}