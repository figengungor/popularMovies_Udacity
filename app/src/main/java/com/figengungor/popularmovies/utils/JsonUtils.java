package com.figengungor.popularmovies.utils;

import com.figengungor.popularmovies.data.model.Movie;
import com.figengungor.popularmovies.data.model.MovieDetailResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by figengungor on 3/5/2018.
 */

public class JsonUtils {

    public static String convertModelToJsonString(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static List<Movie> convertJsonStringtoMovieListResponse(String movieListResponse) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Movie>>() {}.getType();
        List<Movie> movieList = gson.fromJson(movieListResponse, listType);
        return movieList;
    }

    public static MovieDetailResponse convertJsonStringtoMovieDetailResponse(String movieDetailResponseRaw) {
        Gson gson = new Gson();
        MovieDetailResponse movieDetailResponse = gson.fromJson(movieDetailResponseRaw, MovieDetailResponse.class);
        return movieDetailResponse;
    }
}
