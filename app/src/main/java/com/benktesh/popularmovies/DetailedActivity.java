package com.benktesh.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.benktesh.popularmovies.Data.MovieContract;
import com.benktesh.popularmovies.Data.MovieDbHelper;
import com.benktesh.popularmovies.Model.MovieItem;
import com.benktesh.popularmovies.Model.MovieReview;
import com.benktesh.popularmovies.Model.MovieVideo;
import com.benktesh.popularmovies.Util.JsonUtils;
import com.benktesh.popularmovies.Util.NetworkUtilities;
import com.example.benktesh.popularmovies.R;
import com.example.benktesh.popularmovies.databinding.ActivityDetailedBinding;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;

import static com.benktesh.popularmovies.Util.NetworkUtilities.buildVideoUrl;

public class DetailedActivity extends AppCompatActivity implements MovieVideoAdapter.ListItemClickListener {

    private static final String TAG = DetailedActivity.class.getSimpleName();
    public static final String EXTRA_INDEX = "extra_index";
    ActivityDetailedBinding mBinding;

    private RecyclerView mMovieReviewList;
    private List<MovieReview> movieReviewItems;
    private MovieReviewAdapter movieReviewAdapter;

    private RecyclerView mMovieVideoList;
    private List<MovieVideo> movieVideoItems;
    private MovieVideoAdapter movieVideoAdapter;
    private boolean isFavorite;


    private SQLiteDatabase mDb;

    MovieItem movieItem;

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
        movieItem = data.getParcelable("movieItem");
        if (movieItem == null) {
            closeOnError(getString(R.string.Error_MovieData_Not_Found));
            return;
        }

        mMovieReviewList = findViewById(R.id.rv_movie_reviews);
        LinearLayoutManager layoutManagerReview = new LinearLayoutManager(this);
        mMovieReviewList.setLayoutManager(layoutManagerReview);
        mMovieReviewList.setHasFixedSize(false);
        movieReviewAdapter = new MovieReviewAdapter(movieReviewItems, this, mBinding);
        mMovieReviewList.setAdapter(movieReviewAdapter);

        mMovieVideoList = findViewById(R.id.rv_movie_trailers);
        LinearLayoutManager layoutManagerVideo = new LinearLayoutManager(this);
        mMovieVideoList.setLayoutManager(layoutManagerVideo);
        mMovieVideoList.setHasFixedSize(false);
        movieVideoAdapter = new MovieVideoAdapter(movieVideoItems, this, this);
        mMovieVideoList.setAdapter(movieVideoAdapter);

        MovieDbHelper movieDbHelper = new MovieDbHelper(this);
        mDb = movieDbHelper.getWritableDatabase();


        LoadAdditionalData();
        populateUI();
    }

    private void LoadAdditionalData() {
        Log.v(TAG, "Getting Review from Network");

        new DetailedActivity.NetworkQueryTask()
                .execute(new NetworkQueryTaskParameters(movieItem.getId(),
                        getText(R.string.data_key_review).toString(),
                        getText(R.string.api_key).toString()));
        Log.v(TAG, "Getting Trailer Video Data from Network");
        new DetailedActivity.NetworkQueryTask()
                .execute(new NetworkQueryTaskParameters(movieItem.getId(),
                        getText(R.string.data_key_video).toString(),
                        getText(R.string.api_key).toString()));
    }

    @Override
    public void OnListItemClick(MovieVideo movieVideo) {
        this.startActivity(new Intent(Intent.ACTION_VIEW, NetworkUtilities.buildVideoUrl(movieVideo.getKey())));
    }

    private static class NetworkQueryTaskParameters {
        String id;
        String dataKey;
        URL searchUrl;

        NetworkQueryTaskParameters(String id, String dataKey, String apiKey) {
            this.id = id;
            this.dataKey = dataKey;
            searchUrl = NetworkUtilities.buildMovieDataUrl(id, dataKey, apiKey);
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
                Log.d(TAG, "Network data retrieved for " + datakey);
                //we are going to load either review or video based on data_key passed
                if (datakey.equals(getText(R.string.data_key_review).toString())) {
                    movieReviewItems = JsonUtils.parseMovieReviewJson(searchResults);
                    movieReviewAdapter.setMovieReviewData(movieReviewItems);
                } else { //must equal to video
                    movieVideoItems = JsonUtils.parseMovieVideoJson(searchResults);
                    movieVideoAdapter.setMovieReviewData(movieVideoItems);
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

    private void addToFavorite() {
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry.COLUMN_NAME_ID, movieItem.getId());
        cv.put(MovieContract.MovieEntry.COLUMN_NAME_ORIGINALTITLE, movieItem.getOriginalTitle());
        cv.put(MovieContract.MovieEntry.COLUMN_NAME_OVERVIEW, movieItem.getOverview());
        cv.put(MovieContract.MovieEntry.COLUMN_NAME_POSTERPATH, movieItem.getPosterPath());
        cv.put(MovieContract.MovieEntry.COLUMN_NAME_RELEASEDATE, movieItem.getReleaseDate());
        cv.put(MovieContract.MovieEntry.COLUMN_NAME_VOTEAVERAGE, movieItem.getVoteAverage());
        Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, cv);
        Log.d(TAG, "Added Row: " + uri);

    }

    private void removeFromFavorite() {

        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(movieItem.getId().toString()).build();
        int rowCount = getContentResolver().delete(uri, null, null);
        Log.d(TAG, "Removed Rows: " + rowCount);
    }

    private void populateUI() {

        mBinding.tvOriginalTitle.setText(movieItem.getOriginalTitle());
        mBinding.tvSynopsis.setText(movieItem.getOverview());
        mBinding.tvReleaseDate.setText(movieItem.getReleaseDate());
        mBinding.rbvUserRating.setRating((float) movieItem.getVoteAverage());

        mBinding.bvAddToFavorite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(movieItem.isFavorite()) {
                    movieItem.setFavorite(false);
                    removeFromFavorite();
                }
                else {
                    movieItem.setFavorite(true);
                    mBinding.bvAddToFavorite.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.FavoriteColor, null));
                    addToFavorite();
                }
                SetBackGroundColorOfFavorite();

            }
        });

        SetBackGroundColorOfFavorite();

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

    private void SetBackGroundColorOfFavorite() {
        if (movieItem.isFavorite()) {
            mBinding.bvAddToFavorite.setBackgroundColor(ResourcesCompat.getColor(getResources(),
                    R.color.FavoriteColor, null));
        } else {
            mBinding.bvAddToFavorite.setBackgroundColor(ResourcesCompat.getColor(getResources(),
                    R.color.NotFavoriteColor, null));
        }
    }

}
