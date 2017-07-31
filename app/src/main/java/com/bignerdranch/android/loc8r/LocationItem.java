package com.bignerdranch.android.loc8r;

import android.net.Uri;

public class LocationItem {
	private String mName;
	private String mId;
	private String mUrl;
	private String mAddress;

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getId() {
		return mId;
	}

	public String getUrl() {
		return mUrl;
	}

	public void setUrl(String url) {
		mUrl = url;
	}

	public String getAddress() {
		return mAddress;
	}

	public void setAddress(String address) {
		mAddress = address;
	}
}
