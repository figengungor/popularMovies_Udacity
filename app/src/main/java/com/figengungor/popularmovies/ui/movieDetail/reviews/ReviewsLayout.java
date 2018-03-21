package com.figengungor.popularmovies.ui.movieDetail.reviews;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.figengungor.popularmovies.R;
import com.figengungor.popularmovies.data.model.Review;
import com.figengungor.popularmovies.data.model.Reviews;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by figengungor on 3/19/2018.
 */

public class ReviewsLayout extends ConstraintLayout {

    @BindView(R.id.reviewsRv)
    RecyclerView reviewsRv;

    @BindView(R.id.emptyMessageTv)
    AppCompatTextView emptyMessageTv;

    @BindView(R.id.seeMoreTv)
    AppCompatTextView seeMoreTv;

    @OnClick(R.id.seeMoreTv)
    void onSeeMoreClicked() {
        if (reviewsListener != null)
            reviewsListener.onSeeMoreReviewsClicked();
    }

    ReviewsListener reviewsListener;

    public interface ReviewsListener {
        void onSeeMoreReviewsClicked();
    }

    public ReviewsLayout(Context context) {
        super(context);
    }

    public ReviewsLayout(Context context, ReviewsListener reviewsListener, Reviews reviews) {
        super(context);
        init(context, reviewsListener);
        if (reviews.getReviews() == null || reviews.getReviews().size() == 0) {
            emptyMessageTv.setVisibility(View.VISIBLE);
            seeMoreTv.setVisibility(View.GONE);
        } else {
            emptyMessageTv.setVisibility(View.GONE);
            List<Review> reviewList = reviews.getReviews();
            if (reviewList.size() > 3) {
                reviewList = reviewList.subList(0, 3);
                seeMoreTv.setVisibility(View.VISIBLE);
            } else {
                seeMoreTv.setVisibility(View.GONE);
            }
            ReviewsAdapter reviewsAdapter = new ReviewsAdapter(reviewList);
            reviewsRv.setAdapter(reviewsAdapter);
            reviewsRv.setNestedScrollingEnabled(false);
        }
    }

    private void init(Context context, ReviewsListener reviewsListener) {
        this.reviewsListener = reviewsListener;
        View view = inflate(context, R.layout.layout_reviews, this);
        ButterKnife.bind(view);
    }

}
