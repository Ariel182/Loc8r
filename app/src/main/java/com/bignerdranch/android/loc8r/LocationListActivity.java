package com.bignerdranch.android.loc8r;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;


public class LocationListActivity extends SingleFragmentActivity
	implements LocationListFragment.OnItemSelectedListener {

	@Override
	protected Fragment createFragment() {
		return new LocationListFragment();
	}

	public void onItemSelected(String id) {
		// The user selected the headline of an article from the HeadlinesFragment
		// Do something here to display that article

		// Create fragment and give it an argument for the selected article
        LocationFragment newFragment = LocationFragment.newInstance(id);

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		// Replace whatever is in the fragment_container view with this fragment,
		// and add the transaction to the back stack so the user can navigate back
		transaction.replace(R.id.fragment_container, newFragment);
		transaction.addToBackStack(null);

		// Commit the transaction
		transaction.commit();

	}

}

