package es.hkapps.eventus.view.events.photos;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import es.hkapps.eventus.R;
import es.hkapps.eventus.model.Photo;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class PhotosAdapter extends BaseAdapter {
	private ArrayList<Photo> photos;
	private Context context;
	private Photo current;
	private DisplayImageOptions options;

	public PhotosAdapter(Context context, ArrayList<Photo> photos) {
		this.photos = photos;
		this.context = context;

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
	public Photo getItem(int position) {
		return photos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView view;
		if (convertView == null) {
			view= new SquareImageView(context);
			view.setScaleType(ScaleType.CENTER);
		} else 
			view = (ImageView) convertView;

		current = getItem(position);
		this.paint(current,view);

		return view;
	}

	public void paint(final Photo photo, ImageView imageView) {
		//Toast.makeText(context, photo.getUrl(), Toast.LENGTH_LONG).show();
		Log.d("PhotosAdapter",photo.getUrl());
		try {
			ImageLoader.getInstance().displayImage(photo.getUrl(),
					imageView, options,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							// we save the bitmap to sdcard
							if (!photo.isDownloaded())
								photo.saveDownloadedImage(context, loadedImage);
						}
					});
		} catch (Exception fe) {
			Log.d("Photo URL: ",photo.getUrl());
		}
	}
}
