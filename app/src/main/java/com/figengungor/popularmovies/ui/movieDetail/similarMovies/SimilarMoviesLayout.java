package com.figengungor.popularmovies.ui.movieDetail.similarMovies;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.figengungor.popularmovies.R;
import com.figengungor.popularmovies.data.model.Similar;
import com.figengungor.popularmovies.data.model.Videos;
import com.figengungor.popularmovies.ui.movieDetail.videos.VideosAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by figengungor on 3/18/2018.
 */

public class SimilarMoviesLayout extends LinearLayout {

    @BindView(R.id.similarMoviesRv)
    RecyclerView similarMoviesRv;

    @BindView(R.id.emptyMessageTv)
    AppCompatTextView emptyMessageTv;

    public SimilarMoviesLayout(Context context) {
        super(context);
        init(context);
    }

    public SimilarMoviesLayout(Context context, Similar similar) {
        super(context);
        init(context);
        if (similar.getMovies() == null || similar.getMovies().size() == 0){
            emptyMessageTv.setVisibility(View.VISIBLE);
        } else {
            emptyMessageTv.setVisibility(View.GONE);
            SimilarMoviesAdapter adapter = new SimilarMoviesAdapter(similar.getMovies(), context);
            similarMoviesRv.setAdapter(adapter);
            similarMoviesRv.setNestedScrollingEnabled(false);
        }
    }

    private void init(Context context) {
        View view = inflate(context, R.layout.layout_similar_movies, this);
        ButterKnife.bind(view);
    }

}
