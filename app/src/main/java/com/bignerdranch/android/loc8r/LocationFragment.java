package com.bignerdranch.android.loc8r;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Ariel on 31/07/2017.
 */

public class LocationFragment extends Fragment {

	private LocationItem mLocationItem;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLocationItem = new LocationItem();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_location, container, false);

		//Capitulo 7 - Creating a UI Fragment
		//bind controles y eso
		return view;
	}
}
