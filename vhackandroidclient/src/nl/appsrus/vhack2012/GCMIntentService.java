package nl.appsrus.vhack2012;

import android.content.Context;
import android.content.Intent;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	public GCMIntentService() {
	}
	
	@Override
	protected void onError(Context context, String regId) {
		
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onRegistered(Context context, String regId) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onUnregistered(Context context, String regId) {
		// TODO Auto-generated method stub

	}

}
