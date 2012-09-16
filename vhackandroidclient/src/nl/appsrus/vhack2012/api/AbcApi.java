package nl.appsrus.vhack2012.api;

import nl.appsrus.vhack2012.data.UserProfile;

import org.json.JSONObject;

public interface AbcApi {
	
	public interface ApiListener {
		void onError (int errorCode, String errorMessage);
		
		void onSuccess (JSONObject response);
	}

	public void getKey (ApiListener listener);
	
	public void getBirthdays (ApiListener listener);

	public void getSentCongrats (ApiListener listener);

	public void getRcvdCongrats (ApiListener listener);

	public void sayHappyBirthday (int toUserId, ApiListener listener);
	
	public void updateUserProfile (UserProfile userProfile, ApiListener listener);

	public String getAuthToken();
	
	public void setAuthToken(String authToken);
}
