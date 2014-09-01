package es.hkapps.eventus.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import es.hkapps.eventus.R;
import es.hkapps.eventus.api.Util;

public class Photo implements Serializable {
	private String event_key;
	private Uri file;
	private String date;
	private String username;

	public Photo(String event_key, Uri file, String date, String username) {
		this.event_key = event_key;
		this.file = file;
		this.date = date;
		this.username = username;
	}

	public Photo(Event event, String username) {
		this.date = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss")
				.format(new Date());
		this.event_key = event.getKey();

		this.file = Uri.fromFile(this.createImageFile());

		this.username = username;
	}

	public Photo(Event event, User user) {
		this.date = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss")
				.format(new Date());
		this.event_key = event.getKey();

		this.file = Uri.fromFile(this.createImageFile());

		this.username = user.getUsername();
	}

	public Uri getPhotoUri() {
		return file;
	}

	public File createImageFile() {

		String dir = Environment.getExternalStorageDirectory() + "/Eventus/";
		File newdir = new File(dir);
		newdir.mkdirs();

		// Create an image file name
		String imageFileName = event_key + "_" + username + "_"
				+ System.currentTimeMillis() + ".jpg";
		Log.d("test", dir + imageFileName);

		File image = new File(dir + imageFileName);
		return image;
	}

	public static Drawable fromUri(Activity act, Uri uri) {
		Drawable drawable = null;
		try {
			InputStream inputStream = act.getContentResolver().openInputStream(
					uri);
			drawable = Drawable.createFromStream(inputStream, uri.toString());
		} catch (FileNotFoundException e) {
			drawable = act.getResources().getDrawable(R.drawable.mexico);
		}
		return drawable;
	}

}
