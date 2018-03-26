package com.figengungor.popularmovies.ui.movieDetail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.figengungor.popularmovies.R;
import com.figengungor.popularmovies.data.DataManager;
import com.figengungor.popularmovies.data.model.Cast;
import com.figengungor.popularmovies.data.model.Movie;
import com.figengungor.popularmovies.data.model.MovieDetailResponse;
import com.figengungor.popularmovies.data.model.Video;
import com.figengungor.popularmovies.data.model.Videos;
import com.figengungor.popularmovies.ui.castDetail.CastDetailActivity;
import com.figengungor.popularmovies.ui.movieDetail.cast.CastAdapter;
import com.figengungor.popularmovies.ui.movieDetail.cast.CastLayout;
import com.figengungor.popularmovies.ui.movieDetail.details.DetailsLayout;
import com.figengungor.popularmovies.ui.movieDetail.genres.GenresLayout;
import com.figengungor.popularmovies.ui.movieDetail.reviews.ReviewsActivity;
import com.figengungor.popularmovies.ui.movieDetail.reviews.ReviewsLayout;
import com.figengungor.popularmovies.ui.movieDetail.similarMovies.SimilarMoviesAdapter;
import com.figengungor.popularmovies.ui.movieDetail.similarMovies.SimilarMoviesLayout;
import com.figengungor.popularmovies.ui.movieDetail.videos.VideosAdapter;
import com.figengungor.popularmovies.ui.movieDetail.videos.VideosLayout;
import com.figengungor.popularmovies.utils.DateUtils;
import com.figengungor.popularmovies.utils.ErrorUtils;
import com.figengungor.popularmovies.utils.ImageUtils;
import com.figengungor.popularmovies.utils.JsonUtils;
import com.pnikosis.materialishprogress.ProgressWheel;
import org.parceler.Parcels;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieDetailActivity extends AppCompatActivity implements
        VideosAdapter.ItemListener,
        CastAdapter.ItemListener,
        SimilarMoviesAdapter.ItemListener,
        ReviewsLayout.ReviewsListener {

    public static final String EXTRA_MOVIE = "movie";
    private static final String KEY_MOVIE_DETAIL_RESPONSE_RAW = "movie_detail_response";
    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    Movie movie;
    MovieDetailViewModel viewModel;
    String trailerKey;
    String movieDetailResponseRaw;

    @BindView(R.id.backdropIv)
    ImageView backdropIv;

    @BindView(R.id.posterIv)
    ImageView posterIv;

    @BindView(R.id.playTrailerBtn)
    ImageButton playTrailerBtn;

    @BindView(R.id.titleTv)
    TextView titleTv;

    @BindView(R.id.ratingTv)
    TextView ratingTv;

    @BindView(R.id.releaseDateTv)
    TextView releaseDateTv;

    @BindView(R.id.overviewTv)
    TextView overviewTv;

    @BindView(R.id.favoriteBtn)
    FloatingActionButton favoriteBtn;

    @BindView(R.id.contentLl)
    LinearLayout contentLl;

    @BindView(R.id.messageLayout)
    ScrollView messageLayout;

    @BindView(R.id.messageTv)
    TextView messageTv;

    @BindView(R.id.loadingPw)
    ProgressWheel loadingPw;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.retryBtn)
    Button retryBtn;

    @OnClick(R.id.playTrailerBtn)
    void onPlayTrailerBtnClicked() {
        watchYoutubeVideo(trailerKey);
    }

    @OnClick(R.id.favoriteBtn)
    void onFavoriteBtnClicked() {
        viewModel.updateFavorite(movie);
    }

    @OnClick(R.id.retryBtn)
    void onRetryBtnClicked() {
        viewModel.getMovieDetailResponse(movie.getId());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        movie = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_MOVIE));
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        viewModel = ViewModelProviders.
                of(this, new MovieDetailViewModelFactory(getApplication(), DataManager.getInstance(getApplication())))
                .get(MovieDetailViewModel.class);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_MOVIE_DETAIL_RESPONSE_RAW) && viewModel.getMovieDetailResponse().getValue() == null) {
                movieDetailResponseRaw = savedInstanceState.getString(KEY_MOVIE_DETAIL_RESPONSE_RAW);
                MovieDetailResponse movieDetailResponse = JsonUtils.convertJsonStringtoMovieDetailResponse(movieDetailResponseRaw);
                viewModel.getMovieDetailResponse().setValue(movieDetailResponse);
            }
        }

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
                if (movieDetailResponse != null) showMovieDetail(movieDetailResponse);
                else contentLl.setVisibility(View.GONE);
            }
        });

        viewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                Log.d(TAG, "onChanged: getIsLoading -> " + isLoading);
                showLoadingIndicator(isLoading);
            }
        });

        viewModel.getError().observe(this, new Observer<Throwable>() {
            @Override
            public void onChanged(@Nullable Throwable throwable) {
                Log.d(TAG, "onChanged: getError -> " + throwable);
                if (throwable != null) showError(throwable);
                else messageLayout.setVisibility(View.GONE);

            }
        });

        setupUI();
    }

    private void showMovieDetail(MovieDetailResponse movieDetailResponse) {

        contentLl.setVisibility(View.VISIBLE);
        messageLayout.setVisibility(View.GONE);

        //GENRES
        contentLl.addView(new GenresLayout(this, movieDetailResponse.getGenres()));

        //VIDEOS
        showTrailerBtn(movieDetailResponse.getVideos());
        contentLl.addView(new VideosLayout(this, this, movieDetailResponse.getVideos()));

        //REVIEWS
        contentLl.addView(new ReviewsLayout(this, this, movieDetailResponse.getReviews()));

        //CAST
        contentLl.addView(new CastLayout(this, this, movieDetailResponse.getCredits()));

        //SIMILAR MOVIES
        contentLl.addView(new SimilarMoviesLayout(this, this, movieDetailResponse.getSimilar()));

        //DETAILS
        contentLl.addView(new DetailsLayout(this, movieDetailResponse));
    }

    private void showTrailerBtn(Videos videos) {
        List<Video> videoList = videos.getVideos();
        if (videoList != null
                && videoList.size() > 0) {
            for (Video video : videoList) {
                if (video.getType().equalsIgnoreCase("Trailer")) {
                    trailerKey = video.getKey();
                    playTrailerBtn.setVisibility(View.VISIBLE);
                    playTrailerBtn.animate().
                            scaleX(2.0f).
                            scaleY(2.0f).
                            setDuration(600).start();
                    break;
                }
            }
        }
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
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(movie.getTitle());
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

    public void watchYoutubeVideo(String id) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id));
        String title = getResources().getString(R.string.youtube_video_choser_title);
        Intent chooser = Intent.createChooser(intent, title);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }

    private void showLoadingIndicator(Boolean isLoading) {
        if (isLoading) {
            loadingPw.setVisibility(View.VISIBLE);
        } else {
            loadingPw.setVisibility(View.GONE);
        }
    }

    private void showError(Throwable throwable) {
        contentLl.setVisibility(View.GONE);
        messageLayout.setVisibility(View.VISIBLE);
        retryBtn.setVisibility(View.VISIBLE);
        messageTv.setText(ErrorUtils.displayFriendlyErrorMessage(this, throwable));
    }

    @Override
    public void onSeeMoreReviewsClicked() {
        startActivity(new Intent(this, ReviewsActivity.class)
                .putExtra(ReviewsActivity.EXTRA_REVIEWS, Parcels.wrap(viewModel.getMovieDetailResponse().getValue().getReviews().getReviews())));
    }

    @Override
    public void onItemClicked(Video item) {
        watchYoutubeVideo(item.getKey());
    }

    @Override
    public void onItemClicked(Cast item) {
        startActivity(new Intent(this, CastDetailActivity.class)
                .putExtra(CastDetailActivity.EXTRA_PERSON_ID, Parcels.wrap(item)));
    }

    @Override
    public void onItemClicked(Movie item) {
        startActivity(new Intent(this, MovieDetailActivity.class)
                .putExtra(EXTRA_MOVIE, Parcels.wrap(item)));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (viewModel.getMovieDetailResponse().getValue() != null) {
            movieDetailResponseRaw = JsonUtils.convertModelToJsonString(viewModel.getMovieDetailResponse().getValue());
            outState.putString(KEY_MOVIE_DETAIL_RESPONSE_RAW, movieDetailResponseRaw);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
