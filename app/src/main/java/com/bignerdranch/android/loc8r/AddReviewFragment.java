package com.bignerdranch.android.loc8r;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Ariel on 06/08/2017.
 */

public class AddReviewFragment extends Fragment {

	private static final String ARG_LOCATION_ID = "location_id";
	private static final String ARG_LOCATION_NAME = "location_name";
	private String mLocationId;
	private String mLocationName;
	private TextView mReviewHeader;
	private Button mDoAddReviewButton;
	private boolean mPostResult;

	public static AddReviewFragment newInstance(String locationId, String locationName) {
		Bundle args = new Bundle();
		args.putSerializable(ARG_LOCATION_ID, locationId);
		args.putSerializable(ARG_LOCATION_NAME, locationName);

		AddReviewFragment fragment = new AddReviewFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		mLocationId = getArguments().getString(ARG_LOCATION_ID);
		mLocationName = getArguments().getString(ARG_LOCATION_NAME);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_add_review, container, false);

		mReviewHeader = view.findViewById(R.id.review_header);
		mReviewHeader.setText("Review " + mLocationName);

		mDoAddReviewButton = view.findViewById(R.id.do_add_review_button);

		mDoAddReviewButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				TextView authorTextView = view.findViewById(R.id.edit_text_name);
				Spinner ratingSpinner = view.findViewById(R.id.spinner_rating);
				TextView reviewTextView = view.findViewById(R.id.edit_text_review);

				String author = authorTextView.getText().toString();
				String rating = ratingSpinner.getSelectedItem().toString();
				String review = authorTextView.getText().toString();

                if(author.isEmpty() || rating.isEmpty() || review.isEmpty()) {
                    Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_LONG).show();
                }
                else {
                    new PostItemTask().execute(mLocationId, author, rating, review);
                }
			}
		});

		return view;
	}

	private class PostItemTask extends AsyncTask<String,Void,Integer> {
		@Override
		protected Integer doInBackground(String... params) {
			return new ApiInterface().postItem(params, true);
		}

		@Override
		protected void onPostExecute(Integer result) {

			if(result != 0) {
				//Toast.makeText(getContext(), "Review sent", Toast.LENGTH_SHORT).show();
				getFragmentManager().popBackStackImmediate();


//				AddReviewFragment newFragment = AddReviewFragment.newInstance(mId, mLocationItemDetail.getName());
//
//				FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//
//				// Replace whatever is in the fragment_container view with this fragment,
//				// and add the transaction to the back stack so the user can navigate back
//				transaction.replace(R.id.fragment_container, newFragment);
//				transaction.addToBackStack(null);
//
//				// Commit the transaction
//				transaction.commit();
			}
			else {
				Toast.makeText(getContext(), "Couldn't send review. Please try again later.", Toast.LENGTH_LONG).show();
			}
		}
	}
}
