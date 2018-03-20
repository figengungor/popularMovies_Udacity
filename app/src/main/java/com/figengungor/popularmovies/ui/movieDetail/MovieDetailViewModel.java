package com.figengungor.popularmovies.ui.movieDetail;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.figengungor.popularmovies.data.DataManager;
import com.figengungor.popularmovies.data.local.MovieContract.FavoriteMovieEntry;
import com.figengungor.popularmovies.data.model.Movie;
import com.figengungor.popularmovies.data.model.MovieDetailResponse;
import com.figengungor.popularmovies.data.remote.TmdbCallback;

/**
 * Created by figengungor on 3/8/2018.
 */

public class MovieDetailViewModel extends AndroidViewModel {

    DataManager dataManager;
    MutableLiveData<Boolean> isFavorite;
    MutableLiveData<Boolean> isFavoriteChanged;

    MutableLiveData<MovieDetailResponse> movieDetailResponse;
    MutableLiveData<Boolean> isLoading;
    MutableLiveData<Throwable> error;

    public MovieDetailViewModel(@NonNull Application application, DataManager dataManager) {
        super(application);
        this.dataManager = dataManager;
        isFavorite = new MutableLiveData<>();
        isFavoriteChanged = new MutableLiveData<>();
        isFavoriteChanged.setValue(false);
        movieDetailResponse = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        error = new MutableLiveData<>();
    }

    public void updateFavorite(Movie movie) {
        if (isFavorite.getValue() != null) {
            isFavoriteChanged.setValue(true);
            if (isFavorite.getValue()) {
                removeFavorite(movie);
            } else {
                addFavorite(movie);
            }
        }
    }

    private void addFavorite(final Movie movie) {
        new AsyncTask<Void, Void, Uri>() {
            @Override
            protected Uri doInBackground(Void... voids) {
                Uri uri = getApplication().getContentResolver().insert(FavoriteMovieEntry.CONTENT_URI,
                        convertMovieToContentValues(movie));
                return uri;
            }

            @Override
            protected void onPostExecute(Uri uri) {
                super.onPostExecute(uri);
                    if (uri != null) {
                        isFavorite.setValue(true);
                    }
            }
        }.execute();

    }

    private void removeFavorite(final Movie movie) {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                String selection = FavoriteMovieEntry.COLUMN_ID + " = " + movie.getId();
                Integer count = getApplication().getContentResolver().delete(FavoriteMovieEntry.CONTENT_URI,
                        selection,
                        null);
                return count;
            }

            @Override
            protected void onPostExecute(Integer count) {
                super.onPostExecute(count);
                    if (count != -1) {
                        isFavorite.setValue(false);
                    }
            }
        }.execute();

    }

    public void checkFavorite(final Movie movie) {
        new AsyncTask<Void, Void, Cursor>() {
            @Override
            protected Cursor doInBackground(Void... voids) {
                String selection = FavoriteMovieEntry.COLUMN_ID + " = " + movie.getId();
                Cursor cursor = getApplication().getContentResolver().query(FavoriteMovieEntry.CONTENT_URI,
                        null,
                        selection,
                        null,
                        null);
                return cursor;
            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                super.onPostExecute(cursor);
                    if (cursor != null) {
                        if (cursor.getCount() > 0)
                            isFavorite.setValue(true);
                        else
                            isFavorite.setValue(false);
                    }
            }
        }.execute();
    }

    private ContentValues convertMovieToContentValues(Movie movie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoriteMovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        contentValues.put(FavoriteMovieEntry.COLUMN_ADULT, movie.getAdult() ? 1 : 0);
        contentValues.put(FavoriteMovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        contentValues.put(FavoriteMovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(FavoriteMovieEntry.COLUMN_ID, movie.getId());
        contentValues.put(FavoriteMovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        contentValues.put(FavoriteMovieEntry.COLUMN_ORIGINAL_LANGUAGE, movie.getOriginalLanguage());
        contentValues.put(FavoriteMovieEntry.COLUMN_TITLE, movie.getTitle());
        contentValues.put(FavoriteMovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
        contentValues.put(FavoriteMovieEntry.COLUMN_POPULARITY, movie.getPopularity());
        contentValues.put(FavoriteMovieEntry.COLUMN_VOTE_COUNT, movie.getVoteCount());
        contentValues.put(FavoriteMovieEntry.COLUMN_VIDEO, movie.getVideo() ? 1 : 0);
        contentValues.put(FavoriteMovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        return contentValues;
    }


    public void getMovieDetailResponse(int movieId){
        isLoading.setValue(true);
        movieDetailResponse.setValue(null);
        error.setValue(null);
        dataManager.getMovieDetail(movieId, new TmdbCallback<MovieDetailResponse>() {
            @Override
            public void onSuccess(MovieDetailResponse response) {
                isLoading.setValue(false);
                movieDetailResponse.setValue(response);
                error.setValue(null);
            }

            @Override
            public void onFail(Throwable throwable) {
                isLoading.setValue(false);
                error.setValue(throwable);
            }
        });
    }

    public MutableLiveData<Boolean> getIsFavorite() {
        return isFavorite;
    }

    public MutableLiveData<Boolean> getIsFavoriteChanged() {
        return isFavoriteChanged;
    }

    public MutableLiveData<MovieDetailResponse> getMovieDetailResponse() {
        return movieDetailResponse;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<Throwable> getError() {
        return error;
    }
}
