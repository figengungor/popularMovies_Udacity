package com.figengungor.popularmovies.ui.movieList;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.figengungor.popularmovies.R;
import com.figengungor.popularmovies.data.DataManager;
import com.figengungor.popularmovies.data.model.Movie;
import com.figengungor.popularmovies.ui.movieDetail.MovieDetailActivity;
import com.figengungor.popularmovies.utils.ErrorUtils;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieListActivity extends AppCompatActivity implements MovieAdapter.ItemListener {

    @BindView(R.id.movieRv)
    RecyclerView movieRv;

    @BindView(R.id.messageTv)
    TextView messageTv;

    @BindView(R.id.loadingPw)
    ProgressWheel loadingPw;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.spinner)
    Spinner spinner;

    MovieListViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.bind(this);
        init();

    }

    private void init() {
        viewModel = ViewModelProviders.
                of(this, new MovieListViewModelFactory(getApplication(), DataManager.getInstance(getApplication())))
                .get(MovieListViewModel.class);

        viewModel.getMovieList().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                showMovies(movies);
            }
        });

        viewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                showLoadingIndicator(isLoading);
            }
        });

        viewModel.getError().observe(this, new Observer<Throwable>() {
            @Override
            public void onChanged(@Nullable Throwable throwable) {
                showError(throwable);
            }
        });

        setupUI();
    }

    private void setupUI() {
        setupToolbar();
        setupSpinner();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.movie_list_type_options, R.layout.item_spinner_movie_list_type);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        final String[] movieListTypeValues = getResources().getStringArray(R.array.movie_list_type_values);
        String movieListType = viewModel.getMovieListType();
        for (int i = 0; i < movieListTypeValues.length; i++) {
            if (movieListType.equals(movieListTypeValues[i]))
                spinner.setSelection(i);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.setMovieListType(movieListTypeValues[position]);
                viewModel.getMovies();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showMovies(List<Movie> movies) {
        movieRv.setVisibility(View.VISIBLE);
        messageTv.setVisibility(View.GONE);
        MovieAdapter adapter = new MovieAdapter(movies, this);
        movieRv.setAdapter(adapter);
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
        messageTv.setVisibility(View.VISIBLE);
        messageTv.setText(ErrorUtils.displayFriendlyErrorMessage(this, throwable));
    }

    @Override
    public void onItemClick(Movie movie) {
        startActivity(new Intent(this, MovieDetailActivity.class)
        .putExtra(MovieDetailActivity.EXTRA_MOVIE, Parcels.wrap(movie)));
    }
}
