package com.figengungor.popularmovies.ui.castDetail;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;

import com.figengungor.popularmovies.data.DataManager;
import com.figengungor.popularmovies.ui.movieList.MovieListViewModel;

/**
 * Created by figengungor on 2/20/2018.
 */

public class CastDetailViewModelFactory implements ViewModelProvider.Factory {

    DataManager dataManager;
    Application application;

    public CastDetailViewModelFactory(Application application, DataManager dataManager) {
        this.application = application;
        this.dataManager = dataManager;
    }

    @Override
    public CastDetailViewModel create(Class modelClass) {
        return new CastDetailViewModel(application, dataManager);
    }
}
