package nl.appsrus.vhack2012.api;

import nl.appsrus.vhack2012.data.UserProfile;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gcm.GCMRegistrar;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.preference.PreferenceManager;

public class AbcApiImpl implements AbcApi {

	private static final String AUTH_TOKEN = "pref.auth.token.string";
	
	private Context mContext;

	private String mAuthToken;

	public AbcApiImpl(Context c) {
		mContext = c;
		mAuthToken = PreferenceManager.getDefaultSharedPreferences(mContext).getString(AUTH_TOKEN, null);
	}

	public void getKey(final ApiListener listener) {
		// Get the Email
		Account[] accounts = AccountManager.get(mContext).getAccountsByType("com.google");
		if (accounts.length == 0) {
			listener.onError(0, "You need a Google Account configured");
		}
		else {
			final String email = accounts[0].name;
			new ApiRequest(new ApiListener() {
				// Wrapper of the listener to process the Auth Key and store it
				public void onSuccess(JSONObject response) {
					try {
						setAuthToken (response.getString("authKey"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					if (listener != null) {
						listener.onSuccess(response);
					}
				}
				public void onError(int errorCode, String errorMessage) {
					if (listener != null){
						listener.onError(errorCode, errorMessage);
					}
				}
			}).execute("getKey.php",
					"email", email);
		}
	}

	public void getBirthdays(ApiListener listener) {
		new ApiRequest(listener).execute("getBirthdays.php");
	}

	public void sayHappyBirthday(int toUserId, ApiListener listener) {
		new ApiRequest(listener).execute("sayHappyBirthday.php",
				"targetUserId", String.valueOf(toUserId));
	}

	@Override
	public void updateUserProfile(UserProfile userProfile, ApiListener listener) {
		new ApiRequest(listener).execute("updateProfile.php",
				"firstName", userProfile.firstName,
				"lastName", userProfile.lastName,
				"c2dmToken", GCMRegistrar.getRegistrationId(mContext),
				"tagline", userProfile.tagLine,
				"day", Integer.toString(userProfile.day),
				"month", Integer.toString(userProfile.month),
				"year", Integer.toString(userProfile.year),
				"phoneModel", Build.MODEL,
				"osVersion", "Android " + Build.VERSION.RELEASE
				);
	}
	
	@Override
	public void updateGCMId(String regId, ApiListener listener) {
		new ApiRequest(listener).execute("updateProfile.php",
				"c2dm", regId
				);
	}
	
	public void setAuthToken(String token) {
		mAuthToken = token;
		Editor e = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
		e.putString(AUTH_TOKEN, token);
		e.commit();
	}

	public String getAuthToken() {
		return mAuthToken;
	}

	@Override
	public void getSentCongrats(ApiListener listener) {
		new ApiRequest(listener).execute("getSentCongrats.php");		
	}

	@Override
	public void getRcvdCongrats(ApiListener listener) {
		new ApiRequest(listener).execute("getRcvdCongrats.php");
	}

}
