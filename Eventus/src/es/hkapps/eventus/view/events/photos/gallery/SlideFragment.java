package es.hkapps.eventus.view.events.photos.gallery;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import es.hkapps.eventus.R;
import es.hkapps.eventus.model.Photo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public final class SlideFragment extends Fragment {
    Photo photo;
	private DisplayImageOptions options;

    public SlideFragment(Photo photo) {
        this.photo = photo;
        
        options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.loading)
		.showImageForEmptyUri(R.drawable.error)
		.showImageOnFail(R.drawable.error).cacheInMemory(true)
		.cacheOnDisk(true).considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        ImageView image = new ImageView(getActivity());
        this.paint(photo, image);

        LinearLayout layout = new LinearLayout(getActivity());
        //layout.setLayoutParams(new LayoutParams(null));
        
        layout.setGravity(Gravity.CENTER);
        layout.addView(image);

        return layout;
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