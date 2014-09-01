package es.hkapps.eventus.model;

import java.util.ArrayList;

public class WallEntry {
	
	public static ArrayList<WallEntry> retrieveWallEntries(String username, String token, String event_key){
		ArrayList<WallEntry> retVal = new ArrayList<WallEntry>();
		for(int i=0; i<20; i++){
			if(Math.random() < 0.5) retVal.add(new TextEntry());
			else retVal.add(new PhotoEntry());
		}
		return retVal;
	}

}
