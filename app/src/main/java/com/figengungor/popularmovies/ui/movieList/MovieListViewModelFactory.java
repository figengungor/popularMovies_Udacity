package com.figengungor.popularmovies.ui.movieList;

import android.arch.lifecycle.ViewModelProvider;

import com.figengungor.popularmovies.data.DataManager;

/**
 * Created by figengungor on 2/20/2018.
 */

public class MovieListViewModelFactory implements ViewModelProvider.Factory {

    DataManager dataManager;

    public MovieListViewModelFactory(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public MovieListViewModel create(Class modelClass) {
        return new MovieListViewModel(dataManager);
    }
}
