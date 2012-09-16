package nl.appsrus.vhack2012;

import nl.appsrus.vhack2012.api.ApiFactory;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	public GCMIntentService() {
	}
	
	@Override
	protected void onError(Context context, String regId) {
		
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		Toast.makeText(this, "Push received!!", Toast.LENGTH_LONG).show();
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
