package es.hkapps.eventus.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;

public class RequestTaskPost extends AsyncTask<String, String, String>{
	
	public interface RequestListener{
		public void onRequestCompleted(String response);
	}
	
	RequestListener listener = null;
	private List<? extends NameValuePair> nameValuePairs;
	
	public RequestTaskPost(List<NameValuePair> nameValuePairs) {
		this.nameValuePairs = nameValuePairs;
	}

	public void setRequestListener(RequestListener listener){
		this.listener = listener;
	}

    @Override
    protected String doInBackground(String... uri) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
        	
        	HttpPost post = new HttpPost(new URI(uri[0]));
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response = httpclient.execute(post);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
                //Log.d("request", uri[0]);
                Log.d("response", responseString);
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
        	Log.d("response", e.toString());
        } catch (IOException e) {
        	Log.d("response", e.toString());
        } catch (URISyntaxException e) {
        	Log.d("response", e.toString());
		}
        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(listener != null) listener.onRequestCompleted(result);
    }
}