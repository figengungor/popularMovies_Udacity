package com.figengungor.popularmovies.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by figengungor on 2/27/2018.
 */

public class DateUtils {

    public static String getFriendlyReleaseDate(String releaseDate) {
        SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat newFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        try {
            Date date = oldFormat.parse(releaseDate);
            return newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
