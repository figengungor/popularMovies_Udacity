package com.figengungor.popularmovies.ui.movieDetail.videos;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import com.figengungor.popularmovies.R;
import com.figengungor.popularmovies.data.model.Videos;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by figengungor on 3/18/2018.
 */

public class VideosLayout extends LinearLayout {

    @BindView(R.id.videosRv)
    RecyclerView videosRv;

    @BindView(R.id.emptyMessageTv)
    AppCompatTextView emptyMessageTv;

    public VideosLayout(Context context) {
        super(context);
        init(context);
    }

    public VideosLayout(Context context, VideosAdapter.ItemListener itemListener, Videos videos) {
        super(context);
        init(context);
        if (videos.getVideos() == null || videos.getVideos().size() == 0){
            emptyMessageTv.setVisibility(View.VISIBLE);
        } else {
            emptyMessageTv.setVisibility(View.GONE);
            VideosAdapter videosAdapter = new VideosAdapter(videos.getVideos(), context);
            videosAdapter.setItemListener(itemListener);
            videosRv.setAdapter(videosAdapter);
            videosRv.setNestedScrollingEnabled(false);
        }
    }

    private void init(Context context) {
        View view = inflate(context, R.layout.layout_videos, this);
        ButterKnife.bind(view);
    }

}
