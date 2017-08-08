package com.bignerdranch.android.loc8r;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ApiInterface {

    private static final String TAG = "ApiInterface";

    public byte[] getUrlBytes(String urlSpec, boolean conProxy) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection;
        // PARA PROXY
		if(conProxy) {
			InetSocketAddress proxyInet = new InetSocketAddress("10.101.151.22",8080);
			Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyInet);
			connection = (HttpURLConnection)url.openConnection(proxy);
		}
		else
        connection = (HttpURLConnection)url.openConnection();

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
        return new String(getUrlBytes(urlSpec, false));
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

    public LocationItemDetail fetchItemDetail(String id) {

        LocationItemDetail item = new LocationItemDetail();

        try {
            String url = Uri.parse("http://10.0.2.2:3000/api/locations/" + id).toString();
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);

            JSONObject jsonBody = new JSONObject(jsonString);

            String urlMap = Uri.parse("http://maps.googleapis.com/maps/api/staticmap?center=51.455041,-0.9690884&zoom=17&size=400x350&sensor=false&markers=51.455041,-0.9690884&scale=2").toString();

            byte[] bitmapBytes = getUrlBytes(urlMap, true);
            final Bitmap original = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            original.compress(Bitmap.CompressFormat.JPEG, 50, out);
            Bitmap bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

            parseItemDetail(item, jsonBody, bitmap);

        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch item detail", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON in fetchItemDetail", je);
        }

        return item;
    }

    private String parseOpeningHours(JSONArray jsonArrayData) {
        int cantElementos = jsonArrayData.length();
        String str = new String();
        try {
            for (int i = 0; i < cantElementos; ++i) {
                JSONObject elem = jsonArrayData.getJSONObject(i);
                if(i != 0)
                    str += "\n";

                str += elem.getString("days") + ": ";

                if(!elem.getBoolean("closed"))
                    str += elem.getString("opening") + " - " + elem.getString("closing");

                else
                    str += "closed";
            }
        }
        catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON in parseOpeningHours", je);
        }
        return str;
    }

    private void setReviews(JSONArray jsonArrayData, List<Review> reviews)
            throws IOException, JSONException {

        int cantElementos = jsonArrayData.length();
        //for(int i = 0; i != cantElementos; ++i) {
        for(int i = (cantElementos - 1); 0 <= i; --i) {
            reviews.add(parseReview((jsonArrayData.getJSONObject(i))));
        }
    }

    private Review parseReview(JSONObject elem)
            throws JSONException {

        Review review = new Review();
        review.setRating(elem.getString("rating"));
        review.setAuthor(elem.getString("author"));
        review.setCreatedOn(elem.getString("createdOn"));
        review.setReviewText(elem.getString("reviewText"));

        return review;
    }

    private void parseItemDetail(LocationItemDetail item, JSONObject jsonBody, Bitmap bitmap)
            throws IOException, JSONException {
        parseItemImpl(item, jsonBody, false);
        item.setOpeningHours(parseOpeningHours(new JSONArray(jsonBody.getString("openingTimes"))));
        setReviews(new JSONArray(jsonBody.getString("reviews")), item.getReviews());
        item.setLocationMap(bitmap);
    }

    private void parseItemImpl(LocationItem item, JSONObject jsonBody, boolean tieneDistance)
            throws IOException, JSONException {

        item.setId(jsonBody.getString("_id"));
        item.setName(jsonBody.getString("name"));
        item.setAddress(jsonBody.getString("address"));

        if(tieneDistance) {
            Double distanceDouble = jsonBody.getDouble("distance");
            String distance = new DecimalFormat("#.#").format(distanceDouble) + "Km";
            item.setDistance(distance);
        }

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
    }

    private void parseItems(List<LocationItem> items, JSONObject jsonBody)
            throws IOException, JSONException {

        LocationItem item = new LocationItem();
        parseItemImpl(item, jsonBody, true);

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

    Integer postItem(String params[], boolean conProxy) {

        String locationId = params[0];
        String author = params[1];
        String rating = params[2];
        String reviewText = params[3];

        try {
            String urlStr = Uri.parse("http://10.0.2.2:3000/api/locations/" + locationId + "/reviews").toString();
            URL url = new URL(urlStr);

            HttpURLConnection conn;

            // PARA PROXY
            if(conProxy) {
                InetSocketAddress proxyInet = new InetSocketAddress("10.101.151.22",8080);
                Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyInet);
                conn = (HttpURLConnection)url.openConnection(proxy);
            }
            else
                conn = (HttpURLConnection)url.openConnection();

            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

//			String input = "{\"qty\":100,\"name\":\"iPad 4\"}";
//			JSONObject jsonInput = new JSONObject();
//			jsonInput.put("author", author);
//			jsonInput.put("rating", rating);
//			jsonInput.put("text", text);
//			String input = jsonInput.toString();


            String input = new JSONObject()
                    .put("author", author)
                    .put("rating", rating)
                    .put("reviewText", reviewText).toString();



            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();
            return 0;

        } catch (IOException e) {

            e.printStackTrace();
            return 0;

        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }

        return 1;
    }
}

