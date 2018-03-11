package com.figengungor.popularmovies.ui.movieDetail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.figengungor.popularmovies.R;
import com.figengungor.popularmovies.data.model.Video;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by figengungor on 3/11/2018.
 */

public class VideoListAdapter extends
        RecyclerView.Adapter<VideoListAdapter.VideoViewHolder> {

    private List<Video> items;
    private ItemListener itemListener;

    public VideoListAdapter(List<Video> items) {
        this.items = items;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false));
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        final Video item = items.get(position);
        Picasso.with(holder.itemView.getContext())
                .load(holder.itemView.getContext().getString(R.string.youtube_preview_url, item.getKey()))
                .placeholder(R.color.colorAccent)
                .into(holder.previewIv);

        holder.nameTv.setText(item.getName());

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
