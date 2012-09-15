package nl.appsrus.vhack2012;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.viewpagerindicator.LinePageIndicator;

public class MainActivity extends SherlockFragmentActivity implements ApiListener {
	
	private static JSONArray mUsers;

	private class MainActivityAdapter extends FragmentPagerAdapter {

		public MainActivityAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public int getCount() {
			if (mUsers == null) {
				return 1;
			}
			return mUsers.length() + 1;
		}

		@Override
		public Fragment getItem(int position) {
			if (mUsers!= null && position < mUsers.length()){
				return MainFragment.newInstance(position);
			}
			return new NoMoreBirthdaysFragment();
		}
		
	}
	
	public static class MainFragment extends SherlockFragment {

		private static final String POSITION = "position";

		public static MainFragment newInstance(int position) {
			MainFragment mainFragment = new MainFragment();
			Bundle args = new Bundle();
			args.putInt(POSITION, position);
			mainFragment.setArguments(args);
			return mainFragment;
		}

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
			JSONObject user;
			try {
				user = mUsers.getJSONObject(getArguments().getInt(POSITION));
				String firstName = user.getString("firstName");
				
				String usesText = String.format(getString(R.string.usesPhoneFormat), firstName.toUpperCase());
				
				setText(R.id.firstName, firstName);
				setText(R.id.usesPhone, usesText);
				setText(R.id.lastName, user.getString("lastName"));
				setText(R.id.tagline, user.getString("tagline"));
				setText(R.id.phoneName, user.getString("phoneModel"));
				setText(R.id.osVersion, user.getString("osVersion"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		private void setText(int resId, String text) {
			((TextView)getView().findViewById(resId)).setText(text);
		}
	}

	private MainActivityAdapter mAdapter;
	private ViewPager mViewPager;
	
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
			mUsers = response.getJSONArray("users");
			mAdapter.notifyDataSetChanged();
			mViewPager.invalidate();
			mViewPager.setCurrentItem(0);
	        findViewById(R.id.layout_main).setVisibility(View.VISIBLE);
	        findViewById(R.id.layout_loading).setVisibility(View.GONE);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
