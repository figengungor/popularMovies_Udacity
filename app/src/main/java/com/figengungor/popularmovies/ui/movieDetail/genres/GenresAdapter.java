package com.figengungor.popularmovies.ui.movieDetail.genres;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.figengungor.popularmovies.R;
import com.figengungor.popularmovies.data.model.Genre;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by figengungor on 3/11/2018.
 */

public class GenresAdapter extends
        RecyclerView.Adapter<GenresAdapter.GenreViewHolder> {

    private List<Genre> items;
    private ItemListener itemListener;

    public GenresAdapter(List<Genre> items) {
        this.items = items;
    }

    @Override
    public GenreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GenreViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genre, parent, false));
    }

    @Override
    public void onBindViewHolder(GenreViewHolder holder, int position) {
        final Genre item = items.get(position);

        holder.genreTv.setText("#"+item.getName());

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

    public static class GenreViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.genreTv)
        TextView genreTv;

        public GenreViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ItemListener {
        void onItemClicked(Genre item);
    }

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

}
