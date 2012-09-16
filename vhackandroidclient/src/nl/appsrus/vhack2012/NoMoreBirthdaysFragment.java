package nl.appsrus.vhack2012;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;


public class NoMoreBirthdaysFragment extends SherlockFragment implements OnClickListener {
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_no_more_birthdays, null);
	}
	
	@Override 
	public void onViewCreated (View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		view.findViewById(R.id.wtfAndroid).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// Share a link of this app with your friends
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_intent_text));
		sendIntent.setType("text/plain");
		startActivity(sendIntent);
	}
}
