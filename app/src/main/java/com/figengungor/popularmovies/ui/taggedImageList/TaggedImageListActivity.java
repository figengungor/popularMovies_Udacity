package com.figengungor.popularmovies.ui.taggedImageList;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.figengungor.popularmovies.R;
import com.figengungor.popularmovies.data.model.TaggedImages;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaggedImageListActivity extends AppCompatActivity {

    public static final String EXTRA_TAGGED_IMAGES = "tagged_images";
    private static final String KEY_RECYCLERVIEW_STATE = "recyclerview_state";
    Parcelable recyclerViewState;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.taggedImagesRv)
    RecyclerView taggedImagesRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagged_image_list);
        ButterKnife.bind(this);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        TaggedImages taggedImages = Parcels.unwrap(getIntent().getExtras().getParcelable(EXTRA_TAGGED_IMAGES));
        if (taggedImages != null && taggedImages.getTaggedImages() != null
                && taggedImages.getTaggedImages().size() > 0) {
            TaggedImageAdapter adapter = new TaggedImageAdapter(taggedImages.getTaggedImages());
            StaggeredGridLayoutManager staggeredGridLayoutManager =
                    new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            taggedImagesRv.setLayoutManager(staggeredGridLayoutManager);
            taggedImagesRv.setAdapter(adapter);

            if (savedInstanceState != null) {
                recyclerViewState = savedInstanceState.getParcelable(KEY_RECYCLERVIEW_STATE);
                taggedImagesRv.getLayoutManager().onRestoreInstanceState(recyclerViewState);

            }
        }

        setupUI();
    }

    private void setupUI() {
        setupToolbar();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Parcelable state = taggedImagesRv.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(KEY_RECYCLERVIEW_STATE, state);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
