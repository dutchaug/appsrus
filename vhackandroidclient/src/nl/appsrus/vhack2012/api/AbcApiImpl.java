package nl.appsrus.vhack2012.api;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class AbcApiImpl implements AbcApi {

	private static final String AUTH_TOKEN = "pref.auth.token.string";
	
	private Context mContext;

	private String mAuthToken;

	public AbcApiImpl(Context c) {
		mContext = c;
		mAuthToken = PreferenceManager.getDefaultSharedPreferences(mContext).getString(AUTH_TOKEN, null);
	}

	public void getKey(String email, final ApiListener listener) {
		new ApiRequest(new ApiListener() {
			// Wrapper of the listener to process the Auth Key and store it
			public void onSuccess(JSONObject response) {
				try {
					setAuthToken (response.getString("authKey"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				listener.onSuccess(response);
			}
			public void onError(int errorCode, String errorMessage) {
				listener.onError(errorCode, errorMessage);
			}
		}).execute("getKey.php",
				"email", email);
	}

	public void getBirthdays(ApiListener listener) {
		new ApiRequest(listener).execute("getBirthdays.php");
	}

	public void sayHappyBirthday(int toUserId, ApiListener listener) {
		new ApiRequest(listener).execute("sayHappyBirthday.php",
				"targetUserId", String.valueOf(toUserId));
	}

	public void updateUserProfile(String firstName, String lastName,
			String c2dm, String tagline, int day, int month, int year,
			ApiListener listener) {
		// TODO Auto-generated method stub

	}
	
	protected void setAuthToken(String token) {
		mAuthToken = token;
		Editor e = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
		e.putString(AUTH_TOKEN, token);
		e.commit();
	}

	public String getAuthToken() {
		return mAuthToken;
	}

}
