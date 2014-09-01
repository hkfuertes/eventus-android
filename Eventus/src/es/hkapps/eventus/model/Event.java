package es.hkapps.eventus.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;
import es.hkapps.eventus.api.RequestTaskPost;
import es.hkapps.eventus.api.Util;

public class Event implements Serializable {
	private String name, place, date, type, admin;
	private String key;
	private ArrayList<String> participants;
	private ArrayList<ProgramEntry> program;

	private long id = System.currentTimeMillis();

	private String info_updated = null;
	private static final String FORMAT = "EEE MMM dd HH:mm:ss z yyyy";

	public Event(String key) {
		this.key = key;
		participants = new ArrayList<String>();
		program = new ArrayList<ProgramEntry>();
	}

	public Event(String key, String name) {
		this(key);
		this.name = name;
	}

	public Event(String key, String username, String token) {
		this(key);
		retrieveInfo(username, token);
	}

	public static ArrayList<Event> retrieveEventList(String username,
			String token) {
		ArrayList<Event> retVal = new ArrayList<Event>();

		String url = Util.server_addr + Util.app_token
				+ "/event/participation/" + username;
		// Add your data
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("username", username));
		nameValuePairs.add(new BasicNameValuePair("token", token));
		try {
			RequestTaskPost task = new RequestTaskPost(nameValuePairs);
			String response;
			response = task.execute(url).get();
			JSONObject jObj = new JSONObject(response);
			boolean success = jObj.getBoolean("success");
			if (success) {
				// Informacion del evento
				JSONObject events = jObj.getJSONObject("events");
				if (events.length() > 0) {
					Iterator<String> eventKeys = events.keys();
					while (eventKeys.hasNext()) {
						String key = eventKeys.next();
						Event evento = new Event(key);
						JSONObject event = events.getJSONObject(key);
						evento.setName(event.getString("name"));
						evento.setDate(event.getString("date"));
						evento.setPlace(event.getString("place"));
						evento.setType(event.getString("type"));
						evento.setAdmin(event.getString("name"));
						retVal.add(evento);
					}
				}
			}
			return retVal;
		} catch (Exception e) {
			Log.d("Getting list [" + username + "]", e.toString());
			Log.d("Getting list [" + username + "]", url);
			Log.d("Getting list [" + username + "]", nameValuePairs.toString());
			return null;
		}
	}

	private void setAdmin(String admin) {
		this.admin = admin;
	}

	private void setType(String type) {
		this.type = type;
	}

	private void setPlace(String place) {
		this.place = place;
	}

	private void setDate(String date) {
		this.date = date;
	}

	private void setName(String name) {
		this.name = name;
	}

	/**
	 * Recupera la informacion de un Evento contra la Base de datos Eventus
	 * 
	 * @param token
	 * @param username
	 * 
	 * @return Devuelve true o false si obtiene la informacion, y añade la
	 *         informacion.
	 */
	public boolean retrieveInfo(String username, String token) {
		Calendar now = Calendar.getInstance();
		if (info_updated != null)
			if (this.fromString(info_updated).get(Calendar.HOUR)
					- now.get(Calendar.HOUR) < 24)
				return true;

		program = new ArrayList<ProgramEntry>();
		participants = new ArrayList<String>();

		String url = Util.server_addr + Util.app_token + "/event/info/" + key;
		// Add your data
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("username", username));
		nameValuePairs.add(new BasicNameValuePair("token", token));
		try {
			RequestTaskPost task = new RequestTaskPost(nameValuePairs);
			String response;
			response = task.execute(url).get();
			JSONObject jObj = new JSONObject(response);
			boolean success = jObj.getBoolean("success");
			if (success) {
				// Informacion del evento
				JSONObject info = jObj.getJSONObject("info");
				name = info.getString("name");
				place = info.getString("place");
				date = info.getString("date");
				type = info.getString("type");
				admin = info.getString("admin");

				// Informacion de los participantes
				JSONArray p = jObj.getJSONArray("participants");
				for (int i = 0; i < p.length(); i++) {
					participants.add(p.getString(i));
				}

				// Informacion del programa
				JSONArray pr = jObj.getJSONArray("program");
				for (int i = 0; i < pr.length(); i++) {
					JSONObject e = pr.getJSONObject(i);
					program.add(new ProgramEntry(id, e.getString("time") + " "
							+ date, e.getString("act")));
				}
			}

			info_updated = this.toString(now);
			return success;
		} catch (Exception e) {
			Log.d("Getting info [" + username + "]", e.toString());
			Log.d("Getting info [" + username + "]", url);
			Log.d("Getting info [" + username + "]", nameValuePairs.toString());
			e.printStackTrace();
			return false;
		}
	}

	public boolean removeUser(String username, String token) {
		program = new ArrayList<ProgramEntry>();
		participants = new ArrayList<String>();

		String url = Util.server_addr + Util.app_token + "/event/remove/" + key
				+ "/" + username;
		// Add your data
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("username", username));
		nameValuePairs.add(new BasicNameValuePair("token", token));
		try {
			RequestTaskPost task = new RequestTaskPost(nameValuePairs);
			String response;
			response = task.execute(url).get();
			JSONObject jObj = new JSONObject(response);
			boolean success = jObj.getBoolean("success");
			return success;
		} catch (Exception e) {
			Log.d("Getting info [" + username + "]", e.toString());
			Log.d("Getting info [" + username + "]", url);
			Log.d("Getting info [" + username + "]", nameValuePairs.toString());
			e.printStackTrace();
			return false;
		}
	}

	private Calendar fromString(String date) {
		try {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(Event.FORMAT,
					Locale.ENGLISH);
			cal.setTime(sdf.parse(date));
			return cal;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

	}

	private String toString(Calendar cal) {
		SimpleDateFormat sdf = new SimpleDateFormat(Event.FORMAT);
		return sdf.format(cal.getTime());
	}

	public String getName() {
		return name;
	}

	public String getKey() {
		return key;
	}

	public String getDate() {
		return date;
	}

	public String getType() {
		return type;
	}

	public String getPlace() {
		return place;
	}

	public ArrayList<ProgramEntry> getProgram() {
		return this.program;
	}

	public ArrayList<String> getParticipants() {
		return this.participants;
	}

	@Override
	public String toString() {
		return name;
	}

}
