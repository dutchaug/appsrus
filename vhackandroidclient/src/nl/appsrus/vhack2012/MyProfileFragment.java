package nl.appsrus.vhack2012;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;

import nl.appsrus.vhack2012.api.AbcApi;
import nl.appsrus.vhack2012.api.ApiFactory;
import nl.appsrus.vhack2012.api.AbcApi.ApiListener;
import nl.appsrus.vhack2012.data.UserProfile;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

public class MyProfileFragment extends SherlockFragment {
	
	private static final String TAG = MyProfileFragment.class.getSimpleName();
	
	private UserProfile profile;
	
	private EditText firstName;
	private EditText lastName;
	
	private TextView birthDay;
	
	private TextView tagline;
	
	private TextView phoneModel;
	private TextView osVersion;
	
	private Button saveButton;
	private Button dateButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_profile_edit, null);
		firstName = (EditText) view.findViewById(R.id.first_name);
		lastName = (EditText) view.findViewById(R.id.last_name);
		
		birthDay = (TextView) view.findViewById(R.id.date);
		
		tagline = (EditText) view.findViewById(R.id.tagline);
		
		phoneModel = (TextView) view.findViewById(R.id.device_name);
		osVersion = (TextView) view.findViewById(R.id.device_os);
		
		
		saveButton = (Button) view.findViewById(R.id.save);
		saveButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				saveProfile();
			}
		});
		
		dateButton = (Button) view.findViewById(R.id.change_date);
		dateButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				final DatePickerFragment datePicker = new DatePickerFragment(profile.year, profile.month, profile.day);
				datePicker.show(getFragmentManager(), "datePicker");
				datePicker.onDismiss(new DialogInterface() {
					@Override
					public void dismiss() {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void cancel() {
						
					}
				});
			}
		});
		
		return view;
	}
	
	public void setProfile(UserProfile profile) {
		this.profile = profile;
		
		firstName.setText(profile.firstName);
		lastName.setText(profile.lastName);
		tagline.setText(profile.tagLine);
		
		phoneModel.setText(profile.phoneName);
		osVersion.setText(profile.osVersion);
		
		if (profile.day == 0 || profile.month == 0) {
			birthDay.setText(R.string.birthday_not_set);
		} else {
			Calendar calendar = Calendar.getInstance();
			calendar.set(profile.year, profile.month, profile.day);
		}
	}
	
	private void saveProfile() {
		profile.firstName = firstName.getText().toString();
		profile.lastName = lastName.getText().toString();
		profile.tagLine= tagline.getText().toString();
		
		final ProgressDialog progress = new ProgressDialog(getActivity());
		progress.setMessage(getText(R.string.saving_profile));
		progress.show();
		
		ApiFactory.getInstance().updateUserProfile(profile, new AbcApi.ApiListener() {
			
			@Override
			public void onSuccess(JSONObject response) {
				Log.d(TAG, "onSuccess: " + response.toString());
				progress.dismiss();
			}
			
			@Override
			public void onError(int errorCode, String errorMessage) {
				Log.d(TAG, "onError: " + errorMessage);
				progress.dismiss();
				Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
			}
		});
	}
}
