package com.figengungor.popularmovies.ui.movieList;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.figengungor.popularmovies.data.DataManager;
import com.figengungor.popularmovies.data.model.Movie;
import com.figengungor.popularmovies.data.model.MovieListResponse;
import com.figengungor.popularmovies.data.network.TmdbCallback;

import java.util.List;

import static com.figengungor.popularmovies.utils.ErrorUtils.EMPTY;

/**
 * Created by figengungor on 2/20/2018.
 */

public class MovieListViewModel extends ViewModel {

    MutableLiveData<List<Movie>> movieList;
    MutableLiveData<Boolean> isLoading;
    MutableLiveData<Throwable> error;

    DataManager dataManager;

    public MovieListViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
        movieList = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        error = new MutableLiveData<>();
    }

    public void getMovies() {
        isLoading.setValue(true);
        String movieListType = dataManager.getMovieListType();
        getMoviesRemote(movieListType);
    }

    private void getMoviesRemote(String movieListType){
        dataManager.getMovieList(movieListType, new TmdbCallback<MovieListResponse>() {
            @Override
            public void onSuccess(MovieListResponse response) {
                isLoading.setValue(false);
                if(response.getMovies()!=null && response.getMovies().size()>0) {
                    movieList.setValue(response.getMovies());
                } else {
                    error.setValue(new Throwable(EMPTY));
                }
            }

            @Override
            public void onFail(Throwable throwable) {
                isLoading.setValue(false);
                error.setValue(throwable);
            }
        });
    }

    public MutableLiveData<List<Movie>> getMovieList() {
        return movieList;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<Throwable> getError() {
        return error;
    }

    public void setMovieListType(String movieListType) {
        dataManager.saveMovieListType(movieListType);
    }

    public String getMovieListType() {
        return dataManager.getMovieListType();
    }


}
