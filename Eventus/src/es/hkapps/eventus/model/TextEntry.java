package es.hkapps.eventus.model;

public class TextEntry extends WallEntry {
	String text;
	
	public TextEntry(String text){
		this.text = text;
	}
	
	public TextEntry(){
		this.text = System.currentTimeMillis()+"_text";
	}

	public String getText() {
		return text;
	}

}
