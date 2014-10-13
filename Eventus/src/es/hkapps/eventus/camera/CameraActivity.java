package es.hkapps.eventus.camera;

import es.hkapps.eventus.R;
import es.hkapps.eventus.R.id;
import es.hkapps.eventus.R.layout;
import es.hkapps.eventus.R.menu;
import es.hkapps.eventus.api.Util;
import es.hkapps.eventus.model.Event;
import es.hkapps.eventus.model.Photo;
import es.hkapps.eventus.model.PhotoHelper;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class CameraActivity extends ActionBarActivity {
	private static final int TAKE_PICTURE = 1; 
	Photo current;
	Event event;
	PhotoHelper pHelper;
	RelativeLayout fl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		
		pHelper = new PhotoHelper(this);
		
		Intent intent = this.getIntent();
		event = (Event) intent.getExtras().getSerializable(Util.pGeneral);
		
		fl = (RelativeLayout) findViewById(R.id.camera_background);
		
		this.takePhoto();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.camera, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	public void takePhoto() {
	    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
	    current = new Photo(this.event,Util.getUser(this));
	    intent.putExtra(MediaStore.EXTRA_OUTPUT,current.getPhotoUri());
	    startActivityForResult(intent, TAKE_PICTURE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    switch (requestCode) {
	    case TAKE_PICTURE:
	        if (resultCode == Activity.RESULT_OK) {
	            try {
	            	//Lo guardamos en la galeria del telefono
	            	Intent scanFileIntent = new Intent(
	                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, current.getPhotoUri());
	                sendBroadcast(scanFileIntent);
	                
	                //Guardamos la informacion en sqlite
	                pHelper.save(current);
	                
	                current.upload(Util.getUser(this).getToken());
	        		Toast.makeText(this, "Subiendo foto", Toast.LENGTH_LONG).show();
	                
	                //For test purposes
	                //fl.setBackgroundDrawable(current.getDrawable());
	            } catch (Exception e) {
	                Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
	                        .show();
	                Log.e("Camera", e.toString());
	            }
	        }
	    }
	    finish();
	}
	
}
