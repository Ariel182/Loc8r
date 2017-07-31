package com.bignerdranch.android.loc8r;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
			String url = Uri.parse("http://192.168.1.34:3000/api/locations/")
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

//		JSONObject photosJsonObject = jsonBody.getJSONObject("photos");
//		JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");
//
//		for (int i = 0; i < photoJsonArray.length(); i++) {
//			JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);

			LocationItem item = new LocationItem();
			item.setName(jsonBody.getString("name"));
			item.setAddress(jsonBody.getString("address"));

//			if (!photoJsonObject.has("url_s")) {
//				continue;
//			}

//			item.setUrl(photoJsonObject.getString("url_s"));
			items.add(item);
//		}
	}
}

