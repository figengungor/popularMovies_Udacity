package com.figengungor.popularmovies.ui.movieDetail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.figengungor.popularmovies.R;
import com.figengungor.popularmovies.data.model.Review;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by figengungor on 3/11/2018.
 */

public class ReviewListAdapter extends
        RecyclerView.Adapter<ReviewListAdapter.VideoViewHolder> {

    private List<Review> items;
    private ItemListener itemListener;

    public ReviewListAdapter(List<Review> items) {
        this.items = items;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false));
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        final Review item = items.get(position);

        holder.contentTv.setText(item.getContent());
        holder.authorTv.setText(item.getAuthor());

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

        @BindView(R.id.contentTv)
        TextView contentTv;
        @BindView(R.id.authorTv)
        TextView authorTv;

        public VideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public interface ItemListener {
        void onItemClicked(Review item);
    }

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

}
