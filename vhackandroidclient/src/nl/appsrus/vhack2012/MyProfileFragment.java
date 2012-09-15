package nl.appsrus.vhack2012;

import org.json.JSONObject;

import nl.appsrus.vhack2012.api.AbcApi;
import nl.appsrus.vhack2012.api.ApiFactory;
import nl.appsrus.vhack2012.api.AbcApi.ApiListener;
import nl.appsrus.vhack2012.data.UserProfile;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockFragment;

public class MyProfileFragment extends SherlockFragment {
	
	private static final String TAG = MyProfileFragment.class.getSimpleName();
	
	private UserProfile profile;
	
	private EditText firstName;
	private EditText lastName;
	
	private Button saveButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_profile_edit, null);
		firstName = (EditText) view.findViewById(R.id.first_name);
		lastName = (EditText) view.findViewById(R.id.last_name);
		
		saveButton = (Button) view.findViewById(R.id.save);
		saveButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				saveProfile();
			}
		});
		
		return view;
	}
	
	public void setProfile(UserProfile profile) {
		this.profile = profile;
		
		firstName.setText(profile.firstName);
		lastName.setText(profile.lastName);
	}
	
	private void saveProfile() {
		profile.firstName = firstName.getText().toString();
		profile.lastName = lastName.getText().toString();
		
		ApiFactory.getInstance().updateUserProfile(profile, new AbcApi.ApiListener() {
			
			@Override
			public void onSuccess(JSONObject response) {
				Log.d(TAG, "onSuccess: " + response.toString());
			}
			
			@Override
			public void onError(int errorCode, String errorMessage) {
				Log.d(TAG, "onError: " + errorMessage);
			}
		});
	}
}
