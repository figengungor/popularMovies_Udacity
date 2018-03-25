package com.figengungor.popularmovies.ui.taggedImageList;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.figengungor.popularmovies.R;
import com.figengungor.popularmovies.data.model.TaggedImage;
import com.figengungor.popularmovies.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by figengungor on 3/24/2018.
 */

public class TaggedImageAdapter extends RecyclerView.Adapter<TaggedImageAdapter.TaggedImageViewHolder> {

    List<TaggedImage> items = new ArrayList<>();
    ConstraintSet set = new ConstraintSet();

    public TaggedImageAdapter(List<TaggedImage> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public TaggedImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaggedImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tagged_image, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaggedImageViewHolder holder, int position) {
        TaggedImage item = items.get(position);
        String name;
        if (item.getMediaType().equalsIgnoreCase("tv")) {
            name = item.getMedia().getName();
        } else {
            name = item.getMedia().getTitle();
        }

        holder.nameTv.setText(name);
        holder.taggedIv.setContentDescription(holder.itemView.getContext().getString(R.string.a11y_tagged_image, name));

        ImageUtils.loadImageUrl(item.getFilePath(), holder.taggedIv, ImageUtils.ImageType.POSTER);

        String imageRatio = String.format(Locale.getDefault(),"%d:%d", item.getWidth(), item.getHeight());
        set.clone(holder.constraintLayout);
        set.setDimensionRatio(holder.taggedIv.getId(), imageRatio);
        set.applyTo(holder.constraintLayout);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class TaggedImageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.taggedIv)
        ImageView taggedIv;
        @BindView(R.id.nameTv)
        TextView nameTv;
        @BindView(R.id.constraintLayout)
        ConstraintLayout constraintLayout;

        public TaggedImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
