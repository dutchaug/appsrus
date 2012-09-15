package nl.appsrus.vhack2012.api;

public class AbcApiImpl implements AbcApi {

	public void getKey(String email, ApiListener listener) {
		new ApiRequest(listener).execute("getKey.php",
				"email", email);
	}

	public void getBirthdays(ApiListener listener) {
		// TODO Auto-generated method stub

	}

	public void sayHappyBirthday(int toUserId, ApiListener listener) {
		// TODO Auto-generated method stub

	}

	public void updateUserProfile(String firstName, String lastName,
			String c2dm, String tagline, int day, int month, int year,
			ApiListener listener) {
		// TODO Auto-generated method stub

	}

	public String getAuthToken() {
		// TODO Auto-generated method stub
		return null;
	}

}
