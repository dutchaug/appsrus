package nl.appsrus.vhack2012.data;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class UserProfile {
	
	public int userId;
	public String email;
	
	public String gcmToken;
	
	public String firstName;
	public String lastName;
	public String tagLine;
	
	public Date birthDate;
	
	public static UserProfile parse(JSONObject json) throws JSONException {
		UserProfile userProfile = new UserProfile();
		userProfile.firstName = json.getString("firstName");
		userProfile.lastName = json.getString("lastName");
		
		return userProfile;
	}
	
}
