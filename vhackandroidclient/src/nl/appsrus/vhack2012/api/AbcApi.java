package nl.appsrus.vhack2012.api;

import org.json.JSONObject;

public interface AbcApi {
	
	public interface ApiListener {
		void onError (int errorCode, String errorMessage);
		
		void onSuccess (JSONObject response);
	}

	public void getKey (String email, ApiListener listener);
	
	public void getBirthdays (ApiListener listener);

	public void sayHappyBirthday (int toUserId, ApiListener listener);
	
	public void updateUserProfile (String firstName, String lastName, String c2dm, String tagline, int day, int month, int year, ApiListener listener);
}
