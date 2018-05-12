package com.benktesh.popularmovies.Util;

import android.util.Log;

import com.benktesh.popularmovies.Model.MovieItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class JsonUtils {

    private static final String TAG = JsonUtils.class.getSimpleName();

    public static ArrayList<MovieItem> parseMovieJson(String json) {
        try {

            MovieItem movie;
            JSONObject object = new JSONObject(json);

            JSONArray resultsArray = new JSONArray(object.optString("results",
                    "[\"\"]"));

            System.out.println(resultsArray.toString());

            ArrayList<MovieItem> items = new ArrayList<>();
            for (int i = 0; i < resultsArray.length(); i++) {
                String current = resultsArray.optString(i, "");

                JSONObject movieJson = new JSONObject(current);

                String overview = movieJson.optString("overview", "Not Available");
                String original_title = movieJson.optString("original_title",
                        "Not Available");
                String poster_path = movieJson
                        .optString("poster_path", "Not Available");
                String release_date = movieJson.optString("release_date",
                        "Not Available");
                String vote_average = movieJson.optString("vote_average", "Not Available");
                movie = new MovieItem(original_title, overview, poster_path, release_date, Double.parseDouble(vote_average));
                items.add(movie);

            }
            return items;

        } catch (Exception ex) {
            Log.e(TAG + "parseMovieJson", "Could not parse json " + json);
            return null;
        }
    }
}
