package es.hkapps.eventus.model;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PhotoHelper extends SQLiteOpenHelper {

	private static String DB_NAME = "photos.db";
	private static String TABLE_NAME = "fotos";

	private static String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " ("
			+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "event_key VARCHAR(100)," + "path VARCHAR(255),"
			+ "date TIMESTAMP," + "username VARCHAR(100)" + ");";

	private static String[] TABLE_FIELDS = { "id", "event_key", "path", "date",
			"username" };

	private static String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME
			+ ";";

	public PhotoHelper(Context context) {
		super(context, DB_NAME, null, 1);
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
		this.insertPhoto(photo);
	}

	public void delete(Photo photo) {
		this.removePhoto(photo);
	}

	public ArrayList<User> retrievePhotos() {
		ArrayList<User> users = new ArrayList<User>();
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor c = db.query(TABLE_NAME, TABLE_FIELDS, null, null, null, null,
				null);

		// Nos aseguramos de que existe al menos un registro
		if (c.moveToFirst()) {

			// Recorremos el cursor hasta que no haya más registros
			do {
				User user = new User();
				user.setUsername(c.getString(4));
				user.setToken(c.getString(5));
				user.setNombre(c.getString(1));
				user.setApellidos(c.getString(2));
				user.setEmail(c.getString(3));

				user.setId(c.getInt(0));
				users.add(user);
			} while (c.moveToNext());
		}

		return users;
	}

	public Photo retrievePhotoById(int id) {
		Photo photo = null;
		SQLiteDatabase db = this.getReadableDatabase();

		String[] args = new String[] { id + "" };

		Cursor c = db.query(TABLE_NAME, TABLE_FIELDS, TABLE_FIELDS[0] + "=?",
				args, null, null, null);

		// Nos aseguramos de que existe al menos un registro
		if (c.moveToFirst()) {
			photo = new Photo();
			photo.setId(c.getInt(0));
			photo.setEventKey(c.getString(1));
			photo.setUri(c.getString(2));
			photo.setDate(c.getString(4));
			photo.setUsername(c.getString(5));

			// Recorremos el cursor hasta que no haya más registros
			// do {
			// } while(c.moveToNext());
		}

		return photo;
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
				photo.setUri(c.getString(2));
				photo.setDate(c.getString(4));
				photo.setUsername(c.getString(5));
				photos.add(photo);
			} while (c.moveToNext());
		}

		return photos;
	}

	public ArrayList<Photo> retrievePhotosByUsername(String username) {
		Photo photo = null;
		ArrayList<Photo> photos = new ArrayList<Photo>();
		SQLiteDatabase db = this.getReadableDatabase();

		String[] args = new String[] { username + "" };

		Cursor c = db.query(TABLE_NAME, TABLE_FIELDS, TABLE_FIELDS[5] + "=?",
				args, null, null, null);

		// Nos aseguramos de que existe al menos un registro
		if (c.moveToFirst()) {

			// Recorremos el cursor hasta que no haya más registros
			do {
				photo = new Photo();
				photo.setId(c.getInt(0));
				photo.setEventKey(c.getString(1));
				photo.setUri(c.getString(2));
				photo.setDate(c.getString(4));
				photo.setUsername(c.getString(5));
				photos.add(photo);
			} while (c.moveToNext());
		}

		return photos;
	}

	private long insertPhoto(Photo photo) {
		ContentValues newValues = new ContentValues();
		newValues.put("event_key", photo.getEventKey());
		newValues.put("path", photo.getPath());
		newValues.put("date", photo.getDate(true));
		newValues.put("username", photo.getUsername());
		return this.getWritableDatabase().insert(TABLE_NAME, null, newValues);
	}

	private boolean removePhoto(Photo photo) {
		return this.getWritableDatabase().delete(TABLE_NAME,
				"id" + "=" + photo.getId(), null) > 0;
	}

}