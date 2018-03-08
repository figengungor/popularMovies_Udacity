package com.figengungor.popularmovies.ui.movieDetail;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;

import com.figengungor.popularmovies.data.DataManager;

/**
 * Created by figengungor on 2/20/2018.
 */

public class MovieDetailViewModelFactory implements ViewModelProvider.Factory {

    DataManager dataManager;
    Application application;

    public MovieDetailViewModelFactory(Application application, DataManager dataManager) {
        this.application = application;
        this.dataManager = dataManager;
    }

    @Override
    public MovieDetailViewModel create(Class modelClass) {
        return new MovieDetailViewModel(application, dataManager);
    }
}
