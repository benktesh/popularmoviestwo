package com.benktesh.popularmovies.Util;

import android.util.Log;

import com.benktesh.popularmovies.Model.MovieItem;
import com.benktesh.popularmovies.Model.MovieReview;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


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

                String id = movieJson.optString("id", "0");
                String overview = movieJson.optString("overview", "Not Available");
                String original_title = movieJson.optString("original_title",
                        "Not Available");
                String poster_path = movieJson
                        .optString("poster_path", "Not Available");
                String release_date = movieJson.optString("release_date",
                        "Not Available");
                String vote_average = movieJson.optString("vote_average", "Not Available");
                movie = new MovieItem(id, original_title, overview, poster_path, release_date, Double.parseDouble(vote_average));
                items.add(movie);

            }
            return items;

        } catch (Exception ex) {
            Log.e(TAG + "parseMovieJson", "Could not parse json " + json);
            return null;
        }
    }

    public static List<MovieReview> parseMovieReviewJson(String json) {
        try {
            MovieReview review;
            JSONObject object = new JSONObject(json);

            JSONArray resultsArray = new JSONArray(object.optString("results",
                    "[\"\"]"));

            //    {"id":19404,"page":1,"results":[{"author":"MohamedElsharkawy","content":"The Dilwale Dulhania Le Jayenge is a film considered by most to be one of the greatest ever made. From The American Film Institute to as voted by users on the Internet Movie Database (IMDB) it is consider to be one of the best.","id":"59eb3d42925141565100e901","url":"https://www.themoviedb.org/review/59eb3d42925141565100e901"}],"total_pages":1,"total_results":1}
            System.out.println(resultsArray.toString());

            ArrayList<MovieReview> items = new ArrayList<>();
            for (int i = 0; i < resultsArray.length(); i++) {
                String current = resultsArray.optString(i, "");
                JSONObject reviewJson = new JSONObject(current);

                String content = reviewJson.optString("content", "Not Available");
                String author = reviewJson.optString("author", "Not Available");

                review = new MovieReview(author, content);
                items.add(review);
            }
            return items;

        } catch (Exception ex) {
            Log.e(TAG + "parseMovieJson", "Could not parse json " + json);
            return null;
        }

    }
}
