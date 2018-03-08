package com.figengungor.popularmovies.ui.movieList;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.database.Cursor;
import android.os.AsyncTask;

import com.figengungor.popularmovies.R;
import com.figengungor.popularmovies.data.DataManager;
import com.figengungor.popularmovies.data.local.MovieContract.FavoriteMovieEntry;
import com.figengungor.popularmovies.data.model.Movie;
import com.figengungor.popularmovies.data.model.MovieListResponse;
import com.figengungor.popularmovies.data.remote.TmdbCallback;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

import static com.figengungor.popularmovies.utils.ErrorUtils.EMPTY;
import static com.figengungor.popularmovies.utils.ErrorUtils.NO_FAVORITES;

/**
 * Created by figengungor on 2/20/2018.
 */

public class MovieListViewModel extends AndroidViewModel {

    MutableLiveData<List<Movie>> movieList;
    MutableLiveData<Boolean> isLoading;
    MutableLiveData<Throwable> error;
    Call call;
    AsyncTask<Void, Void, Cursor> fetchFavoriteMoviesTask;
    DataManager dataManager;

    public MovieListViewModel(Application application, DataManager dataManager) {
        super(application);
        this.dataManager = dataManager;
        movieList = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        error = new MutableLiveData<>();
    }

    public void getMovies() {
        isLoading.setValue(true);
        movieList.setValue(null);
        error.setValue(null);
        //cancel remote requests
        if (call != null) {
            call.cancel();
        }
        //cancel local request
        if (fetchFavoriteMoviesTask != null) {
            fetchFavoriteMoviesTask.cancel(true);
        }
        String movieListType = getMovieListType();
        if (movieListType.equalsIgnoreCase(getApplication().getString(R.string.favorite))) {
            getFavorites();
        } else {
            getMoviesRemote(movieListType);
        }
    }

    private void getFavorites() {
        fetchFavoriteMoviesTask = new AsyncTask<Void, Void, Cursor>() {
            @Override
            protected Cursor doInBackground(Void... voids) {
                Cursor cursor = getApplication().getContentResolver().query(FavoriteMovieEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);
                return cursor;
            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                super.onPostExecute(cursor);
                if (!isCancelled()) {
                    isLoading.setValue(false);
                    List<Movie> movies = convertCursorToMovieList(cursor);
                    if (movies.size() > 0)
                        movieList.setValue(movies);
                    else
                        error.setValue(new Throwable(NO_FAVORITES));

                }
            }
        };

        fetchFavoriteMoviesTask.execute();

    }

    private List<Movie> convertCursorToMovieList(Cursor cursor) {
        List<Movie> movies = new ArrayList<>();
        while (cursor.moveToNext()) {
            Movie movie = new Movie();
            movie.setPosterPath(cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_POSTER_PATH)));
            movie.setAdult(1 == cursor.getInt(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_ADULT)));
            movie.setOverview(cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_OVERVIEW)));
            movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_RELEASE_DATE)));
            movie.setId(cursor.getInt(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_ID)));
            movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_ORIGINAL_TITLE)));
            movie.setOriginalLanguage(cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_ORIGINAL_LANGUAGE)));
            movie.setTitle(cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_TITLE)));
            movie.setBackdropPath(cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_BACKDROP_PATH)));
            movie.setPopularity(cursor.getDouble(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_POPULARITY)));
            movie.setVoteCount(cursor.getInt(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_VOTE_COUNT)));
            movie.setVideo(1 == cursor.getInt(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_VIDEO)));
            movie.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_VOTE_AVERAGE)));
            movies.add(movie);
        }
        return movies;
    }

    private void getMoviesRemote(String movieListType) {
        call = dataManager.getMovieList(movieListType, new TmdbCallback<MovieListResponse>() {
            @Override
            public void onSuccess(MovieListResponse response) {
                isLoading.setValue(false);
                if (response.getMovies() != null && response.getMovies().size() > 0) {
                    movieList.setValue(response.getMovies());
                    error.setValue(null);
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
