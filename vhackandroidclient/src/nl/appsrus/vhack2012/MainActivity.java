package nl.appsrus.vhack2012;

import org.json.JSONException;
import org.json.JSONObject;

import nl.appsrus.vhack2012.api.AbcApi.ApiListener;
import nl.appsrus.vhack2012.api.ApiFactory;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockFragmentActivity implements ApiListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
	protected void onStart() {
		super.onStart();
		// Ask for loading the profile info
		ApiFactory.getInstance().getBirthdays(this);
	}

	@Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
    	getSupportMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.menu_profile:
    		startActivity(new Intent(this, ProfileActivity.class));
    		return true;
    	}
    	return super.onOptionsItemSelected(item);
    }

	@Override
	public void onError(int errorCode, String errorMessage) {
		Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onSuccess(JSONObject response) {
		try {
			JSONObject user = response.getJSONArray("users").getJSONObject(0);
			String firstName = user.getString("firstName");
			String lastName = user.getString("lastName");
			String tagline = user.getString("tagline");

			setText(R.id.firstName, firstName);
			setText(R.id.lastName, lastName);
			setText(R.id.tagline, tagline);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setText(int resId, String text) {
		((TextView)findViewById(resId)).setText(text);
	}
}
