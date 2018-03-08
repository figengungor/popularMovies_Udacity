package com.figengungor.popularmovies.ui.movieDetail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.figengungor.popularmovies.R;
import com.figengungor.popularmovies.data.DataManager;
import com.figengungor.popularmovies.data.model.Movie;
import com.figengungor.popularmovies.utils.DateUtils;
import com.figengungor.popularmovies.utils.ImageUtils;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "movie";
    Movie movie;
    MovieDetailViewModel viewModel;

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

    @BindView(R.id.favoriteBtn)
    ImageView favoriteBtn;

    @OnClick(R.id.favoriteBtn)
    void onFavoriteBtnClicked() {
        viewModel.updateFavorite(movie);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        movie = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_MOVIE));
        init();
    }

    private void init() {
        viewModel = ViewModelProviders.
                of(this, new MovieDetailViewModelFactory(getApplication(), DataManager.getInstance(getApplication())))
                .get(MovieDetailViewModel.class);

        viewModel.getIsFavorite().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isFavorite) {
                if (isFavorite != null) {
                    showFavorite(isFavorite);
                }
            }
        });

        viewModel.getIsFavoriteChangedAskingBecauseGonnaUpdatePreviousActivityIfFavoriteIsListType().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isFavoriteChanged) {
                if (isFavoriteChanged)
                    setResult(RESULT_OK);
            }
        });

        setupUI();
    }


    private void setupUI() {
        setupToolbar();
        setupContent();
        checkFavorite();
    }

    private void checkFavorite() {
        if (viewModel.getIsFavorite().getValue() == null)
            viewModel.checkFavorite(movie);
    }

    private void setupToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupContent() {
        String backdropPath = movie.getBackdropPath();
        String posterPath = movie.getPosterPath();
        String title = movie.getTitle();
        String friendlyReleaseDate = DateUtils.getFriendlyReleaseDate(movie.getReleaseDate());
        double voteAverage = movie.getVoteAverage();
        String overview = movie.getOverview();

        ImageUtils.loadImageUrl(backdropPath, backdropIv, ImageUtils.ImageType.BACKDROP);
        ImageUtils.loadImageUrl(posterPath, posterIv, ImageUtils.ImageType.POSTER);
        titleTv.setText(title);
        releaseDateTv.setText(friendlyReleaseDate);
        ratingTv.setText(getString(R.string.rating, voteAverage));
        overviewTv.setText(overview);

        //Accessibility setup
        titleTv.setContentDescription(getString(R.string.a11y_movie_title, title));
        releaseDateTv.setContentDescription(getString(R.string.a11y_release_date, friendlyReleaseDate));
        ratingTv.setContentDescription(getString(R.string.a11y_rating, voteAverage));
        overviewTv.setContentDescription(overview);
    }

    private void showFavorite(Boolean isFavorite) {
        if (isFavorite) {
            favoriteBtn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_black_24dp));
        } else {
            favoriteBtn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_star_border_black_24dp));
        }
    }
}
