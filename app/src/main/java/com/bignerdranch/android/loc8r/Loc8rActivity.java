package com.bignerdranch.android.loc8r;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Loc8rActivity extends com.bignerdranch.android.loc8r.SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new LocationListFragment();
	}
}
