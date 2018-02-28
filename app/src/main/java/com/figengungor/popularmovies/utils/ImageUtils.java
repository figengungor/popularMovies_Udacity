package com.figengungor.popularmovies.utils;

import android.util.Log;
import android.widget.ImageView;

import com.figengungor.popularmovies.R;
import com.squareup.picasso.Picasso;

public class ImageUtils {

    private static final String TAG = ImageUtils.class.getSimpleName();

    public enum ImageType {
        POSTER,
        BACKDROP
    }

    private static final String IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String PATH_POSTER_W185 = "w342";
    private static final String PATH_BACKDROP_W780 = "w780";
    private static final String POSTER_URL = IMAGE_URL + PATH_POSTER_W185;
    private static final String BACKDROP_URL = IMAGE_URL + PATH_BACKDROP_W780;


    public static void loadImageUrl(String imagePath, ImageView imageView, ImageType imageType) {
        String url;
        int placeholderResId;
        int placeholderErrorResId;
        switch (imageType) {
            case POSTER:
                url = POSTER_URL + imagePath;
                placeholderResId = R.drawable.placeholder_poster;
                placeholderErrorResId = R.drawable.placeholder_error_poster;
                break;
            case BACKDROP:
                url = BACKDROP_URL + imagePath;
                placeholderResId = R.drawable.placeholder_backdrop;
                placeholderErrorResId = R.drawable.placeholder_error_backdrop;
                break;
            default:
                throw new UnsupportedOperationException("ImageType not supported");
        }
        Log.e(TAG, "loadImageUrl: "+url);
        Picasso.with(imageView.getContext())
                .load(url)
                .noFade()
                .placeholder(placeholderResId)
                .error(placeholderErrorResId)
                .into(imageView);
    }

}