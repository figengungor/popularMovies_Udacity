package com.figengungor.popularmovies.ui.movieDetail.genres;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.figengungor.popularmovies.R;
import com.figengungor.popularmovies.data.model.Genre;
import com.figengungor.popularmovies.data.model.Videos;
import com.figengungor.popularmovies.ui.movieDetail.videos.VideosAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by figengungor on 3/18/2018.
 */

public class GenresLayout extends LinearLayout {

    @BindView(R.id.genresRv)
    RecyclerView genresRv;

    @BindView(R.id.emptyMessageTv)
    AppCompatTextView emptyMessageTv;

    public GenresLayout(Context context) {
        super(context);
        init(context);
    }

    public GenresLayout(Context context, List<Genre> genres) {
        super(context);
        init(context);
        if (genres== null || genres.size() == 0){
            emptyMessageTv.setVisibility(View.VISIBLE);
        } else {
            emptyMessageTv.setVisibility(View.GONE);
            GenresAdapter adapter = new GenresAdapter(genres);
            genresRv.setAdapter(adapter);
            genresRv.setNestedScrollingEnabled(false);
        }
    }

    private void init(Context context) {
        View view = inflate(context, R.layout.layout_genres, this);
        ButterKnife.bind(view);
    }

}
