package com.figengungor.popularmovies.utils;

import com.google.gson.Gson;

/**
 * Created by figengungor on 3/5/2018.
 */

public class JsonUtils {

    public static String convertModelToJsonString(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }
}
