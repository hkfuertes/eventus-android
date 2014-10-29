package es.hkapps.eventus.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.util.Log;
import es.hkapps.eventus.api.RequestTaskPost;
import es.hkapps.eventus.api.Util;

public class ProgramEntry implements Comparator<ProgramEntry>, Serializable {
	private String event_key;
	private String time, act;
	private Date date=null;
	
	public ProgramEntry(String event_key, String time, String act){
		this.event_key = event_key;
		this.setTime(time);
		this.act = act;
	}
	
	public static boolean updateProgram(User user, Event event, ArrayList<ProgramEntry> program){
		String url = Util.server_addr + Util.app_token + "/event/program/update";

		try {

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("username", user
					.getUsername()));
			nameValuePairs
					.add(new BasicNameValuePair("token", user.getToken()));
			
			for(int i=0; i<program.size(); i++){
				nameValuePairs.add(new BasicNameValuePair("event_program["+event.getKey()+"]["+i+"][time]", program.get(i).getTime("HH:mm")));
				nameValuePairs.add(new BasicNameValuePair("event_program["+event.getKey()+"]["+i+"][act]", program.get(i).getAct()));
			}

			RequestTaskPost task = new RequestTaskPost(nameValuePairs);
			String response;
			response = task.execute(url).get();
			JSONObject jObj = new JSONObject(response);
			boolean success = jObj.getBoolean("success");
			return success;

		} catch (Exception e) {
			e.printStackTrace();
			Log.d("Saving [" + user.getUsername() + "]", e.toString());
			return false;
		}
	}
	
	public ProgramEntry(String event_key) {
		// TODO Auto-generated constructor stub
		date = new Date();
		time ="";
		act = "";
		this.event_key = event_key;
	}

	public void setTime(String time){
		this.time = time;
		try {
			this.date = (Date) new SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.ENGLISH).parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public String getTime(){
		return time;
	}
	
	protected Date getDate(){
		return date;
	}
	
	
	public String getTime(String format){
		if(date == null) return time;
		else return new SimpleDateFormat(format,Locale.ENGLISH).format(date);
	}
	
	public int getHour(){
		String hourStr = getTime("HH");
		return Integer.parseInt(hourStr);
	}
	public int getMinutes(){
		String minStr = getTime("mm");
		return Integer.parseInt(minStr);
	}
	
	public String getAct(){
		return act;
	}
	
	public static ArrayList<ProgramEntry> sort(ArrayList<ProgramEntry> program){
		if(program == null) return null;
		Collections.sort(program, new ProgramEntry("toCompare"));
		return program;
	}

	@Override
	public int compare(ProgramEntry lhs, ProgramEntry rhs) {
		return lhs.getDate().compareTo(rhs.getDate());
	}

}
