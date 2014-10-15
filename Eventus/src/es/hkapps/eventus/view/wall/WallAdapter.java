package es.hkapps.eventus.view.wall;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import es.hkapps.eventus.R;
import es.hkapps.eventus.model.PhotoEntry;
import es.hkapps.eventus.model.TextEntry;
import es.hkapps.eventus.model.WallEntry;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

public class WallAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;
	private ArrayList<WallEntry> feed;

	public WallAdapter(Activity activity, ArrayList<WallEntry> feed) {

		this.feed = feed;

		layoutInflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// Set the total list item count
		return feed.size();
	}

	@Override
	public WallEntry getItem(int position) {
		return feed.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// Inflate the item layout and set the views
		View listItem = convertView;
		if (listItem == null) {
			listItem = layoutInflater.inflate(R.layout.wall_list_item, null);
		}

		FrameLayout photo = (FrameLayout) listItem
				.findViewById(R.id.wall_list_photo);
		TextView text = (TextView) listItem.findViewById(R.id.wall_list_text);

		WallEntry entry;
		if ((entry = this.getItem(position)) instanceof PhotoEntry) {
			PhotoEntry pEntry = (PhotoEntry) entry;
			//(new DownloadImageTask(photo)).execute(pEntry.getPhoto());
			photo.setBackgroundResource(R.drawable.mexico);
			text.setText(pEntry.getText());
		} else {
			TextEntry tEntry = (TextEntry) entry;
			text.setText(tEntry.getText());
			photo.setVisibility(View.GONE);
		}

		return listItem;
	}

	protected class DownloadImageTask extends AsyncTask<String, Void, Drawable> {
		FrameLayout bmImage;

		public DownloadImageTask(FrameLayout bmImage) {
			this.bmImage = bmImage;
		}

		protected Drawable doInBackground(String... urls) {
			try {
				URL url = new URL(urls[0]);
				InputStream is = (InputStream) url.getContent();
				Drawable d = Drawable.createFromStream(is, "file_"+System.currentTimeMillis());
				return d;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		protected void onPostExecute(Drawable result) {
			bmImage.setBackgroundDrawable(result);
		}
	}
}
