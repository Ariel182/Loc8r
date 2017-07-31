package com.bignerdranch.android.loc8r;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class LocationListFragment extends Fragment {

	private static final String TAG = "LocationListFragment";

	private RecyclerView mLocationRecyclerView;
	private LocationAdapter mAdapter;
	private List<LocationItem> mItems = new ArrayList<>();

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

		mLocationRecyclerView = (RecyclerView) view
				.findViewById(R.id.location_recycler_view);
		mLocationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		setupAdapter();

		return view;
	}

	private class LocationHolder extends RecyclerView.ViewHolder {
		public LocationHolder(LayoutInflater inflater, ViewGroup parent) {
			super(inflater.inflate(R.layout.list_item_location, parent, false));
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

		}

		@Override
		public int getItemCount() {
			return mLocationItems.size();
		}
	}

	private void setupAdapter() {
		if (isAdded()) {
			mLocationRecyclerView.setAdapter(new LocationAdapter(mItems));
		}
	}

	private class FetchItemsTask extends AsyncTask<Void,Void,Void> {
		@Override
		protected Void doInBackground(Void... params) {
			new LocationFetchr().fetchItems();
			return null;
		}
	}
}

