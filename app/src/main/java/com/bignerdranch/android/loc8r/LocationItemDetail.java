package com.bignerdranch.android.loc8r;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class LocationItemDetail extends LocationItem {

    private String mOpeningHours;
    private List<String> mReviews= new ArrayList<>();
    private Bitmap mLocationMap;

    public Bitmap getLocationMap() {
        return mLocationMap;
    }

    public void setLocationMap(Bitmap locationMap) {
        this.mLocationMap = locationMap;
    }

    public String getOpeningHours() {
        return mOpeningHours;
    }

    public void setOpeningHours(String openingHours) {
        this.mOpeningHours = openingHours;
    }


}
