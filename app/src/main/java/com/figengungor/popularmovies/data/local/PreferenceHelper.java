package com.figengungor.popularmovies.data.local;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.figengungor.popularmovies.Constants;

/**
 * Created by figengungor on 2/25/2018.
 */

public class PreferenceHelper {

    private static final String PREF_KEY_MOVIE_LIST_TYPE = "movie_list_type";

    private static PreferenceHelper instance;
    private SharedPreferences sharedPreferences;

    private PreferenceHelper(Application application) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
    }

    public static PreferenceHelper getInstance(Application application) {
        if (instance == null) instance = new PreferenceHelper(application);
        return instance;
    }

    public String getMovieListType() {
        return sharedPreferences.getString(PREF_KEY_MOVIE_LIST_TYPE, Constants.DEFAULT_MOVIE_LIST_TYPE);
    }

    public void setMovieListType(String movieListType) {
        sharedPreferences.edit()
                .putString(PREF_KEY_MOVIE_LIST_TYPE, movieListType)
                .apply();
    }
}
