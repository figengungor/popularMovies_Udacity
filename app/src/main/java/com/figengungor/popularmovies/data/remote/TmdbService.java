package com.figengungor.popularmovies.data.remote;

import com.figengungor.popularmovies.data.model.CastDetail;
import com.figengungor.popularmovies.data.model.MovieDetailResponse;
import com.figengungor.popularmovies.data.model.MovieListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by figengungor on 2/19/2018.
 */

public interface  TmdbService {

    @GET("movie/{type}")
    Call<MovieListResponse> getMovies(@Path("type") String type);

    @GET("movie/{movie_id}")
    Call<MovieDetailResponse> getMovieDetail(@Path("movie_id") int movieId, @Query("append_to_response") String appendToResponse);

    @GET("person/{person_id}")
    Call<CastDetail> getCastDetail(@Path("person_id") int personId);


}
