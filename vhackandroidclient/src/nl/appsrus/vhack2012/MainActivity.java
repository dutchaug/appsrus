package nl.appsrus.vhack2012;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import nl.appsrus.vhack2012.api.AbcApi.ApiListener;
import nl.appsrus.vhack2012.api.ApiFactory;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gcm.GCMRegistrar;
import com.viewpagerindicator.LinePageIndicator;

public class MainActivity extends SherlockFragmentActivity implements ApiListener {
	
	private static final String TAG = MainActivity.class.getSimpleName();

	private static final long MIN_SPLASH_TIME = 2000;
	
	private static JSONArray sUsers;

	private class MainActivityAdapter extends FragmentPagerAdapter {

		public MainActivityAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public int getCount() {
			if (sUsers == null) {
				return 1;
			}
			return Math.min(sUsers.length() + 1, 5);
		}

		@Override
		public Fragment getItem(int position) {
			if (sUsers!= null && position < sUsers.length()){
				return MainFragment.newInstance(position);
			}
			return new NoMoreBirthdaysFragment();
		}
		
	}
	
	public static class MainFragment extends SherlockFragment implements OnClickListener, ApiListener {

		private static final String POSITION = "position";
		private static final String ACTION = "alreadySentHappyBD";

		public static MainFragment newInstance(int position) {
			MainFragment mainFragment = new MainFragment();
			Bundle args = new Bundle();
			args.putInt(POSITION, position);
			mainFragment.setArguments(args);
			return mainFragment;
		}

		private int mUserId;
		private JSONObject mUser;

		@Override
		public View onCreateView (LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
		{
			View v = inflater.inflate(R.layout.fragment_main, null);
			return v;
		}
		
		@Override 
		public void onViewCreated (View view, Bundle savedInstanceState) {
			super.onViewCreated(view, savedInstanceState);
			view.findViewById(R.id.sayHappyBirthday).setOnClickListener(this);
			try {
				mUser = sUsers.getJSONObject(getArguments().getInt(POSITION));
				String firstName = mUser.getString("firstName");
				mUserId = mUser.getInt("userId");
				
				String usesText = String.format(getString(R.string.usesPhoneFormat), firstName.toUpperCase());
				setText(R.id.firstName, firstName);
				setText(R.id.usesPhone, usesText);
				setText(R.id.lastName, mUser.getString("lastName"));
				setText(R.id.tagline, mUser.getString("tagline"));
				setText(R.id.phoneName, mUser.getString("phoneModel"));
				setText(R.id.osVersion, mUser.getString("osVersion"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			displayProperMessage();
		}
		
		private void setText(int resId, String text) {
			((TextView)getView().findViewById(resId)).setText(text);
		}

		@Override
		public void onClick(View v) {
			Button button = (Button) getView().findViewById(R.id.sayHappyBirthday);
			button.setEnabled(false);
			
			ApiFactory.getInstance().sayHappyBirthday(mUserId, this);
		}

		@Override
		public void onError(int errorCode, String errorMessage) {
			Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
		}

		@Override
		public void onSuccess(JSONObject response) {
			try {
				// Mark it as done
				mUser.put(ACTION, true);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			displayProperMessage();
		}

		private void displayProperMessage() {
			// Hide the button / Do an animation
			Button button = (Button) getView().findViewById(R.id.sayHappyBirthday);
			try {
				if (mUser.getBoolean(ACTION)) {
					button.setText(R.string.alreadyCongratulated);
					button.setEnabled(false);
					return;
				}
			} catch (JSONException e) {
				// The attribute is not there, so we should show the say HB button
			}
			button.setText(R.string.sayHappyBirthday);
			button.setEnabled(true);
		}
	}

	private MainActivityAdapter mAdapter;
	private ViewPager mViewPager;

	private long mInitialTime;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
		mAdapter = new MainActivityAdapter(getSupportFragmentManager());		
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAdapter);

        LinePageIndicator titleIndicator = (LinePageIndicator) findViewById(R.id.titles);
        titleIndicator.setViewPager(mViewPager);
    }

	@Override
	protected void onStart() {
		super.onStart();
		ApiFactory.getInstance().getBirthdays(this);
		mInitialTime = System.currentTimeMillis();
		findViewById(R.id.layout_loading).setVisibility(View.VISIBLE);
		findViewById(R.id.layout_main).setVisibility(View.GONE);
	}

	@Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
    	getSupportMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.menu_profile:
    		startActivity(new Intent(this, ProfileActivity.class));
    		return true;
    	}
    	return super.onOptionsItemSelected(item);
    }

    @Override
	public void onError(int errorCode, String errorMessage) {
		Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onSuccess(JSONObject response) {
		try {
			sUsers = response.getJSONArray("users");
			mAdapter = new MainActivityAdapter(getSupportFragmentManager());		
	        mViewPager.setAdapter(mAdapter);
			mViewPager.invalidate();
			mViewPager.setCurrentItem(0);
			// Now, check the time the splash has been displaying, if less than minimum, schedule a dismiss
			long currentTime = System.currentTimeMillis();
			if ((currentTime - mInitialTime) < MIN_SPLASH_TIME) {
				Timer t = new Timer();
				t.schedule(new TimerTask() {
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								dismissSplashScreen();
							}
						});
					}
				}, MIN_SPLASH_TIME - (currentTime - mInitialTime));
			}
			else {
				dismissSplashScreen();
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void dismissSplashScreen() {
		findViewById(R.id.layout_main).setVisibility(View.VISIBLE);
		findViewById(R.id.layout_loading).setVisibility(View.GONE);
	}
}
