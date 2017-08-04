package com.bignerdranch.android.loc8r;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


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

		//Capitulo 7 - Creating a UI Fragment
		//bind controles y eso
		//mLocationName = view.findViewById(R.id.location_name);
		//mLocationAddress = view.findViewById(R.id.location_address);
		//mLocationFacilities = view.findViewById(R.id.location_facilities);
		//mLocationDistance = view.findViewById(R.id.location_distance);
		//mLocationRating = view.findViewById(R.id.location_rating);
		//mLocationOpeningHours = view.findViewById(R.id.location_opening_hours);


		new FetchItemDetailTask().execute();

		return view;
	}

	private class FetchItemDetailTask extends AsyncTask<Void,Void,LocationItemDetail> {
		@Override
		protected LocationItemDetail doInBackground(Void... params) {
			return new LocationFetchr().fetchItemDetail(mId);
		}

		@Override
		protected void onPostExecute(LocationItemDetail locationItemDetail) {
			mLocationItemDetail = locationItemDetail;
			asignarView();
		}
	}

	void asignarView(){
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
	}
}
