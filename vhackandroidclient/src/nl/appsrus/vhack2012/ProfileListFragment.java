package nl.appsrus.vhack2012;

import java.util.ArrayList;
import java.util.List;

import nl.appsrus.vhack2012.api.AbcApi.ApiListener;
import nl.appsrus.vhack2012.data.UserProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

public abstract class ProfileListFragment extends SherlockFragment implements ApiListener {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		ViewGroup view = (ViewGroup) inflater.inflate(getLayout(), null);
		ListView list = (ListView) view.findViewById(R.id.list);
		list.setEmptyView(view.findViewById(R.id.empty_list));
		
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// Show the loading screen
		view.findViewById(R.id.layout_loading).setVisibility(View.VISIBLE);
		view.findViewById(R.id.list).setVisibility(View.GONE);
		
		
		ImageView iv = (ImageView) view.findViewById(R.id.loading_animation);
		iv.setImageResource(R.drawable.loading_animated);
		AnimationDrawable ad = (AnimationDrawable) iv.getDrawable();
		ad.start();
		// Start the api call
		makeApiCall();
	}

	protected abstract void makeApiCall();

	@Override
	public void onError(int errorCode, String errorMessage) {
		Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onSuccess(JSONObject response) {
		// Parse all the user info
		try {
			List<UserProfile> profiles = new ArrayList<UserProfile> ();
			JSONArray users = response.getJSONArray("users");
			for (int i=0; i<users.length(); i++) {
				profiles.add(UserProfile.parse(users.getJSONObject(i)));
			}
			// Create an adapter for the users
			ListView lv = (ListView) getView().findViewById(R.id.list);
			lv.setAdapter(new UserProfileAdapter (getActivity(), profiles));
			
			getView().findViewById(R.id.list).setVisibility(View.VISIBLE);
			getView().findViewById(R.id.layout_loading).setVisibility(View.GONE);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected abstract int getLayout();
}
