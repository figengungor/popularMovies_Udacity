package com.figengungor.popularmovies.ui.movieList;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.figengungor.popularmovies.R;
import java.util.List;

/**
 * Created by figengungor on 3/21/2018.
 */

public class MovieListTypeAdapter extends ArrayAdapter<String> {

    private LayoutInflater layoutInflater;

    public MovieListTypeAdapter(@NonNull Context context, @NonNull List<String> objects) {
        super(context, 0, objects);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_spinner_drop_movie_list_type, parent, false);
            holder = new ViewHolder();
            holder.typeTv = convertView.findViewById(R.id.typeTv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String type = getItem(position);
        holder.typeTv.setText(type);
        return convertView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String movieListType = getItem(position);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_spinner_movie_list_type, null);
        }
        TextView typeTv = convertView.findViewById(R.id.typeTv);
        typeTv.setText(movieListType);

        return convertView;
    }

    private static class ViewHolder {
        public TextView typeTv;
    }
}
