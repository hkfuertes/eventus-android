package es.hkapps.eventus.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class RequestTaskPost extends AsyncTask<String, String, String>{	
	public interface RequestListener{
		public void onRequestCompleted(String response);
	}
	
	RequestListener listener = null;
	private HttpEntity entity;
	private boolean debug = true;
	
	public RequestTaskPost(List<NameValuePair> nameValuePairs) {
		 Log.d("POST Data", nameValuePairs.toString());
			try {
				this.entity = new UrlEncodedFormEntity(nameValuePairs);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public RequestTaskPost(HttpEntity entity) {
		this.entity = entity;
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
            post.setEntity(entity);
            
            response = httpclient.execute(post);

            StatusLine statusLine = response.getStatusLine();
            Log.d("POST Response: ",statusLine.getStatusCode()+"");
            if(statusLine.getStatusCode() == HttpStatus.SC_OK || debug ){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
                Log.d("request", uri[0]);
                Log.d("post",entity.toString());
                Log.d("response", responseString);
            }else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (Exception e) {
        	//Log.d("response", e.toString());
        	e.printStackTrace();
        } /*catch (IOException e) {
        	Log.d("response", e.toString());
        } catch (URISyntaxException e) {
        	Log.d("response", e.toString());
		}*/
        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(listener != null) listener.onRequestCompleted(result);
    }
}