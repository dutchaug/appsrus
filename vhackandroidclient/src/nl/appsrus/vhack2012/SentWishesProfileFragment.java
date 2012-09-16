package nl.appsrus.vhack2012;

import nl.appsrus.vhack2012.api.ApiFactory;

public class SentWishesProfileFragment extends ProfileListFragment {

	@Override
	protected void makeApiCall() {
		ApiFactory.getInstance().getSentCongrats(this);
	}
	
	@Override
	protected int getLayout() {
		return R.layout.sent_list;
	}
	
}
