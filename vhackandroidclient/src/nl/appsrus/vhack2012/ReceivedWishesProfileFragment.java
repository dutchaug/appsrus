package nl.appsrus.vhack2012;

import nl.appsrus.vhack2012.api.ApiFactory;

public class ReceivedWishesProfileFragment extends ProfileListFragment {

	@Override
	protected void makeApiCall() {
		ApiFactory.getInstance().getRcvdCongrats(this);
		
		((App) getActivity().getApplication()).clearReceivedNotification();
	}
	
}
