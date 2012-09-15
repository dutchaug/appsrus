package nl.appsrus.vhack2012;

import org.json.JSONObject;

import nl.appsrus.vhack2012.api.AbcApi;
import nl.appsrus.vhack2012.api.ApiFactory;
import nl.appsrus.vhack2012.data.UserProfile;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.viewpagerindicator.TitlePageIndicator;

public class ProfileActivity extends SherlockFragmentActivity {
	
	private static final String TAG = ProfileActivity.class.getSimpleName();
	
	private ViewPager mViewPager;
	private ProfileFragmentAdapter mAdapter;
	
	private MyProfileFragment myProfileFragment;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_profile);
        
        myProfileFragment = new MyProfileFragment();
        
        mAdapter = new ProfileFragmentAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAdapter);
        
        TitlePageIndicator titleIndicator = (TitlePageIndicator) findViewById(R.id.titles);
        titleIndicator.setViewPager(mViewPager);
        
        UserProfile myProfile = ((App) getApplication()).getMyUserProfile();
        if (myProfile == null) {
        	myProfile = new UserProfile();
        	ApiFactory.getInstance().updateUserProfile(myProfile, new AbcApi.ApiListener() {
				
				@Override
				public void onSuccess(JSONObject response) {
					Log.d(TAG, "onSuccess: " + response.toString());
				}
				
				@Override
				public void onError(int errorCode, String errorMessage) {
					Log.d(TAG, "onError: " + errorCode + " = " + errorMessage);
				}
			});
        }
    }
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		
		return super.onMenuItemSelected(featureId, item);
	}

    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
    	getSupportMenuInflater().inflate(R.menu.activity_profile, menu);
        return true;
    }
    
    private class ProfileFragmentAdapter extends FragmentPagerAdapter {
    	
    	public ProfileFragmentAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}
    	
    	@Override
    	public int getCount() {
    		return 3;
    	}
    	
    	@Override
    	public Fragment getItem(int position) {
    		switch (position) {
    		case 0: return myProfileFragment;
    		case 1: return new ReceivedWishesProfileFragment();
    		case 2: return new SentWishesProfileFragment();
    		}
    		return null;
    	}
    	
    	@Override
    	public CharSequence getPageTitle(int position) {
    		switch (position) {
    		case 0: return getText(R.string.title_profile_my);
    		case 1: return getText(R.string.title_profile_received);
    		case 2: return getText(R.string.title_profile_sent);
    		}
    		return null;
    	}
    }
    
}
