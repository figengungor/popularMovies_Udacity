package com.figengungor.popularmovies.ui.movieDetail;



import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import com.figengungor.popularmovies.R;
import com.figengungor.popularmovies.data.model.Movie;
import org.parceler.Parcels;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailFragment.OnInteractionListener {

    public static final String EXTRA_MOVIE = "movie";
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        movie = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_MOVIE));
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.movieDetailContainer, MovieDetailFragment.newInstance(movie))
                    .commit();
        }
    }

    @Override
    public void onSimilarSeriesSelected(Movie movie) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.movieDetailContainer, MovieDetailFragment.newInstance(movie))
                .commit();
    }
}
