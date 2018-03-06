package com.figengungor.popularmovies.data;

import android.app.Application;

import com.figengungor.popularmovies.data.local.PreferenceHelper;
import com.figengungor.popularmovies.data.model.MovieListResponse;
import com.figengungor.popularmovies.data.remote.TmdbCallback;
import com.figengungor.popularmovies.data.remote.TmdbService;
import com.figengungor.popularmovies.data.remote.TmdbServiceFactory;

/**
 * Created by figengungor on 2/19/2018.
 */

public class DataManager {

    private static DataManager instance;
    private TmdbService tmdbService;
    private PreferenceHelper preferenceHelper;

    private DataManager(Application application) {
        this.tmdbService = TmdbServiceFactory.createService();
        this.preferenceHelper = PreferenceHelper.getInstance(application);
    }

    public static DataManager getInstance(Application application) {
        if (instance == null) instance = new DataManager(application);
        return instance;
    }

    public void getMovieList(String movieListType, TmdbCallback<MovieListResponse> listener) {
        tmdbService.getMovies(movieListType)
                .enqueue(listener);
    }

    public String getMovieListType(){
        return preferenceHelper.getMovieListType();
    }

    public void saveMovieListType(String movieListType){
        preferenceHelper.setMovieListType(movieListType);
    }
}
