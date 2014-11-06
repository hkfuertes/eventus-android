package es.hkapps.eventus.model;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import es.hkapps.eventus.api.RequestTaskPost;
import es.hkapps.eventus.api.Util;

public class Photo implements Serializable {
	private static final long serialVersionUID = -7818803384036585431L;

	private static final String FORMAT = "yyyy-MM-dd_HH:mm:ss";

	public static final String ARRAY_TAG = "array_de_photos";

	public static final String POSITION_TAG = "posicion_de_photo";
	private int id;
	private String event_key;
	private String date;
	private String username;
	private String file = null;


	private boolean downloaded = true;

	public Photo(String event_key, String file, String date, String username) {
		this.event_key = event_key;
		this.file = file;
		this.date = date;
		this.username = username;
	}

	public Photo(int id) {
		this.id = id;

		this.downloaded = false;
	}

	protected Photo() {

	}

	public boolean isDownloaded() {
		return downloaded || file!=null;
	}



	public String getSdcardFolder() {
		return Environment.getExternalStorageDirectory() + "/Eventus/";
	}

	public Photo(Event event, User user) {
		this.date = new SimpleDateFormat(FORMAT).format(new Date());
		this.event_key = event.getKey();

		this.file = this.createImageFile().getAbsolutePath();

		this.username = user.getUsername();
	}

	public Uri getUri() {
		return Uri.fromFile(new File(this.file));
	}

	public File createImageFile() {

		String dir = this.getSdcardFolder();
		File newdir = new File(dir);
		newdir.mkdirs();

		// Create an image file name
		String imageFileName = event_key + "_" + username + "_"
				+ System.currentTimeMillis() + ".jpg";
		//Log.d("test", dir + imageFileName);

		File image = new File(dir + imageFileName);
		return image;
	}


	public String getEventKey() {
		return this.event_key;
	}

	public String getPath() {
		return this.file;
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
				JSONObject jobj = jObj.getJSONObject("photo");
				this.setId(jobj.getInt("photo_id"));
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
					Photo cPhoto = new Photo(current.getInt("photo_id"));
					cPhoto.setEventKey(event_key);
					cPhoto.setUsername(current.getString("username"));
					cPhoto.setDate(current.getString("uploaded_at"));
					photos.add(cPhoto);
				}
			}
			return photos;
		} catch (Exception e) {
			// Log.d("Registering [" + username + "]", e.toString());
			e.printStackTrace();
			return null;
		}
	}

	public void setDownloaded(int num) {
		this.setDownloaded(num == 0 ? false : true);
	}

	public void setDownloaded(boolean b) {
		this.downloaded = b;
	}

	public void setPath(String path) {
		this.file = path;
	}

	public static void updateDatabase(Context cntx, User user, Event event) {
		ArrayList<Photo> pweb = Photo.getFromEvent(user, event);
		PhotoHelper pHelper = new PhotoHelper(cntx);
		ArrayList<Photo> pdb = pHelper.retrievePhotosFromEvent(event);
		// We remove the ones from the database and we get a new array to
		// insert.
		pweb.removeAll(pdb);
		pHelper.insertArray(pweb);
	}
	
	@Override
	public boolean equals(Object photo){
		if(photo != null && photo instanceof Photo){
			Photo p = (Photo) photo;
			return p.getId() == this.getId();
		}else
			return false;
	}

	public String getUrl() {
		if(this.isDownloaded()){
			return "file://" + this.getPath();
		}else{
			return Util.server_addr + Util.app_token + "/show/photo/"
					+ getEventKey() + "/" + getId();
		}
	}

	public void saveDownloadedImage(Context context, Bitmap image) {
		try {
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

			File f = this.createImageFile();
			//f.createNewFile();
			
			FileOutputStream fo = new FileOutputStream(f);
			fo.write(bytes.toByteArray());
			fo.close();
			
			this.setDownloaded(true);
			this.setPath(f.getAbsolutePath());
			
			PhotoHelper pHelper = new PhotoHelper(context);
			pHelper.save(this);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public String toString(){
		return getUrl() + " "+isDownloaded();
	}

}
