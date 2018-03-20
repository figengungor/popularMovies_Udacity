package com.figengungor.popularmovies.ui.movieDetail.cast;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.figengungor.popularmovies.R;
import com.figengungor.popularmovies.data.model.Cast;
import com.figengungor.popularmovies.data.model.Movie;
import com.figengungor.popularmovies.utils.DisplayUtils;
import com.figengungor.popularmovies.utils.ImageUtils;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by figengungor on 3/11/2018.
 */

public class CastAdapter extends
        RecyclerView.Adapter<CastAdapter.SimilarMovieViewHolder> {

    private List<Cast> items;
    private ItemListener itemListener;
    private int ivWidth;

    public CastAdapter(List<Cast> items, Context context) {
        this.items = items;
        int screenWidth = DisplayUtils.getScreenWidth(context);
        int visibleItemCount = context.getResources().getInteger(R.integer.movie_detail_similar_recycler_view_visible_item_count);
        int margins = (visibleItemCount+1) * DisplayUtils.dp2px(context, context.getResources().getInteger(R.integer.movie_detail_horizontal_recycler_view_item_margin));
        int peekWidth = DisplayUtils.dp2px(context, context.getResources().getInteger(R.integer.movie_detail_horizontal_recycler_view_item_peek_width));
        ivWidth = (screenWidth - margins - peekWidth) / visibleItemCount;
    }

    @Override
    public SimilarMovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SimilarMovieViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cast, parent, false));
    }

    @Override
    public void onBindViewHolder(SimilarMovieViewHolder holder, int position) {
        final Cast item = items.get(position);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.profileIv.getLayoutParams();
        layoutParams.width = ivWidth;
        holder.profileIv.setLayoutParams(layoutParams);

        ImageUtils.loadImageUrl(item.getProfilePath(), holder.profileIv, ImageUtils.ImageType.POSTER);

        RelativeLayout.LayoutParams nameLayoutParams = (RelativeLayout.LayoutParams) holder.nameTv.getLayoutParams();
        nameLayoutParams.width = ivWidth;
        holder.nameTv.setLayoutParams(nameLayoutParams);

        holder.nameTv.setText(item.getName());

        RelativeLayout.LayoutParams roleLayoutParams = (RelativeLayout.LayoutParams) holder.roleTv.getLayoutParams();
        roleLayoutParams.width = ivWidth;
        holder.roleTv.setLayoutParams(roleLayoutParams);

        holder.roleTv.setText(item.getCharacter());

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

    public static class SimilarMovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.profileIv)
        ImageView profileIv;
        @BindView(R.id.nameTv)
        TextView nameTv;
        @BindView(R.id.roleTv)
        TextView roleTv;

        public SimilarMovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ItemListener {
        void onItemClicked(Cast item);
    }

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

}
