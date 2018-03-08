package com.figengungor.popularmovies.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.figengungor.popularmovies.data.local.MovieContract.FavoriteMovieEntry;

/**
 * Created by figengungor on 3/6/2018.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_FAVORITE_MOVIE_TABLE =

                "CREATE TABLE " + FavoriteMovieEntry.TABLE_NAME + " (" +
                        FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FavoriteMovieEntry.COLUMN_POSTER_PATH + " TEXT, " +
                        FavoriteMovieEntry.COLUMN_ADULT + " INTEGER NOT NULL, " +
                        FavoriteMovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                        FavoriteMovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                        FavoriteMovieEntry.COLUMN_ID + " INTEGER NOT NULL, " +
                        FavoriteMovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                        FavoriteMovieEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT NOT NULL, " +
                        FavoriteMovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                        FavoriteMovieEntry.COLUMN_BACKDROP_PATH + " TEXT, " +
                        FavoriteMovieEntry.COLUMN_POPULARITY + " INTEGER NOT NULL, " +
                        FavoriteMovieEntry.COLUMN_VOTE_COUNT + " INTEGER NOT NULL, " +
                        FavoriteMovieEntry.COLUMN_VIDEO + " INTEGER NOT NULL, " +
                        FavoriteMovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                        " UNIQUE (" + FavoriteMovieEntry.COLUMN_ID + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_FAVORITE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteMovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
