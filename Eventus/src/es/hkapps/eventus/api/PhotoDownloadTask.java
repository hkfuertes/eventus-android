package es.hkapps.eventus.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import es.hkapps.eventus.model.Photo;
import android.os.AsyncTask;
import android.util.Log;

public class PhotoDownloadTask extends AsyncTask<Photo, String, ArrayList<Photo>> {

	@Override
	protected ArrayList<Photo> doInBackground(Photo... param) {
		ArrayList<Photo> result = new ArrayList<Photo>();
		for(Photo photo : param){
			result.add(downloadBitmap(photo));
		}
		return result;
	}

	private Photo downloadBitmap(Photo photo) {

		String link = Util.server_addr + Util.app_token + "/show/photo/"
				+ photo.getEventKey() + "/" + photo.getOnlineId();
		Log.d("Downloading [URL]", link);
		try {
			DefaultHttpClient client = new DefaultHttpClient();
	        HttpGet get = new HttpGet(link);
	        HttpResponse getResponse = client.execute(get);;
	        
	        HttpEntity responseEntity = getResponse.getEntity();
	        BufferedHttpEntity httpEntity = null;
	        
	        httpEntity = new BufferedHttpEntity(responseEntity);
	        InputStream input = httpEntity.getContent();

			String storagePath = photo.getSdcardFolder();
			String path = storagePath + photo.getFilename();
			File pFile = new File(path);
			pFile.getParentFile().mkdirs();
			OutputStream output = new FileOutputStream(pFile);
			try {
				byte[] buffer = new byte[2048];
				int bytesRead = 0;
				while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
					output.write(buffer, 0, bytesRead);
				}
				Log.d("Downloading",path);
			} finally {
				output.close();
				photo.setDownloaded(true);
				photo.setPath(path);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return photo;
	}

}