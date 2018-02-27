package com.figengungor.popularmovies.ui.movieDetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.figengungor.popularmovies.R;
import com.figengungor.popularmovies.data.model.Movie;
import com.figengungor.popularmovies.utils.ImageUtils;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.backdropIv)
    ImageView backdropIv;

    @BindView(R.id.posterIv)
    ImageView posterIv;

    @BindView(R.id.titleTv)
    TextView titleTv;

    @BindView(R.id.ratingTv)
    TextView ratingTv;

    @BindView(R.id.releaseDateTv)
    TextView releaseDateTv;

    @BindView(R.id.overviewTv)
    TextView overviewTv;

    public static final String EXTRA_MOVIE = "movie";
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        movie = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_MOVIE));
        setupUI();
    }

    private void setupUI() {
        setupToolbar();
        setupContent();
    }

    private void setupToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupContent() {
        ImageUtils.loadImageUrl(movie.getBackdropPath(), backdropIv, ImageUtils.ImageType.BACKDROP);
        ImageUtils.loadImageUrl(movie.getPosterPath(), posterIv, ImageUtils.ImageType.POSTER);
        titleTv.setText(movie.getTitle());
        releaseDateTv.setText(movie.getReleaseDate());
        ratingTv.setText(getString(R.string.rating, movie.getVoteAverage()));
        overviewTv.setText(movie.getOverview());

        //Accessibility setup
        titleTv.setContentDescription(getString(R.string.a11y_movie_title,movie.getTitle()));
        releaseDateTv.setContentDescription(getString(R.string.a11y_release_date, movie.getReleaseDate()));
        ratingTv.setContentDescription(getString(R.string.a11y_rating, movie.getVoteAverage()));
        overviewTv.setContentDescription(movie.getOverview());

    }
}
