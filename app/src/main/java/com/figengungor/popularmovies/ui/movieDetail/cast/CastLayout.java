package com.figengungor.popularmovies.ui.movieDetail.cast;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.figengungor.popularmovies.R;
import com.figengungor.popularmovies.data.model.Credits;
import com.figengungor.popularmovies.data.model.Similar;
import com.figengungor.popularmovies.ui.movieDetail.similarMovies.SimilarMoviesAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by figengungor on 3/18/2018.
 */

public class CastLayout extends LinearLayout {

    @BindView(R.id.castRv)
    RecyclerView castRv;

    @BindView(R.id.emptyMessageTv)
    AppCompatTextView emptyMessageTv;

    public CastLayout(Context context) {
        super(context);
        init(context);
    }

    public CastLayout(Context context, CastAdapter.ItemListener itemListener, Credits credits) {
        super(context);
        init(context);
        if (credits.getCast() == null || credits.getCast().size() == 0){
            emptyMessageTv.setVisibility(View.VISIBLE);
        } else {
            emptyMessageTv.setVisibility(View.GONE);
            CastAdapter adapter = new CastAdapter(credits.getCast(), context);
            adapter.setItemListener(itemListener);
            castRv.setAdapter(adapter);
            castRv.setNestedScrollingEnabled(false);
        }
    }

    private void init(Context context) {
        View view = inflate(context, R.layout.layout_cast, this);
        ButterKnife.bind(view);
    }

}
