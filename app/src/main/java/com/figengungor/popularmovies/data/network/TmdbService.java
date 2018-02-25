package com.figengungor.popularmovies.data.network;

import com.figengungor.popularmovies.data.model.MovieListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by figengungor on 2/19/2018.
 */

public interface  TmdbService {

    @GET("movie/{type}")
    Call<MovieListResponse> getMovies(@Path("type") String type);

}
