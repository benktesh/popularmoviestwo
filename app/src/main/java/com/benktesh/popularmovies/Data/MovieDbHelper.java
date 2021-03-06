package com.benktesh.popularmovies.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Benktesh on 5/26/18.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                MovieContract.MovieEntry.TABLE_NAME + " (" +
                //MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.MovieEntry.COLUMN_NAME_ID + " TEXT NOT NULL PRIMARY KEY, " +
                MovieContract.MovieEntry.COLUMN_NAME_ORIGINALTITLE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_NAME_OVERVIEW + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_NAME_POSTERPATH + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_NAME_RELEASEDATE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_NAME_VOTEAVERAGE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP " +
                ");";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);

    }
}
