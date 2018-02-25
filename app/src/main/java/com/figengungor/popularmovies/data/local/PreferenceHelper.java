package com.figengungor.popularmovies.data.local;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.figengungor.popularmovies.R;

/**
 * Created by figengungor on 2/25/2018.
 */

public class PreferenceHelper {

    private static final String PREF_KEY_MOVIE_LIST_TYPE = "movie_list_type";

    private static PreferenceHelper instance;
    private SharedPreferences sharedPreferences;
    private Application application;


    private PreferenceHelper(Application application) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        this.application = application;
    }

    public static PreferenceHelper getInstance(Application application) {
        if (instance == null) instance = new PreferenceHelper(application);
        return instance;
    }

    public String getMovieListType() {
        return sharedPreferences.getString(PREF_KEY_MOVIE_LIST_TYPE, application.getString(R.string.movie_list_type_default));
    }

    public void setMovieListType(String movieListType) {
        sharedPreferences.edit()
                .putString(PREF_KEY_MOVIE_LIST_TYPE, movieListType)
                .apply();
    }
}
