package com.figengungor.popularmovies.ui.movieDetail.videos;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.figengungor.popularmovies.R;
import com.figengungor.popularmovies.data.model.Video;
import com.figengungor.popularmovies.utils.DisplayUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by figengungor on 3/11/2018.
 */

public class VideosAdapter extends
        RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {

    private List<Video> items;
    private ItemListener itemListener;
    private int ivWidth;

    public VideosAdapter(List<Video> items, Context context) {
        this.items = items;
        int screenWidth = DisplayUtils.getScreenWidth(context);
        int visibleItemCount = context.getResources().getInteger(R.integer.movie_detail_videos_recycler_view_visible_item_count);
        int margins = (visibleItemCount+1) * DisplayUtils.dp2px(context, context.getResources().getInteger(R.integer.movie_detail_horizontal_recycler_view_item_margin));
        int peekWidth = DisplayUtils.dp2px(context, context.getResources().getInteger(R.integer.movie_detail_horizontal_recycler_view_item_peek_width));
        ivWidth = (screenWidth - margins - peekWidth) / visibleItemCount;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false));
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        final Video item = items.get(position);

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) holder.previewIv.getLayoutParams();
        layoutParams.width = ivWidth;
        holder.previewIv.setLayoutParams(layoutParams);

        Picasso.with(holder.itemView.getContext())
                .load(holder.itemView.getContext().getString(R.string.youtube_preview_url, item.getKey()))
                .placeholder(R.color.colorAccent)
                .into(holder.previewIv);

        RelativeLayout.LayoutParams nameLayoutParams = (RelativeLayout.LayoutParams) holder.nameTv.getLayoutParams();
        nameLayoutParams.width = ivWidth;
        holder.nameTv.setLayoutParams(nameLayoutParams);

        holder.nameTv.setText(item.getName());

        holder.typeTv.setText(item.getType());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemListener != null)
                    itemListener.onItemClicked(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.previewIv)
        ImageView previewIv;
        @BindView(R.id.nameTv)
        TextView nameTv;
        @BindView(R.id.typeTv)
        TextView typeTv;

        public VideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ItemListener {
        void onItemClicked(Video item);
    }

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

}
