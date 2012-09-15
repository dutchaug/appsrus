package nl.appsrus.vhack2012;

import nl.appsrus.vhack2012.api.AbcApi;
import nl.appsrus.vhack2012.api.ApiFactory;
import nl.appsrus.vhack2012.data.UserProfile;

import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.util.Log;

public class App extends Application {
	
	private static final String TAG = App.class.getSimpleName();

	private UserProfile myUserProfile;
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate");
		
		ApiFactory.initialize(getBaseContext());
		final AbcApi api = ApiFactory.getInstance();

		
		if (api.getAuthToken() == null) {
			Account[] accounts = AccountManager.get(this).getAccountsByType("com.google");
			if (accounts.length > 0) {
				api.getKey(accounts[0].name, new AbcApi.ApiListener() {
					
					@Override
					public void onSuccess(JSONObject response) {
						try {
							String authKey = response.getString("authKey");
							api.setAuthToken(authKey);
						} catch (JSONException e) {
							Log.e(TAG, "Failed to parse JSON", e);
						}
					}
					
					@Override
					public void onError(int errorCode, String errorMessage) {
						Log.e(TAG, "Failed to get a key: " + errorMessage);
					}
				});
			}
		}
	}
	
	public UserProfile getMyUserProfile() {
		return myUserProfile;
	}
	
	public void setMyUserProfile(UserProfile myUserProfile) {
		this.myUserProfile = myUserProfile;
	}
}
