package com.bignerdranch.android.loc8r;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LocationListFragment extends Fragment {

	OnItemSelectedListener mCallback;

	private static final String TAG = "LocationListFragment";

	private RecyclerView mLocationRecyclerView;
	private LocationAdapter mAdapter;
	private List<LocationItem> mItems = new ArrayList<>();

	// Container Activity must implement this interface
	public interface OnItemSelectedListener {
		void onItemSelected(String id);
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);

		Activity activity = (Activity)context;

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (OnItemSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnItemSelectedListener");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		new FetchItemsTask().execute();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_location_list, container, false);

		mLocationRecyclerView = view
				.findViewById(R.id.location_recycler_view);
		mLocationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		setupAdapter();

		return view;
	}

	private class LocationHolder extends RecyclerView.ViewHolder
		implements View.OnClickListener {

		private LocationItem mLocationItem;

		private TextView mLocationNameTextView;
		private TextView mLocationAddressTextView;
		private TextView mLocationFacilitiesTextView;
		private TextView mLocationDistanceTextView;
		private TextView mLocationRatingTextView;

		public LocationHolder(LayoutInflater inflater, ViewGroup parent) {
			super(inflater.inflate(R.layout.list_item_location, parent, false));

			itemView.setOnClickListener(this);

			mLocationNameTextView = itemView.findViewById(R.id.location_name);
			mLocationAddressTextView = itemView.findViewById(R.id.location_address);
			mLocationFacilitiesTextView = itemView.findViewById(R.id.location_facilities);
			mLocationDistanceTextView = itemView.findViewById(R.id.location_distance);
			mLocationRatingTextView = itemView.findViewById(R.id.location_rating);
		}

		public void bind(LocationItem locationItem) {
			mLocationItem = locationItem;
			mLocationNameTextView.setText(locationItem.getName());
			mLocationAddressTextView.setText(locationItem.getAddress());
			mLocationFacilitiesTextView.setText(locationItem.getFacilities());
			mLocationDistanceTextView.setText(locationItem.getDistance());
			mLocationRatingTextView.setText(locationItem.getRating());
		}

		@Override
		public void onClick(View view) {
			//Intent intent = new Intent(getActivity(), LocationActivity.class);
			//startActivity(intent);
			mCallback.onItemSelected(mLocationItem.getId());
		}
	}

	private class LocationAdapter extends RecyclerView.Adapter<LocationHolder> {

		private List<LocationItem> mLocationItems;

		public LocationAdapter(List<LocationItem> locationItems) {
			mLocationItems = locationItems;
		}

		@Override
		public LocationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

			return new LocationHolder(layoutInflater, parent);
		}

		@Override
		public void onBindViewHolder(LocationHolder holder, int position) {
			LocationItem locationItem = mLocationItems.get(position);
			holder.bind(locationItem);
		}

		@Override
		public int getItemCount() {
			return mLocationItems.size();
		}

		public void setLocations(List<LocationItem> locationItems) {
			mLocationItems = locationItems;
		}
	}

	private void setupAdapter() {
		if (isAdded()) {
			mLocationRecyclerView.setAdapter(new LocationAdapter(mItems));
		}
	}

	private class FetchItemsTask extends AsyncTask<Void,Void,List<LocationItem>> {
		@Override
		protected List<LocationItem> doInBackground(Void... params) {
			return new LocationFetchr().fetchItems();
		}

		@Override
		protected void onPostExecute(List<LocationItem> items) {
			mItems = items;
			setupAdapter();
		}
	}
}

