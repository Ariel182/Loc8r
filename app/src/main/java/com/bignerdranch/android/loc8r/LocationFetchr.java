package com.bignerdranch.android.loc8r;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocationFetchr {

	private static final String TAG = "LocationFetchr";

	public byte[] getUrlBytes(String urlSpec) throws IOException {
		URL url = new URL(urlSpec);
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();

		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			InputStream in = connection.getInputStream();

			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new IOException(connection.getResponseMessage() +
						": with " +
						urlSpec);
			}

			int bytesRead = 0;
			byte[] buffer = new byte[1024];
			while ((bytesRead = in.read(buffer)) > 0) {
				out.write(buffer, 0, bytesRead);
			}
			out.close();
			return out.toByteArray();
		} finally {
			connection.disconnect();
		}
	}

	public String getUrlString(String urlSpec) throws IOException {
		return new String(getUrlBytes(urlSpec));
	}

	public List<LocationItem> fetchItems() {
		List<LocationItem> items = new ArrayList<>();

		try {
			String url = Uri.parse("http://10.0.2.2:3000/api/locations/")
					.buildUpon()
					.appendQueryParameter("lng", "-0.7992599")
					.appendQueryParameter("lat", "51.378091")
					.build().toString();
			String jsonString = getUrlString(url);
			Log.i(TAG, "Received JSON: " + jsonString);

			ArrayList<JSONObject> locations = new ArrayList<>();
			JSONArray jsonarray = new JSONArray(jsonString);
			int cantLocations = jsonarray.length();

			for(int i = 0; i < cantLocations; ++i) {
				jsonarray.getJSONObject(i);
				parseItems(items, jsonarray.getJSONObject(i));
			}
		} catch (IOException ioe) {
			Log.e(TAG, "Failed to fetch items", ioe);
		} catch (JSONException je) {
			Log.e(TAG, "Failed to parse JSON", je);
		}

		return items;
	}

	private void parseItems(List<LocationItem> items, JSONObject jsonBody)
			throws IOException, JSONException {

		LocationItem item = new LocationItem();
		item.setName(jsonBody.getString("name"));
		item.setAddress(jsonBody.getString("address"));

		Double distanceDouble = jsonBody.getDouble("distance");
		String distance = new DecimalFormat("#.#").format(distanceDouble) + "Km";
		item.setDistance(distance);

		JSONArray facilities = jsonBody.getJSONArray("facilities");
		item.setFacilities(getFacilitiesString(facilities));

		Integer ratingInt = jsonBody.getInt("rating");
		String rating = "";
		if(ratingInt == 1) {
			rating = "1 star";
		} else {
			rating += ratingInt.toString() + " stars";
		}
		item.setRating(rating);

		items.add(item);
	}

	String getFacilitiesString(JSONArray facilities){
		String facilitiesString = "";
		int cantFacilities = facilities.length();
		try {
			for(int i = 0; i != cantFacilities; ++i) {
				facilitiesString += facilities.getString(i);
				facilitiesString += (i != cantFacilities - 1) ? " - " : "";
			}
		}
		catch(JSONException e)
		{
			Log.e(TAG, "getFacilitiesString", e);
		}
		return facilitiesString;
	}
}

