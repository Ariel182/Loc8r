package com.bignerdranch.android.loc8r;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


public class LocationFragment extends Fragment {

	private static final String ARG_LOCATION_ID = "location_id";

	private String mId;
	private LocationItem mLocationItem;
	private LocationItemDetail mLocationItemDetail;
	private TextView mLocationName;
	private TextView mLocationAddress;
	private TextView mLocationFacilities;
	private TextView mLocationDistance;
	private TextView mLocationRating;
	private TextView mLocationOpeningHours;
	private ImageView mLocationMap;
	private TextView mDetailCustomerReviews;
	private Button mAddReviewButton;

	public static LocationFragment newInstance(String locationId) {
		Bundle args = new Bundle();
		args.putSerializable(ARG_LOCATION_ID, locationId);

		LocationFragment fragment = new LocationFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mId = getArguments().getString(ARG_LOCATION_ID);
		mLocationItem = new LocationItemDetail();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_location, container, false);

		mDetailCustomerReviews = view.findViewById(R.id.detail_customer_reviews);
		mAddReviewButton = view.findViewById(R.id.add_review_button);
		mDetailCustomerReviews.setVisibility(View.INVISIBLE);
		mAddReviewButton.setVisibility(View.INVISIBLE);

		new FetchItemDetailTask().execute();

		return view;
	}

	private class FetchItemDetailTask extends AsyncTask<Void,Void,LocationItemDetail> {
		@Override
		protected LocationItemDetail doInBackground(Void... params) {
			return new ApiInterface().fetchItemDetail(mId);
		}

		@Override
		protected void onPostExecute(LocationItemDetail locationItemDetail) {
			mLocationItemDetail = locationItemDetail;
			asignarView();
		}
	}

	private void asignarView(){
		mLocationName = getView().findViewById(R.id.location_name);
		mLocationAddress = getView().findViewById(R.id.location_address);
		mLocationFacilities = getView().findViewById(R.id.location_facilities);
		mLocationRating = getView().findViewById(R.id.location_rating);
		mLocationOpeningHours = getView().findViewById(R.id.location_opening_hours);
		mLocationMap = getView().findViewById(R.id.location_map);

		mLocationName.setText(mLocationItemDetail.getName());
		mLocationAddress.setText(mLocationItemDetail.getAddress());
		mLocationFacilities.setText(mLocationItemDetail.getFacilities());
		mLocationRating.setText(mLocationItemDetail.getRating());
		mLocationOpeningHours.setText(mLocationItemDetail.getOpeningHours());
		mLocationMap.setImageBitmap(mLocationItemDetail.getLocationMap());
		getView().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
		mAddReviewButton.setVisibility(View.VISIBLE);

		int cantReviews = mLocationItemDetail.getReviews().size();

		if(cantReviews > 0) {

			mDetailCustomerReviews.setVisibility(View.VISIBLE);
			displayReviews(mLocationItemDetail.getReviews(), cantReviews);
		}

		mAddReviewButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				AddReviewFragment newFragment = AddReviewFragment.newInstance(mId, mLocationItemDetail.getName());

				FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

				// Replace whatever is in the fragment_container view with this fragment,
				// and add the transaction to the back stack so the user can navigate back
				transaction.replace(R.id.fragment_container, newFragment);
				transaction.addToBackStack(null);

				// Commit the transaction
				transaction.commit();
			}
		});
	}

	private void displayReviews(List<Review> reviews, int cantReviews) {

		LinearLayout myLayout = getView().findViewById(R.id.location_layout);

		for(int i = 0; i != cantReviews; ++i) {
			TextView cabeceraTextView = new TextView(getContext());
			TextView cuerpoTextView = new TextView(getContext());
			Review review = reviews.get(i);
			String cabecera = review.getRating() + " " + review.getAuthor() + " " + review.getCreatedOn();
			String cuerpo = review.getReviewText();

			cabeceraTextView.setText(cabecera);
			cuerpoTextView.setText(cuerpo);

			myLayout.addView(cabeceraTextView);
			myLayout.addView(cuerpoTextView);
		}
	}
}
