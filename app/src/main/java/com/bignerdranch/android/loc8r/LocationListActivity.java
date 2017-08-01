package com.bignerdranch.android.loc8r;
import android.support.v4.app.Fragment;


public class LocationListActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new LocationListFragment();
	}
}

