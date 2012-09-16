package nl.appsrus.vhack2012;

import nl.appsrus.vhack2012.api.AbcApi;
import nl.appsrus.vhack2012.api.ApiFactory;
import nl.appsrus.vhack2012.data.UserProfile;
import nl.appsrus.vhack2012.ui.RemoteImageView;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;

public class App extends Application {

	private static final String TAG = App.class.getSimpleName();

	private static final String PROPERY_UNREAD_WISHES = "prefs.unread.wishes";
	
	private static final int NOTIFICATION_ID = 0;

	private UserProfile myUserProfile;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate");

		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		final String regId = GCMRegistrar.getRegistrationId(this);
		if (regId.equals("")) {
			GCMRegistrar.register(this, "6462992600");
		} else {
			Log.v(TAG, "Already registered");
		}
		RemoteImageView.initialize(getBaseContext());
		ApiFactory.initialize(getBaseContext());
		final AbcApi api = ApiFactory.getInstance();

		if (api.getAuthToken() == null) {
			api.getKey(new AbcApi.ApiListener() {

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

	public void newWishReceived() {
		int unreadWishes = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt(
				PROPERY_UNREAD_WISHES, 0);
		
		unreadWishes++;
		
		Editor editor = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit();
		editor.putInt(PROPERY_UNREAD_WISHES, unreadWishes);
		editor.commit();

		Intent intent = new Intent(this, ProfileActivity.class);
		intent.putExtra(ProfileActivity.EXTRA_SELECTED_PAGE, ProfileActivity.PAGE_RECEIVED);
		
		Notification noti = new NotificationCompat.Builder(getBaseContext())
				.setContentTitle(getText(R.string.title_activity_main))
				.setContentText(Integer.toString(unreadWishes) + " " + getText(R.string.unread_birthday_wishes))
				.setSmallIcon(R.drawable.ic_stat_notification)
				.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
				.setContentIntent(PendingIntent.getActivity(getBaseContext(), 0, intent, 0))
				.build();

		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		nm.notify(NOTIFICATION_ID, noti);
	}
	
	public void clearReceivedNotification() {
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		nm.cancel(NOTIFICATION_ID);
		
		Editor editor = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit();
		editor.putInt(PROPERY_UNREAD_WISHES, 0);
		editor.commit();
	}
}
