package com.benktesh.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.benktesh.popularmovies.Model.MovieItem;
import com.benktesh.popularmovies.Util.JsonUtils;
import com.example.benktesh.popularmovies.R;
import com.benktesh.popularmovies.Util.NetworkUtilities;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {

    private RecyclerView mMovieItemList;
    private static final String SORT_POPULAR = "popular";
    private static final String SORT_TOP_RATED = "top_rated";
    private static final String FAVORITE = "Favorite";
    private static String currentSort = SORT_POPULAR;
    private static final String MOVIE_LIST_KEY = "MOVIE_LIST_KEY";
    private static final String CURRENT_SORT_KEY = "CURRENT_SORT_KEY";
    private ArrayList<MovieItem> movieItems;
    private MovieAdapter mMovieAdapter;

    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get reference to recyclerview
        mMovieItemList = findViewById(R.id.rv_movies);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mMovieItemList.setLayoutManager(layoutManager);
        mMovieItemList.setHasFixedSize(true);


        mMovieAdapter = new MovieAdapter(movieItems, this, this);
        mMovieItemList.setAdapter(mMovieAdapter);


        //if saved instance is not null and contains key for movie list, we will restore that.
        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIE_LIST_KEY)) {
            movieItems = savedInstanceState.getParcelableArrayList(MOVIE_LIST_KEY);
            currentSort = savedInstanceState.getString(CURRENT_SORT_KEY, SORT_POPULAR);
            Log.d(TAG, "Got List from Saved Instance");
        }
        LoadView();
    }

    /**
     * This method loads the main view. It first checks if the movieItems is empty or null and in that case
     * calls GetMovieData to get a fresh copy of data. Otherwise, it just loads the view from existing list of
     * movie items.
     */
    private void LoadView() {
        if (movieItems == null || movieItems.isEmpty()) {
            if(currentSort.equals(FAVORITE)) {
                Toast.makeText(this, "Will bring favorite from db", Toast.LENGTH_LONG);
                Log.v(TAG, "Have not implemented the database fetch of popular movies");
               // return;
            }
            Log.d(TAG, "Getting Data from Network");
            new NetworkQueryTask().execute(NetworkUtilities.buildDataUrl(getText(R.string.api_key).toString(), currentSort));
        } else {
            mMovieAdapter.setMovieData(movieItems);
        }
    }

    /*
    This is an async task to fetch data from network and new data is applied to adapter.
    Also makes a long toast message when fails to retrieve information from the network
     */
    public class NetworkQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
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
                movieItems = JsonUtils.parseMovieJson(searchResults);
                mMovieAdapter.setMovieData(movieItems);

            } else {
                Toast.makeText(getApplicationContext(), R.string.Network_Error_Prompt, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Depending on the item selected, this method sets the current sort and clears the movie item list.
     *
     * @param item - the menu
     * @return boolean true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sort_most_popular && !currentSort.equals(SORT_POPULAR)) {
            ClearMovieItemList();
            currentSort = SORT_POPULAR;
            LoadView();
            return true;
        }
        if (id == R.id.action_sort_top_rated && !currentSort.equals(SORT_TOP_RATED)) {
            ClearMovieItemList();
            currentSort = SORT_TOP_RATED;
            LoadView();
            return true;
        }
        if (id == R.id.action_favorite && !currentSort.equals(FAVORITE)) {
            ClearMovieItemList();
            currentSort = FAVORITE;
            LoadView();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void ClearMovieItemList() {
        if (movieItems != null) {
            movieItems.clear();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (currentSort.equals(SORT_TOP_RATED)) {
            menu.findItem(R.id.action_sort_top_rated).setChecked(true);

        } else if (currentSort.equals(SORT_POPULAR)){
            menu.findItem(R.id.action_sort_most_popular).setChecked(true);
        }
        else {
            menu.findItem(R.id.action_favorite).setChecked(true);
        }
        return true;
    }

    @Override
    public void OnListItemClick(MovieItem movieItem) {
        Toast.makeText(this, " " + movieItem.getId(), Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(this, DetailedActivity.class);
        myIntent.putExtra(DetailedActivity.EXTRA_INDEX, 1);
        myIntent.putExtra("movieItem", movieItem);
        startActivity(myIntent);
    }

    /**
     * Upon restore the movie items and current sort will be applied.
     *
     * @param savedInstanceState - saved bundle
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        movieItems = savedInstanceState.getParcelableArrayList(MOVIE_LIST_KEY);
        currentSort = savedInstanceState.getString(CURRENT_SORT_KEY);
        Log.v(TAG, "Restored movies from bundle");
    }

    /**
     * We will store the movieItems and Current Sort into the bundle
     *
     * @param outState - instance of bundle to save
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIE_LIST_KEY, movieItems);
        outState.putString(CURRENT_SORT_KEY, currentSort);
        Log.v(TAG, "Saving the bundle");
    }
}
