package com.benktesh.popularmovies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.benktesh.popularmovies.Model.MovieItem;
import com.benktesh.popularmovies.Model.MovieReview;
import com.benktesh.popularmovies.Util.JsonUtils;
import com.benktesh.popularmovies.Util.NetworkUtilities;
import com.example.benktesh.popularmovies.R;
import com.example.benktesh.popularmovies.databinding.ActivityDetailedBinding;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DetailedActivity extends AppCompatActivity  {

    private static final String TAG = DetailedActivity.class.getSimpleName();
    public static final String EXTRA_INDEX = "extra_index";
    ActivityDetailedBinding mBinding;

    private RecyclerView mMovieReviewList;
    private List<MovieReview> movieReviewItems;
    private MovieReviewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detailed);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError("Intent is null");
        }

        Bundle data = getIntent().getExtras();
        if (data == null) {
            closeOnError(getString(R.string.Error_MovieData_Not_Found));
            return;
        }
        MovieItem movieItem = data.getParcelable("movieItem");
        if (movieItem == null) {
            closeOnError(getString(R.string.Error_MovieData_Not_Found));
            return;
        }

        mMovieReviewList = findViewById(R.id.rv_movie_reviews);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        mMovieReviewList.setLayoutManager(layoutManager);


        mMovieReviewList.setHasFixedSize(false);
        mAdapter = new MovieReviewAdapter(movieReviewItems, this, mBinding);
        mMovieReviewList.setAdapter(mAdapter);
        LoadReviews(movieItem);
        populateUI(movieItem);
    }

    private void LoadReviews(MovieItem movieItem) {
       Log.d(TAG, "Getting Data from Network");

        NetworkQueryTaskParameters taskParameters = new NetworkQueryTaskParameters(movieItem.getId(),
                getText(R.string.data_key_review).toString(),
                getText(R.string.api_key).toString());
       //call review data async
       new DetailedActivity.NetworkQueryTask()
               .execute(taskParameters);

    }

    private static class NetworkQueryTaskParameters {
        String id;
        String dataKey;
        URL searchUrl;

        NetworkQueryTaskParameters(String id, String dataKey, String apiKey) {
            this.id = id;
            this.dataKey = dataKey;
            searchUrl = NetworkUtilities.buildMovieDataUrl(id, dataKey, apiKey );
        }
    }


    public class NetworkQueryTask extends AsyncTask<NetworkQueryTaskParameters, Void, String> {

        String datakey;
        @Override
        protected String doInBackground(NetworkQueryTaskParameters... params) {
            URL searchUrl = params[0].searchUrl;
            datakey = params[0].dataKey;
            String searchResults = null;
            try {
                searchResults = NetworkUtilities.getResponseFromHttpUrl(searchUrl, getApplicationContext());

            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
            return searchResults;
        }

        @Override
        protected void onPostExecute(String searchResults) {
            if (searchResults != null && !searchResults.equals("")) {
                //we are going to load either review or video based on data_key passed
                if(datakey.equals(getText(R.string.data_key_review).toString())){
                    movieReviewItems = JsonUtils.parseMovieReviewJson(searchResults);
                    mAdapter.setMovieReviewData(movieReviewItems);
                    mAdapter.notifyDataSetChanged();
                }
                else {

                }


            } else {
                Toast.makeText(getApplicationContext(), R.string.Network_Error_Prompt, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void closeOnError(String msg) {
        finish();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(MovieItem movieItem) {

        mBinding.tvOriginalTitle.setText(movieItem.getOriginalTitle());
        mBinding.tvSynopsis.setText(movieItem.getOverview());
        mBinding.tvReleaseDate.setText(movieItem.getReleaseDate());
        mBinding.rbvUserRating.setRating((float) movieItem.getVoteAverage());

        mBinding.bvAddToFavorite.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Will add to favorite db", Toast.LENGTH_LONG).show();
            }
        });

        String posterPathURL = NetworkUtilities.buildPosterUrl(movieItem.getPosterPath());
        try {
            Picasso.with(this)
                    .load(posterPathURL)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(mBinding.ivMoviePoster);
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        }

        setTitle(movieItem.getOriginalTitle());

    }

}
