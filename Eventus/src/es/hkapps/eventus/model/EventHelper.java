package es.hkapps.eventus.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventHelper extends SQLiteOpenHelper {

	private static String DB_NAME = "events.db";
	private static String TABLE_NAME = "eventos";

	public static String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " ("
			+ "key VARCHAR(100)  PRIMARY KEY,"
			+ "name VARCHAR(100)," + "place VARCHAR(100),"
			+ "date TIMESTAMP," + "type VARCHAR(125),"
			+ "admin VARCHAR(100)" + ");";

	private static String[] TABLE_FIELDS = { "key", "name", "place",
			"date", "type", "admin" };
	
	private static String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

	public EventHelper(Context context) {
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

	public void save(Event event) {
		if (this.retrieveEventByKey(event.getKey()) == null) {
			// No existe el usuario en la db, con lo que lo creo
			this.insertEvent(event);
		} else {
			// Existe, luego lo actualizo
			this.updateEvent(event);
		}
	}

	public void delete(Event event) {
		if (this.retrieveEventByKey(event.getKey()) != null) {
			this.removeEvent(event);
		}
	}

	public Event retrieveEventByKey(String key) {
		Event event = null;
		SQLiteDatabase db = this.getReadableDatabase();

		String[] args = new String[] { key };

		Cursor c = db.query(TABLE_NAME, TABLE_FIELDS, TABLE_FIELDS[0] + "=?",
				args, null, null, null);

		// Nos aseguramos de que existe al menos un registro
		if (c.moveToFirst()) {
			event = new Event(c.getString(0));
			event.setName(c.getString(1));
			event.setPlace(c.getString(2));
			event.setDate(c.getString(3));
			event.setType(c.getString(4));
			event.setAdmin(c.getString(5));


			// Recorremos el cursor hasta que no haya más registros
			// do {
			// } while(c.moveToNext());
		}

		return event;
	}

	private long insertEvent(Event event) {
		ContentValues newValues = new ContentValues();
		newValues.put("key", event.getKey());
		newValues.put("name", event.getName());
		newValues.put("place", event.getPlace());
		newValues.put("date", event.getDate());
		newValues.put("type", event.getType());
		newValues.put("admin", event.getAdmin());
		return this.getWritableDatabase().insert(TABLE_NAME, null, newValues);
	}

	private boolean removeEvent(Event event) {
		return this.getWritableDatabase().delete(TABLE_NAME,
				"key" + "=" + event.getKey(), null) > 0;
	}

	private boolean updateEvent(Event event) {
		ContentValues newValues = new ContentValues();
		newValues.put("key", event.getKey());
		newValues.put("name", event.getName());
		newValues.put("place", event.getPlace());
		newValues.put("date", event.getDate());
		newValues.put("type", event.getType());
		newValues.put("admin", event.getAdmin());
		return this.getWritableDatabase().update(TABLE_NAME, newValues,
				"key" + "=" + event.getKey(), null) > 0;
	}

	public Event retrieveEventByKey(Event selected) {
		return this.retrieveEventByKey(selected.getKey());
	}

}