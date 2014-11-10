package es.hkapps.eventus.view.events.photos.gallery;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import es.hkapps.eventus.R;
import es.hkapps.eventus.model.Photo;

public class TouchImageAdapter extends PagerAdapter {

    private ArrayList<Photo> photos;
	private DisplayImageOptions options;
    
    public TouchImageAdapter(ArrayList<Photo> photos){
    	this.photos = photos;
    	
    	options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.loading)
		.showImageForEmptyUri(R.drawable.error)
		.showImageOnFail(R.drawable.error).cacheInMemory(true)
		.cacheOnDisk(true).considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565).build();
    }
	
    @Override
    public int getCount() {
    	return photos.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        TouchImageView img = new TouchImageView(container.getContext());
        this.paint(photos.get(position), img);
        container.addView(img, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        return img;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    
    public void paint(final Photo photo, ImageView image) {
		//Toast.makeText(context, photo.getUrl(), Toast.LENGTH_LONG).show();
		Log.d("Gallery",photo.getUrl());
		try {
			ImageLoader.getInstance().displayImage(photo.getUrl(),
					image, options);
		} catch (Exception fe) {
			Log.d("Photo URL: ",photo.getUrl());
		}
	}

}