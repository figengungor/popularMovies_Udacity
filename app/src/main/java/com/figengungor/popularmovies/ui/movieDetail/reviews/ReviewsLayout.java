package com.figengungor.popularmovies.ui.movieDetail.reviews;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.figengungor.popularmovies.R;
import com.figengungor.popularmovies.data.model.Reviews;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by figengungor on 3/19/2018.
 */

public class ReviewsLayout extends LinearLayout {

    @BindView(R.id.reviewsRv)
    RecyclerView reviewsRv;

    @BindView(R.id.emptyMessageTv)
    AppCompatTextView emptyMessageTv;

    public ReviewsLayout(Context context) {
        super(context);
    }

    public ReviewsLayout(Context context, Reviews reviews) {
        super(context);
        init(context);
        if (reviews.getReviews() == null || reviews.getReviews().size() == 0) {
            emptyMessageTv.setVisibility(View.VISIBLE);
        } else {
            emptyMessageTv.setVisibility(View.GONE);
            ReviewsAdapter reviewsAdapter = new ReviewsAdapter(reviews.getReviews());
            reviewsRv.setAdapter(reviewsAdapter);
            reviewsRv.setNestedScrollingEnabled(false);
            reviewsRv.setHasFixedSize(true);
        }
    }

    private void init(Context context) {
        View view = inflate(context, R.layout.layout_reviews, this);
        ButterKnife.bind(view);
    }

}
