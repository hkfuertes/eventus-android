package es.hkapps.eventus.view.events;

import java.util.ArrayList;

import es.hkapps.eventus.model.Photo;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class PhotosAdapter extends BaseAdapter {
	private static final int TILE = 85;
	private ArrayList<Photo> photos;
	private Context context;

	public PhotosAdapter(Context context, ArrayList<Photo> photos) {
		this.photos = photos;
		this.context = context;
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
		GridView grid = (GridView)parent;
		ImageView imageView;
		if (convertView == null) {
			imageView = new SquareImageView(this.context);
			imageView.setLayoutParams(new GridView.LayoutParams(TILE, TILE));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		} else {
			imageView = (ImageView) convertView;
		}

		Bitmap bm = decodeSampledBitmapFromUri(getItem(position).getPath(),TILE, TILE);

		imageView.setImageBitmap(bm);
		return imageView;
	}

	public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth,
			int reqHeight) {
		Bitmap bm = null;
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeFile(path, options);
		return bm;
	}

	public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}
		return inSampleSize;
	}
}
