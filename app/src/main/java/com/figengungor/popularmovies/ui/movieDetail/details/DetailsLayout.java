package com.figengungor.popularmovies.ui.movieDetail.details;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;

import com.figengungor.popularmovies.R;
import com.figengungor.popularmovies.data.model.MovieDetailResponse;
import com.figengungor.popularmovies.utils.DateUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by figengungor on 3/18/2018.
 */

public class DetailsLayout extends ConstraintLayout {

    @BindView(R.id.releaseDateTv)
    AppCompatTextView releaseDateTv;
    @BindView(R.id.originalTitleTv)
    AppCompatTextView originalTitleTv;
    @BindView(R.id.originalLanguageTv)
    AppCompatTextView originalLanguageTv;
    @BindView(R.id.budgetTv)
    AppCompatTextView budgetTv;
    @BindView(R.id.revenueTv)
    AppCompatTextView revenueTv;

    public DetailsLayout(Context context) {
        super(context);
        init(context);
    }

    public DetailsLayout(Context context, MovieDetailResponse movieDetailResponse) {
        super(context);
        init(context);
        releaseDateTv.setText(DateUtils.getFriendlyReleaseDate(movieDetailResponse.getReleaseDate()));
        originalTitleTv.setText(movieDetailResponse.getOriginalTitle());
        originalLanguageTv.setText(movieDetailResponse.getOriginalLanguage());
        budgetTv.setText(String.valueOf(movieDetailResponse.getBudget()));
        revenueTv.setText(String.valueOf(movieDetailResponse.getRevenue()));
    }

    private void init(Context context) {
        View view = inflate(context, R.layout.layout_details, this);
        ButterKnife.bind(view);
    }

}
