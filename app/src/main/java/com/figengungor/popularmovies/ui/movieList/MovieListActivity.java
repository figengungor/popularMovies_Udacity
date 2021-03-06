package com.figengungor.popularmovies.ui.movieList;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import com.figengungor.popularmovies.R;
import com.figengungor.popularmovies.data.DataManager;
import com.figengungor.popularmovies.data.model.Movie;
import com.figengungor.popularmovies.ui.movieDetail.MovieDetailActivity;
import com.figengungor.popularmovies.utils.ErrorUtils;
import com.figengungor.popularmovies.utils.JsonUtils;
import com.pnikosis.materialishprogress.ProgressWheel;
import org.parceler.Parcels;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.figengungor.popularmovies.utils.ErrorUtils.NO_FAVORITES;

public class MovieListActivity extends AppCompatActivity implements MovieAdapter.ItemListener {

    private static final int UPDATE_LIST_REQUEST_CODE = 1;
    @BindView(R.id.movieRv)
    RecyclerView movieRv;

    @BindView(R.id.messageLayout)
    ScrollView messageLayout;

    @BindView(R.id.messageTv)
    TextView messageTv;

    @BindView(R.id.loadingPw)
    ProgressWheel loadingPw;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.spinner)
    Spinner spinner;

    @BindView(R.id.retryBtn)
    Button retryBtn;

    @OnClick(R.id.retryBtn)
    void onRetryBtnClicked() {
        viewModel.getMovies();
    }

    MovieListViewModel viewModel;
    private static final String TAG = MovieListActivity.class.getSimpleName();
    private static final String KEY_RECYCLERVIEW_STATE = "recyclerview_state";
    private static final String KEY_MOVIE_LIST_RAW = "movie_list";
    Parcelable recyclerViewState;
    String movieListRaw = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.bind(this);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            recyclerViewState = savedInstanceState.getParcelable(KEY_RECYCLERVIEW_STATE);
        }

        viewModel = ViewModelProviders.
                of(this, new MovieListViewModelFactory(getApplication(), DataManager.getInstance(getApplication())))
                .get(MovieListViewModel.class);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_MOVIE_LIST_RAW) && viewModel.getMovieList().getValue() == null) {
                movieListRaw = savedInstanceState.getString(KEY_MOVIE_LIST_RAW);
                List<Movie> movieList = JsonUtils.convertJsonStringtoMovieListResponse(movieListRaw);
                viewModel.getMovieList().setValue(movieList);
            }
        }

        viewModel.getMovieList().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                Log.d(TAG, "onChanged: getMovieList -> " + JsonUtils.convertModelToJsonString(movies));
                if (movies != null) showMovies(movies);
                else movieRv.setVisibility(View.GONE);
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

    private void setupUI() {
        setupToolbar();
        setupSpinner();
        setupLayoutManager();
        loadData();
    }

    private void setupLayoutManager() {
        int spanCount = getResources().getInteger(R.integer.spanCount);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
        movieRv.setLayoutManager(gridLayoutManager);
    }

    private void loadData() {
        if (viewModel.getMovieList().getValue() == null) {
            viewModel.getMovies();
            Log.d(TAG, "loadData: fetch movies");
        }
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setupSpinner() {
      /*  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.movie_list_type_options, R.layout.item_spinner_movie_list_type);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/
        String[] movieListTypeOptionsArray = getResources().getStringArray(R.array.movie_list_type_options);
        ArrayList<String> movieListTypeOptions = new ArrayList<>(Arrays.asList(movieListTypeOptionsArray));
        MovieListTypeAdapter adapter = new MovieListTypeAdapter(this, movieListTypeOptions);

        spinner.setAdapter(adapter);

        final String[] movieListTypeValues = getResources().getStringArray(R.array.movie_list_type_values);
        String movieListType = viewModel.getMovieListType();
        for (int i = 0; i < movieListTypeValues.length; i++) {
            if (movieListType.equals(movieListTypeValues[i]))
                spinner.setSelection(i, false);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                movieRv.setVisibility(View.GONE);
                viewModel.setMovieListType(movieListTypeValues[position]);
                viewModel.getMovies();
                Log.d(TAG, "onItemSelected: Spinner called it");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void showMovies(List<Movie> movies) {
        movieRv.setVisibility(View.VISIBLE);
        messageLayout.setVisibility(View.GONE);
        MovieAdapter adapter = new MovieAdapter(movies, this);
        movieRv.setAdapter(adapter);
        if (recyclerViewState != null) {
            movieRv.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            recyclerViewState = null;
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
        movieRv.setVisibility(View.GONE);
        messageLayout.setVisibility(View.VISIBLE);
        if (throwable.getMessage() != null & throwable.getMessage().equals(NO_FAVORITES)) {
            retryBtn.setVisibility(View.GONE);
        } else {
            retryBtn.setVisibility(View.VISIBLE);
        }
        messageTv.setText(ErrorUtils.displayFriendlyErrorMessage(this, throwable));
    }

    @Override
    public void onItemClick(Movie movie) {
        startActivityForResult(new Intent(this, MovieDetailActivity.class)
                .putExtra(MovieDetailActivity.EXTRA_MOVIE, Parcels.wrap(movie)), UPDATE_LIST_REQUEST_CODE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Parcelable state = movieRv.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(KEY_RECYCLERVIEW_STATE, state);
        if (viewModel.getMovieList().getValue() != null) {
            movieListRaw = JsonUtils.convertModelToJsonString(viewModel.getMovieList().getValue());
            outState.putString(KEY_MOVIE_LIST_RAW, movieListRaw);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_LIST_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (viewModel.getMovieListType().equalsIgnoreCase(getString(R.string.favorite))) {
                    viewModel.getMovies();
                }
            }
        }
    }
}
