package com.figengungor.popularmovies;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by figengungor on 3/6/2018.
 */

public class PopularMoviesApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
