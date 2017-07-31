package com.bignerdranch.android.loc8r;

import android.net.Uri;

public class LocationItem {
	private String mName;
	private String mDistance;
	private String mFacilities;
	private String mAddress;
	private String mRating;

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getAddress() {
		return mAddress;
	}

	public void setAddress(String address) {
		mAddress = address;
	}

	public String getDistance() {
		return mDistance;
	}

	public void setDistance(String distance) {
		mDistance = distance;
	}

	public String getFacilities() {
		return mFacilities;
	}

	public void setFacilities(String facilities) {
		mFacilities = facilities;
	}

	public String getRating() {
		return mRating;
	}

	public void setRating(String rating) {
		mRating = rating;
	}
}
