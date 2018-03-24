package com.figengungor.popularmovies.ui.movieDetail.reviews;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.figengungor.popularmovies.R;
import com.figengungor.popularmovies.data.model.Review;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.reviewsRv)
    RecyclerView reviewsRv;

    public static final String EXTRA_REVIEWS = "reviews";
    private static final String KEY_RECYCLERVIEW_STATE = "recyclerview_state";
    Parcelable recyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<Review> reviewList = Parcels.unwrap(getIntent().getExtras().getParcelable(EXTRA_REVIEWS));
        ReviewsAdapter reviewsAdapter = new ReviewsAdapter(reviewList);
        reviewsRv.setAdapter(reviewsAdapter);

        if (savedInstanceState != null) {
            recyclerViewState = savedInstanceState.getParcelable(KEY_RECYCLERVIEW_STATE);
            reviewsRv.getLayoutManager().onRestoreInstanceState(recyclerViewState);

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Parcelable state = reviewsRv.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(KEY_RECYCLERVIEW_STATE, state);
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
