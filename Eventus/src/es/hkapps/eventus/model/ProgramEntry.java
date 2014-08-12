package es.hkapps.eventus.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProgramEntry {
	private long event_id;
	private String time, act;
	private Date date=null;
	
	public ProgramEntry(long event_id, String time, String act){
		this.event_id = event_id;
		this.setTime(time);
		this.act = act;
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
	
	
	public String getTime(String format){
		if(date == null) return time;
		else return new SimpleDateFormat(format,Locale.ENGLISH).format(date);
	}
	
	public String getAct(){
		return act;
	}

}