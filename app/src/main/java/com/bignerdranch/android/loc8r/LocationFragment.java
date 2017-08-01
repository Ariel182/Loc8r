package com.bignerdranch.android.loc8r;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LocationFragment extends Fragment {

	private static final String ARG_LOCATION_ID = "location_id";

	private LocationItem mLocationItem;
	private TextView mLocationName;
	private TextView mLocationAddress;
	private TextView mLocationFacilities;
	private TextView mLocationDistance;
	private TextView mLocationRating;
	private TextView mLocationOpeningHours;

	public static LocationFragment newInstance(long crimeId) {
		Bundle args = new Bundle();
		args.putSerializable(ARG_LOCATION_ID, crimeId);

		LocationFragment fragment = new LocationFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		long id = getArguments().getLong(ARG_LOCATION_ID);
		mLocationItem = new LocationItem();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_location, container, false);

		//Capitulo 7 - Creating a UI Fragment
		//bind controles y eso
		mLocationName = view.findViewById(R.id.location_name);
		mLocationAddress = view.findViewById(R.id.location_name);
		mLocationFacilities = view.findViewById(R.id.location_name);
		mLocationDistance = view.findViewById(R.id.location_name);
		mLocationRating = view.findViewById(R.id.location_name);
		mLocationOpeningHours = view.findViewById(R.id.location_name);

		return view;
	}
}
