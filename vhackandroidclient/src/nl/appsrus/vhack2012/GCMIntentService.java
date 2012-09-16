package nl.appsrus.vhack2012;

import nl.appsrus.vhack2012.api.ApiFactory;
import android.content.Context;
import android.content.Intent;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = GCMIntentService.class.getSimpleName();
		
	public GCMIntentService() {
	}

	@Override
	protected void onError(Context context, String regId) {

	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		((App) getApplication()).newWishReceived();
	}

	@Override
	protected void onRegistered(Context context, String regId) {
		ApiFactory.getInstance().updateGCMId(regId, null);
	}

	@Override
	protected void onUnregistered(Context context, String regId) {
		// TODO Auto-generated method stub

	}

}
