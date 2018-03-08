package com.figengungor.popularmovies.data.local;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by figengungor on 3/6/2018.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.figengungor.popularmovies";

    public static final Uri BASE_CONTEMT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVORITE_MOVIE = "favorite_movie";

    public static final class FavoriteMovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTEMT_URI.buildUpon()
                .appendPath(PATH_FAVORITE_MOVIE)
                .build();

        public static final String TABLE_NAME = "favorite_movie";

        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_ADULT = "adult";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_ORIGINAL_LANGUAGE = "original_language";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_VIDEO = "video";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

    }

}
