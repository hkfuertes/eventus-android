package es.hkapps.eventus.model;

public class PhotoEntry extends WallEntry{
	String text, photo;
	
	public PhotoEntry(String text, String photo){
		this.text = text;
		this.photo = photo;
	}
	
	public PhotoEntry(){
		this.photo = "http://2.bp.blogspot.com/-8NdWPj5NIZY/TnNrI_9HWDI/AAAAAAAANLc/rvz9Yrc0eZo/s1600/mexico-escapada-paisaje-lagos-lagunas-destinos-rios-sol-horizonte.jpg";
		this.text = System.currentTimeMillis()+"_foto";
	}

	public String getPhoto() {
		return photo;
	}

	public String getText() {
		return text;
	}

}
