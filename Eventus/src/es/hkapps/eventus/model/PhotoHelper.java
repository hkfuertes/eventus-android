package es.hkapps.eventus.model;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PhotoHelper extends SQLiteOpenHelper {

	private static String DB_NAME = "photos.db";
	private static String TABLE_NAME = "fotos";

	private static String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " ("
			+ "id INTEGER PRIMARY KEY," //no queremos autoincrement, porque nos lo van a pasar de arriba.
			+ "event_key VARCHAR(100)," + "path VARCHAR(255),"
			+ "date TIMESTAMP," + "username VARCHAR(100)," +"downloaded BOOLEAN default false"+ ");";

	private static String[] TABLE_FIELDS = { "id", "event_key", "path", "date",
			"username", "downloaded"};

	private static String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME
			+ ";";
	private Context context;

	public PhotoHelper(Context context) {
		super(context, DB_NAME, null, 1);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Se ejecuta la sentencia SQL de creación de la tabla
		db.execSQL(TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int versionAnterior,
			int versionNueva) {

		// Se elimina la versión anterior de la tabla
		db.execSQL(TABLE_DROP);

		// Se crea la nueva versión de la tabla
		db.execSQL(TABLE_CREATE);
	}

	public void save(Photo photo) {
		//If update returns false, we insert it.
		if(!this.updatePhoto(photo)){
			this.insertPhoto(photo);
		}
		
		if(photo.isDownloaded()){
			//Lo guardamos en la galeria del telefono
	    	Intent scanFileIntent = new Intent(
	                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, photo.getUri());
	        context.sendBroadcast(scanFileIntent);
		}
        
	}

	public void delete(Photo photo) {
		this.removePhoto(photo);
	}

	public ArrayList<Photo> retrievePhotosFromEvent(Event event) {
		return this.retrievePhotosByEventKey(event.getKey());
	}
	
	public ArrayList<Photo> retrievePhotosByEventKey(String key) {
		Photo photo = null;
		ArrayList<Photo> photos = new ArrayList<Photo>();
		SQLiteDatabase db = this.getReadableDatabase();

		String[] args = new String[] { key + "" };

		Cursor c = db.query(TABLE_NAME, TABLE_FIELDS, TABLE_FIELDS[1] + "=?",
				args, null, null, null);

		// Nos aseguramos de que existe al menos un registro
		if (c.moveToFirst()) {

			// Recorremos el cursor hasta que no haya más registros
			do {
				photo = new Photo();
				photo.setId(c.getInt(0));
				photo.setEventKey(c.getString(1));
				photo.setPath(c.getString(2));
				photo.setDate(c.getString(3));
				photo.setUsername(c.getString(4));
				photo.setDownloaded(c.getInt(4));
				photos.add(photo);
			} while (c.moveToNext());
		}

		return photos;
	}
	
	public ArrayList<Photo> retrievePhotosByEventAndUser(User user, Event event){
		return this.retrievePhotosByEventKeyAndUsername(event.getKey(), user.getUsername());
	}

	public ArrayList<Photo> retrievePhotosByEventKeyAndUsername(String event_key, String username) {
		Photo photo = null;
		ArrayList<Photo> photos = new ArrayList<Photo>();
		SQLiteDatabase db = this.getReadableDatabase();

		String[] args = new String[] { username, event_key };
		String whereClause = TABLE_FIELDS[4]+"=? and "+TABLE_FIELDS[0]+"=?";

		Cursor c = db.query(TABLE_NAME, TABLE_FIELDS, whereClause,
				args, null, null, null);

		// Nos aseguramos de que existe al menos un registro
		if (c.moveToFirst()) {

			// Recorremos el cursor hasta que no haya más registros
			do {
				photo = new Photo();
				photo.setId(c.getInt(0));
				photo.setEventKey(c.getString(1));
				photo.setPath(c.getString(2));
				photo.setDate(c.getString(3));
				photo.setUsername(c.getString(4));
				photo.setDownloaded(c.getInt(5));
				photos.add(photo);
			} while (c.moveToNext());
		}

		return photos;
	}

	private long insertPhoto(Photo photo) {
		ContentValues newValues = new ContentValues();
		newValues.put("id", photo.getId());
		newValues.put("event_key", photo.getEventKey());
		newValues.put("path", photo.getPath());
		newValues.put("date", photo.getDate(true));
		newValues.put("username", photo.getUsername());
		newValues.put("downloaded", photo.isDownloaded() ? 1 : 0);
		long ret = this.getWritableDatabase().insert(TABLE_NAME, null, newValues);
		this.close();
		return ret;
	}

	private boolean removePhoto(Photo photo) {
		boolean ret = this.getWritableDatabase().delete(TABLE_NAME,
				"id" + "=" + photo.getId(), null) > 0;
				this.close();
				return ret;
	}

	public void insertArray(ArrayList<Photo> pweb) {
		Iterator<Photo> it = pweb.iterator();
		while (it.hasNext()) save(it.next());
	}
	
	private boolean updatePhoto(Photo photo) {
		ContentValues newValues = new ContentValues();
		newValues.put("event_key", photo.getEventKey());
		newValues.put("path", photo.getPath());
		newValues.put("date", photo.getDate(true));
		newValues.put("username", photo.getUsername());
		newValues.put("downloaded", photo.isDownloaded() ? 1 : 0);
		boolean ret = this.getWritableDatabase().update(TABLE_NAME, newValues,
				"id" + "=" + photo.getId(), null) > 0;
				this.close();
				return ret;
	}

}