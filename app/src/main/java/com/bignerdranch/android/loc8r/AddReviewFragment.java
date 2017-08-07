package com.bignerdranch.android.loc8r;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Ariel on 06/08/2017.
 */

public class AddReviewFragment extends Fragment {

	private static final String ARG_LOCATION_ID = "location_id";
	private static final String ARG_LOCATION_NAME = "location_name";
	private String mId;
	private String mLocationName;
	private TextView mReviewHeader;

	public static AddReviewFragment newInstance(String locationId, String locationName) {
		Bundle args = new Bundle();
		args.putSerializable(ARG_LOCATION_ID, locationId);
		args.putSerializable(ARG_LOCATION_NAME, locationName);

		AddReviewFragment fragment = new AddReviewFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mId = getArguments().getString(ARG_LOCATION_ID);
		mLocationName = getArguments().getString(ARG_LOCATION_NAME);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_add_review, container, false);

		mReviewHeader = view.findViewById(R.id.review_header);
		mReviewHeader.setText("Review " + mLocationName);

		return view;
	}
}
