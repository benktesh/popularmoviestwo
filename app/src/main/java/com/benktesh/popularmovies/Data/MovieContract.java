package com.benktesh.popularmovies.Data;

import android.provider.BaseColumns;

/**
 * Created by Benktesh on 5/26/18.
 */

public final class MovieContract {

    private MovieContract(){}

    /* Define table and content */
    public static class MovieEntry implements BaseColumns {

        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_TIMESTAMP ="timestamp";
    }
}
