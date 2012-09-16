package nl.appsrus.vhack2012;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.viewpagerindicator.TitlePageIndicator;

public class ProfileActivity extends SherlockFragmentActivity {
	
	private static final String TAG = ProfileActivity.class.getSimpleName();
	
	public static final String EXTRA_SELECTED_PAGE = "page";
	public static final int PAGE_MY_PROFILE = 0;
	public static final int PAGE_RECEIVED = 1;
	public static final int PAGE_SENT = 2;
	
	private ViewPager mViewPager;
	private ProfileFragmentAdapter mAdapter;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_profile);
                
        mAdapter = new ProfileFragmentAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAdapter);
        
        TitlePageIndicator titleIndicator = (TitlePageIndicator) findViewById(R.id.titles);
        titleIndicator.setViewPager(mViewPager);
        
        if (getIntent().hasExtra(EXTRA_SELECTED_PAGE)) {
        	mViewPager.setCurrentItem(getIntent().getIntExtra(EXTRA_SELECTED_PAGE, 0));
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
    		case 0: return new MyProfileFragment();
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
