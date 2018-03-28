package com.figengungor.popularmovies.ui;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.figengungor.popularmovies.R;
import com.figengungor.popularmovies.data.model.Movie;
import com.figengungor.popularmovies.ui.movieDetail.MovieDetailActivity;
import com.figengungor.popularmovies.ui.movieDetail.MovieDetailFragment;
import com.figengungor.popularmovies.ui.movieList.MovieListFragment;
import org.parceler.Parcels;

public class MainActivity extends AppCompatActivity implements MovieListFragment.OnInteractionListener,
MovieDetailFragment.OnInteractionListener{

    private static final int UPDATE_LIST_REQUEST_CODE = 1;
    boolean isTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.movieListContainer, new MovieListFragment())
                    .commit();
        }

        if (findViewById(R.id.movieDetailContainer) != null) {
            isTwoPane = true;
        }
    }

    @Override
    public void onMovieSelected(Movie movie) {
        if (isTwoPane) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.movieDetailContainer, MovieDetailFragment.newInstance(movie))
                    .commit();
        } else {
            startActivityForResult(new Intent(this, MovieDetailActivity.class)
                    .putExtra(MovieDetailActivity.EXTRA_MOVIE, Parcels.wrap(movie)), UPDATE_LIST_REQUEST_CODE);
        }

    }

    @Override
    public void onSimilarSeriesSelected(Movie movie) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.movieDetailContainer, MovieDetailFragment.newInstance(movie))
                .commit();
    }

}
