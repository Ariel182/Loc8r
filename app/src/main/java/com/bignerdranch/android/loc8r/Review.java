package com.bignerdranch.android.loc8r;

/**
 * Created by Ariel on 06/08/2017.
 */

public class Review {

	private String mRating;
	private String mAuthor;
	private String mReviewText;
	private String mCreatedOn;

	public String getRating() {
		return mRating;
	}

	public void setRating(String rating) {
		Integer ratingInt = Integer.parseInt(rating);

		if(ratingInt == 1)
			rating += " star";
		else
			rating += " stars";

		mRating = rating;
	}

	public String getAuthor() {
		return mAuthor;
	}

	public void setAuthor(String author) {
		mAuthor = author;
	}

	public String getReviewText() {
		return mReviewText;
	}

	public void setReviewText(String reviewText) {
		mReviewText = reviewText;
	}

	public String getCreatedOn() {
		return mCreatedOn.toString();
	}

	public void setCreatedOn(String createdOn) {
		String str = createdOn.substring(0, 16);
		mCreatedOn = str.replace("T", " ");
	}
}
