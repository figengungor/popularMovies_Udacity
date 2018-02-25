package com.figengungor.popularmovies.ui.movieList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.figengungor.popularmovies.R;
import com.figengungor.popularmovies.data.model.Movie;
import com.figengungor.popularmovies.utils.ImageUtils;

import java.util.List;

/**
 * Created by figengungor on 2/20/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    List<Movie> movies;
    ItemListener itemListener;

    interface ItemListener {
        void onItemClick(Movie movie);
    }

    public MovieAdapter(List<Movie> movies, ItemListener itemListener) {
        this.movies = movies;
        this.itemListener = itemListener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false));
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bindItem(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView posterIv;

        public MovieViewHolder(View itemView) {
            super(itemView);
            posterIv = itemView.findViewById(R.id.posterIv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.onItemClick(movies.get(getAdapterPosition()));
                }
            });
        }

        public void bindItem(Movie movie) {
            ImageUtils.loadImageUrl(movie.getPosterPath(), posterIv, ImageUtils.ImageType.POSTER);
        }
    }

}
