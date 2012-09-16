package nl.appsrus.vhack2012.data;

import org.json.JSONException;
import org.json.JSONObject;

public class UserProfile {
	
	public int userId;
	public String email;
	
	public String gcmToken;
	
	public String firstName;
	public String lastName;
	public String tagLine;
	
	public int day;
	public int month;
	public int year;
	
	public String phoneName;
	public String osVersion;
	
	public static UserProfile parse(JSONObject json) throws JSONException {
		UserProfile userProfile = new UserProfile();
		userProfile.userId = json.getInt("userId");
		userProfile.firstName = json.getString("firstName");
		userProfile.lastName = json.getString("lastName");
		userProfile.tagLine = json.getString("tagline");
		userProfile.day = json.getInt("day");
		userProfile.month = json.getInt("month");
		userProfile.year = json.getInt("year");
		userProfile.phoneName = json.getString("phoneModel");
		userProfile.osVersion = json.getString("osVersion");
		return userProfile;
	}
}
