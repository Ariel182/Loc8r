package com.bignerdranch.android.loc8r;

import java.util.ArrayList;
import java.util.List;

public class LocationItemDetail extends LocationItem {

    private String mOpeningHours;
    private List<String> mReviews= new ArrayList<>();

    public String getmOpeningHours() {
        return mOpeningHours;
    }

    public void setmOpeningHours(String mOpeningHours) {
        this.mOpeningHours = mOpeningHours;
    }
}
