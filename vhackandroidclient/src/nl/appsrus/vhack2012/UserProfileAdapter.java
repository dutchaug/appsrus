package nl.appsrus.vhack2012;

import java.net.URI;
import java.util.List;

import nl.appsrus.vhack2012.data.UserProfile;
import nl.appsrus.vhack2012.ui.RemoteImageView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class UserProfileAdapter extends BaseAdapter {

	private List<UserProfile> mProfiles;
	private LayoutInflater mInfalter;

	public UserProfileAdapter(Context c, List<UserProfile> profiles) {
		mInfalter = LayoutInflater.from(c);
		mProfiles = profiles;
	}

	@Override
	public int getCount() {
		return mProfiles.size();
	}

	@Override
	public Object getItem(int position) {
		return mProfiles.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mProfiles.get(position).userId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null){
			convertView = mInfalter.inflate(R.layout.profile_list_item, null);
		}
		TextView firstName = (TextView) convertView.findViewById(R.id.firstName);
		TextView lastName = (TextView) convertView.findViewById(R.id.lastName);
		RemoteImageView profile = (RemoteImageView) convertView.findViewById(R.id.profile);
		
		UserProfile user = mProfiles.get(position);
		firstName.setText(user.firstName);
		lastName.setText(user.lastName);

		URI uri = URI.create("http://www.gravatar.com/avatar/" + user.gravatarUrl);

		profile.loadURI(uri);
		
		return convertView;
	}

}
