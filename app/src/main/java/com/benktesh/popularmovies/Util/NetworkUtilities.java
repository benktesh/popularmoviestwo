package com.benktesh.popularmovies.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Benktesh on 5/1/18.
 * Some of the resource for this file were created from Udacity provided content for the students in Google Challenge Scholar's Exercise 2.
 */

public class NetworkUtilities {
    private static final String TAG = NetworkUtilities.class.getSimpleName();

    private final static String BASE_URL = "http://image.tmdb.org/t/p/";
    private final static String BASE_URL_MOVIE = "http://api.themoviedb.org/3/movie/";
    private final static String BASE_URL_TRAILER_IMAGE = "http://img.youtube.com/vi/"; //5581bd68c3a3685df70000c6

    //The width of the poster
    private final static String WIDTH = "w185";

    private final static String API_KEY_PARAM = "?api_key=";

    /**
     * Builds the URL to fetch poster image.
     *
     * @param poster The file for poster.
     * @return The URL to use to query the GitHub.
     */
    public static String buildPosterUrl(String poster) {

        String finalPath = BASE_URL + WIDTH + "/" + poster;
        Log.d(TAG, "Building PosterURL (" + poster + ") Final: " + finalPath);

        return finalPath;

    }

    @Nullable
    private static URL getUrl(Uri builtUri) {
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /*
    This return data of a movie id. Currently we know video and review are datakeys that we know will use.
     */
    public static URL buildMovieDataUrl(String id, String dataKey, String apiKey) {
        //example rurl http://api.themoviedb.org/3/movie/19404/reviews?api_key=b22b477cd9c23c35e1ebee827d547c38
        //http://api.themoviedb.org/3/movie/19404/reviews?api_key=b22b477cd9c23c35e1ebee827d547c38
        String finalPath = BASE_URL_MOVIE + id + "/" + dataKey + API_KEY_PARAM + apiKey;

        Uri builtUri = Uri.parse(finalPath);
        return getUrl(builtUri);
    }

    public static String buildYoutubeTrailerImageUrl(String key) {
        //Example https://img.youtube.com/vi/Y9JvS2TmSvA/1.jpg
        String finalPath = BASE_URL_TRAILER_IMAGE + key + "/0.jpg"; // + GetRandomPictureId() + ".jpg";
        return finalPath;
    }

    /*
    This function generate a random integer between 0 and 3 (inclusive).
    Youtube has thumbnail images for pictures. This function generate a random integer between 0 and 3 (inclusive)
    @return String value of randomely generated number
    */
    private static String GetRandomPictureId() {
        int min = 0;
        int max = 3;

        Random rand = new Random();

        return Integer.toString(rand.nextInt((max - min) + 1) + min);
    }


    public static URL buildDataUrl(String apiKey, String sort) {
        String finalPath = BASE_URL_MOVIE + sort + API_KEY_PARAM + apiKey;
        Uri builtUri = Uri.parse(finalPath);
        return getUrl(builtUri);
    }


    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url, Context context) throws IOException {
        if (!isOnline(context)) {
            Log.e(TAG, "There is no network connection");
            return null;
        }
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(10000); //sets connection timeout to 10 seconds
        urlConnection.setReadTimeout(20000); //sets read time out to 20 seconds
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                scanner.close();
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * This method checks network connection. This code was derived from
     * https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
     **/
    private static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm != null ? cm.getActiveNetworkInfo() : null;
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
