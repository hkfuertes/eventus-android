package es.hkapps.eventus.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;
import es.hkapps.eventus.R;
import es.hkapps.eventus.api.PhotoDownloadTask;
import es.hkapps.eventus.api.RequestTask;
import es.hkapps.eventus.api.RequestTaskPost;
import es.hkapps.eventus.api.Util;

public class Photo implements Serializable {
	private static final String FORMAT = "yyyy-MM-dd_HH:mm:ss";
	private int id;
	private String event_key;
	private Uri file;
	private String date;
	private String username;
	
	private String filename;

	private String online_id;
	private boolean downloaded = true;

	public Photo(String event_key, Uri file, String date, String username) {
		this.event_key = event_key;
		this.file = file;
		this.date = date;
		this.username = username;
	}

	public Photo(String online_id, String filename) {
		this.online_id = online_id;
		this.filename = filename;
		this.downloaded = false;
	}

	protected Photo() {

	}
	
	public boolean isDownloaded(){
		return downloaded;
	}
	
	public String getOnlineId(){
		return this.online_id;
	}
	
	public String getFilename(){
		return this.filename;
	}

	public static boolean downloadAndSave(Context context, Photo photo) {
		PhotoDownloadTask photosTask;
		if (photo.isDownloaded())
			return true;
		if (photo.getOnlineId() == null || photo.getFilename() == null)
			return false;
		// Metodo para descargar imagenes
		try {
			photosTask = new PhotoDownloadTask();
			Toast.makeText(context, "Downloading Image: " + photo.getFilename(), Toast.LENGTH_LONG).show();
			ArrayList<Photo> photos = photosTask.execute(photo).get();
			
			Iterator<Photo> it = photos.iterator();
			PhotoHelper pHelper = new PhotoHelper(context);
			while(it.hasNext()){
				pHelper.save(it.next());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public String getSdcardFolder(){
		return Environment.getExternalStorageDirectory()+"/Eventus/";
	}

	public Photo(Event event, String username) {
		this.date = new SimpleDateFormat(FORMAT).format(new Date());
		this.event_key = event.getKey();

		this.file = Uri.fromFile(this.createImageFile());

		this.username = username;
	}

	public Photo(Event event, User user) {
		this.date = new SimpleDateFormat(FORMAT).format(new Date());
		this.event_key = event.getKey();

		this.file = Uri.fromFile(this.createImageFile());

		this.username = user.getUsername();
	}

	public Uri getPhotoUri() {
		return file;
	}

	public File createImageFile() {

		String dir = this.getSdcardFolder();
		File newdir = new File(dir);
		newdir.mkdirs();

		// Create an image file name
		String imageFileName = event_key + "_" + username + "_"
				+ System.currentTimeMillis() + ".jpg";
		Log.d("test", dir + imageFileName);

		File image = new File(dir + imageFileName);
		return image;
	}

	public Drawable getDrawable() {
		return Drawable.createFromPath(file.getPath());
	}

	public Bitmap getBitmap() {
		File imgFile = new File(this.getPath());
		if (imgFile.exists())
			return BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		else
			return null;
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
		return id + "";
	}

	public void setId(int id) {
		this.id = id;
	}

	public Photo setEventKey(String event_key) {
		this.event_key = event_key;
		return this;
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

	/**
	 * Sube una foto al servidor.
	 * 
	 * @param token
	 * @return Devuelve true o false si esta logeado y añade el token al
	 *         usuario.
	 */
	public boolean upload(String token) {

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		builder.addTextBody("username", username);
		builder.addTextBody("token", token);
		builder.addTextBody("event_key", event_key);
		builder.addPart("photo", new FileBody(new File(getPath())));
		HttpEntity entity = builder.build();

		try {
			String url = Util.server_addr + Util.app_token + "/event/upload/"
					+ event_key;
			Log.d("Registering [URL]", url);
			RequestTaskPost task = new RequestTaskPost(entity);
			String response = task.execute(url).get();
			// Log.d("Registering [RESP]", response);
			JSONObject jObj = new JSONObject(response);
			boolean success = jObj.getBoolean("success");
			if (success) {
				return true;
			}
			return false;
		} catch (Exception e) {
			// Log.d("Registering [" + username + "]", e.toString());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Sube una foto al servidor.
	 * 
	 * @param token
	 * @return Devuelve true o false si esta logeado y añade el token al
	 *         usuario.
	 */
	public static ArrayList<Photo> getFromEvent(User user, Event event) {
		ArrayList<Photo> photos = new ArrayList<Photo>();

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		builder.addTextBody("username", user.getUsername());
		builder.addTextBody("token", user.getToken());
		builder.addTextBody("event_key", event.getKey());

		HttpEntity entity = builder.build();

		try {
			String url = Util.server_addr + Util.app_token + "/event/photos/"
					+ event.getKey();
			Log.d("Registering [URL]", url);
			RequestTaskPost task = new RequestTaskPost(entity);
			String response = task.execute(url).get();
			// Log.d("Registering [RESP]", response);
			JSONObject jObj = new JSONObject(response);
			boolean success = jObj.getBoolean("success");
			if (success) {
				String event_key = jObj.getString("event_key");
				JSONArray ps = jObj.getJSONArray("photos");
				JSONObject current;
				for (int i = 0; i < ps.length(); i++) {
					current = ps.getJSONObject(i);
					photos.add(new Photo(current.getString("photo_id"), current.getString("filename")).setEventKey(event_key));
				}

			}
			return photos;
		} catch (Exception e) {
			// Log.d("Registering [" + username + "]", e.toString());
			e.printStackTrace();
			return null;
		}
	}

	public void setDownloaded(boolean b) {
		this.downloaded = true;
	}

	public void setPath(String path) {
		this.file = Uri.fromFile(new File(path));
	}

}
