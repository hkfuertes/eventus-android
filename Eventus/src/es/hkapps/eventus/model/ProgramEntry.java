package es.hkapps.eventus.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class ProgramEntry implements Comparator<ProgramEntry>, Serializable {
	private long event_id;
	private String time, act;
	private Date date=null;
	
	public ProgramEntry(long event_id, String time, String act){
		this.event_id = event_id;
		this.setTime(time);
		this.act = act;
	}
	
	public ProgramEntry() {
		// TODO Auto-generated constructor stub
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
	
	public String getAct(){
		return act;
	}
	
	public static ArrayList<ProgramEntry> sort(ArrayList<ProgramEntry> program){
		if(program == null) return null;
		ArrayList<ProgramEntry> clone = (ArrayList<ProgramEntry>) program.clone();
		Collections.sort(clone, new ProgramEntry());
		return clone;
	}

	@Override
	public int compare(ProgramEntry lhs, ProgramEntry rhs) {
		return lhs.getDate().compareTo(rhs.getDate());
	}

}
