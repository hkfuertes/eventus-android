package es.hkapps.eventus;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.hkapps.eventus.api.RequestTask;
import es.hkapps.eventus.api.RequestTask.RequestListener;
import es.hkapps.eventus.api.User;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

public class UserActivity extends ListActivity {

	private RequestTask task;
	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			String[] values = User.getUsers();
			adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, values);
			setListAdapter(adapter);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
