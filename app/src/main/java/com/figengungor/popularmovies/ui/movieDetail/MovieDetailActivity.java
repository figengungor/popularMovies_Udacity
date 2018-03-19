package com.figengungor.popularmovies.ui.movieDetail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.figengungor.popularmovies.R;
import com.figengungor.popularmovies.data.DataManager;
import com.figengungor.popularmovies.data.model.Movie;
import com.figengungor.popularmovies.data.model.MovieDetailResponse;
import com.figengungor.popularmovies.data.model.Video;
import com.figengungor.popularmovies.ui.movieDetail.reviews.ReviewsLayout;
import com.figengungor.popularmovies.ui.movieDetail.videos.VideosAdapter;
import com.figengungor.popularmovies.ui.movieDetail.videos.VideosLayout;
import com.figengungor.popularmovies.utils.DateUtils;
import com.figengungor.popularmovies.utils.ImageUtils;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieDetailActivity extends AppCompatActivity implements VideosAdapter.ItemListener {

    public static final String EXTRA_MOVIE = "movie";
    private static final String TAG = MovieDetailActivity.class.getSimpleName();
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

    @BindView(R.id.contentLl)
    LinearLayout contentLl;

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

        viewModel.getIsFavoriteChanged().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isFavoriteChanged) {
                if (isFavoriteChanged)
                    setResult(RESULT_OK);
            }
        });

        viewModel.getMovieDetailResponse().observe(this, new Observer<MovieDetailResponse>() {
            @Override
            public void onChanged(@Nullable MovieDetailResponse movieDetailResponse) {
                showMovieDetail(movieDetailResponse);
            }
        });

        setupUI();
    }

    private void showMovieDetail(MovieDetailResponse movieDetailResponse) {
        //VIDEOS
        contentLl.addView(new VideosLayout(this, movieDetailResponse.getVideos()));

        //REVIEWS
        contentLl.addView(new ReviewsLayout(this, movieDetailResponse.getReviews()));
    }

    private void setupUI() {
        setupToolbar();
        setupContent();
        checkFavorite();
        loadMovieDetail();
    }

    private void loadMovieDetail() {
        if (viewModel.getMovieDetailResponse().getValue() == null) {
            viewModel.getMovieDetailResponse(movie.getId());
        }
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
        setRating(getString(R.string.rating, voteAverage));
        overviewTv.setText(overview);

        //Accessibility setup
        titleTv.setContentDescription(getString(R.string.a11y_movie_title, title));
        releaseDateTv.setContentDescription(getString(R.string.a11y_release_date, friendlyReleaseDate));
        ratingTv.setContentDescription(getString(R.string.a11y_rating, voteAverage));
        overviewTv.setContentDescription(overview);
    }

    private void setRating(String voteAverageStr) {
        int end = voteAverageStr.length();
        int start = end - 3;
        SpannableString ss1 = new SpannableString(voteAverageStr);
        ss1.setSpan(new RelativeSizeSpan(0.5f), start, end, 0); // set size
        ratingTv.setText(ss1);
    }

    private void showFavorite(Boolean isFavorite) {
        if (isFavorite) {
            favoriteBtn.setImageResource(R.drawable.ic_star_black_24dp);
            favoriteBtn.setContentDescription(getString(R.string.remove_from_favorites_button_content_description));
        } else {
            favoriteBtn.setImageResource(R.drawable.ic_star_border_black_24dp);
            favoriteBtn.setContentDescription(getString(R.string.add_to_favorites_button_content_description));
        }
    }

    @Override
    public void onItemClicked(Video item) {
        watchYoutubeVideo(item.getKey());
    }

    public void watchYoutubeVideo(String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

}
