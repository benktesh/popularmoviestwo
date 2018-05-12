package com.benktesh.popularmovies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.benktesh.popularmovies.Model.MovieItem;
import com.benktesh.popularmovies.Util.NetworkUtilities;
import com.example.benktesh.popularmovies.R;
import com.example.benktesh.popularmovies.databinding.ActivityDetailedBinding;
import com.squareup.picasso.Picasso;

public class DetailedActivity extends AppCompatActivity {

    private static final String TAG = DetailedActivity.class.getSimpleName();
    public static final String EXTRA_INDEX = "extra_index";
    ActivityDetailedBinding mBinding;

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

        populateUI(movieItem);
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
