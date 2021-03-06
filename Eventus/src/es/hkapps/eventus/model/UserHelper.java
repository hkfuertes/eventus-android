package es.hkapps.eventus.model;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserHelper extends SQLiteOpenHelper {

	private static String DB_NAME = "users.db";
	private static String TABLE_NAME = "usuarios";

	private static String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " ("
			+ "id INTEGER  PRIMARY KEY,"
			+ "firstname VARCHAR(100)," + "lastname VARCHAR(100),"
			+ "email VARCHAR(100)," + "username VARCHAR(100),"
			+ "token VARCHAR(255)" + ");";

	private static String[] TABLE_FIELDS = { "id", "firstname", "lastname",
			"email", "username", "token" };
	
	private static String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

	public UserHelper(Context context) {
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

	public void save(User user) {
		if (this.retrieveUserByUsername(user.getUsername()) == null) {
			// No existe el usuario en la db, con lo que lo creo
			this.insertUser(user);
		} else {
			// Existe, luego lo actualizo
			this.updateUser(user);
		}
	}

	public void delete(User user) {
		if (this.retrieveUserByUsername(user.getUsername()) != null) {
			this.removeUser(user);
		}
	}

	public User retrieveFirstUser() {
		ArrayList<User> users = this.retrieveAllUsers();
		return (users.size() > 0)?users.get(0):null;
	}
	
	public ArrayList<User> retrieveAllUsers() {
		ArrayList<User> users = new ArrayList<User>();
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor c = db.query("Usuarios", TABLE_FIELDS, null, null, null, null,
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

	public User retrieveUserById(int id) {
		User user = null;
		SQLiteDatabase db = this.getReadableDatabase();

		String[] args = new String[] { id + "" };

		Cursor c = db.query("Usuarios", TABLE_FIELDS, TABLE_FIELDS[0] + "=?",
				args, null, null, null);

		// Nos aseguramos de que existe al menos un registro
		if (c.moveToFirst()) {
			user = new User();
			user.setUsername(c.getString(4));
			user.setToken(c.getString(5));
			user.setNombre(c.getString(1));
			user.setApellidos(c.getString(2));
			user.setEmail(c.getString(3));

			user.setId(c.getInt(0));

			// Recorremos el cursor hasta que no haya más registros
			// do {
			// } while(c.moveToNext());
		}

		return user;
	}

	public User retrieveUserByUsername(String username) {
		User user = null;
		SQLiteDatabase db = this.getReadableDatabase();

		String[] args = new String[] { username };

		Cursor c = db.query("Usuarios", TABLE_FIELDS, TABLE_FIELDS[4] + "=?",
				args, null, null, null);

		// Nos aseguramos de que existe al menos un registro
		if (c.moveToFirst()) {
			user = new User();
			user.setUsername(c.getString(4));
			user.setToken(c.getString(5));
			user.setNombre(c.getString(1));
			user.setApellidos(c.getString(2));
			user.setEmail(c.getString(3));

			user.setId(c.getInt(0));

			// Recorremos el cursor hasta que no haya más registros
			// do {
			// } while(c.moveToNext());
		}

		return user;
	}

	private long insertUser(User user) {
		ContentValues newValues = new ContentValues();
		newValues.put("id", user.getId());
		newValues.put("username", user.getUsername());
		newValues.put("token", user.getToken());
		newValues.put("firstname", user.getNombre());
		newValues.put("lastname", user.getApellidos());
		newValues.put("email", user.getEmail());
		long ret = this.getWritableDatabase().insert(TABLE_NAME, null, newValues);
		this.close();
		return ret;
	}

	private boolean removeUser(User user) {
		boolean ret =  this.getWritableDatabase().delete(TABLE_NAME,
				"id" + "=" + user.getId(), null) > 0;
				this.close();
				return ret;
	}

	private boolean updateUser(User user) {
		ContentValues newValues = new ContentValues();
		newValues.put("id", user.getId());
		newValues.put("username", user.getUsername());
		newValues.put("token", user.getToken());
		newValues.put("firstname", user.getNombre());
		newValues.put("lastname", user.getApellidos());
		newValues.put("email", user.getEmail());
		boolean ret = this.getWritableDatabase().update(TABLE_NAME, newValues,
				"id" + "=" + user.getId(), null) > 0;
				this.close();
				return ret;
	}

}