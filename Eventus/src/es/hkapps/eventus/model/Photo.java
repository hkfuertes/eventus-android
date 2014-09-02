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
	private static final String FORMAT = "yyyy-MM-dd_HH:mm:ss";
	private int id;
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
	
	protected Photo(){
		
	}

	public Photo(Event event, String username) {
		this.date = new SimpleDateFormat(FORMAT)
				.format(new Date());
		this.event_key = event.getKey();

		this.file = Uri.fromFile(this.createImageFile());

		this.username = username;
	}

	public Photo(Event event, User user) {
		this.date = new SimpleDateFormat(FORMAT)
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
	
	public Drawable getDrawable(){
		return Drawable.createFromPath(file.getPath());
	}

	public String getEventKey() {
		return this.event_key;
	}

	public String getPath() {
		return this.file.getPath();
	}

	public String getDate(boolean sql) {
		return date;
	}

	public String getUsername() {
		return this.username;
	}

	public String getId() {
		// TODO Auto-generated method stub
		return id+"";
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setEventKey(String event_key) {
		this.event_key = event_key;
	}

	public void setUri(String path) {
		this.file = Uri.fromFile(new File(path));
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
