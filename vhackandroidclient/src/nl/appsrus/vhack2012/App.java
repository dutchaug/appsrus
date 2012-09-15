package nl.appsrus.vhack2012;

import java.util.regex.Pattern;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.util.Log;
import android.util.Patterns;

public class App extends Application {
	
	private static final String TAG = App.class.getSimpleName();
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		Account[] accounts = AccountManager.get(this).getAccounts();
		for (Account account : accounts) {
			Log.d(TAG, "Account: " + account.toString());
		    if (emailPattern.matcher(account.name).matches()) {
		        String possibleEmail = account.name;
		        Log.d(TAG, "FOUND E-MAIL: " + possibleEmail);
		    }
		}
	}
}
