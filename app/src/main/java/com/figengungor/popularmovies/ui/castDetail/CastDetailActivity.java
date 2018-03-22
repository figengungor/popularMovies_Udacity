package com.figengungor.popularmovies.ui.castDetail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.figengungor.popularmovies.R;
import com.figengungor.popularmovies.data.DataManager;
import com.figengungor.popularmovies.data.model.Cast;
import com.figengungor.popularmovies.data.model.CastDetail;
import com.figengungor.popularmovies.data.model.ExternalIds;
import com.figengungor.popularmovies.utils.DateUtils;
import com.figengungor.popularmovies.utils.ErrorUtils;
import com.figengungor.popularmovies.utils.ImageUtils;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CastDetailActivity extends AppCompatActivity {

    private static final String TAG = CastDetailActivity.class.getSimpleName();
    public static final String EXTRA_PERSON_ID = "person_id";
    private Cast cast;
    private CastDetailViewModel viewModel;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.messageLayout)
    ScrollView messageLayout;
    @BindView(R.id.messageTv)
    TextView messageTv;
    @BindView(R.id.retryBtn)
    Button retryBtn;
    @BindView(R.id.loadingPw)
    ProgressWheel loadingPw;
    @BindView(R.id.profileIv)
    ImageView profileIv;
    @BindView(R.id.bioTv)
    TextView bioTv;
    @BindView(R.id.birthDeathdayTv)
    TextView birthDeathdayTv;
    @BindView(R.id.placeOfBirthTv)
    TextView placeOfBirthTv;
    @BindView(R.id.twitterIconIv)
    ImageView twitterIconIv;
    @BindView(R.id.facebookIconIv)
    ImageView facebookIconIv;
    @BindView(R.id.instagramIconIv)
    ImageView instagramIconIv;

    String twitterId, facebookId, instagramId;

    @OnClick(R.id.twitterIconIv)
    void onTwitterIconClicked() {
        openUrl(twitterId);
    }

    @OnClick(R.id.facebookIconIv)
    void onFacebookIconClicked() {
        openUrl(facebookId);
    }

    @OnClick(R.id.instagramIconIv)
    void onInstagramIconClicked() {
        openUrl(instagramId);
    }

    @OnClick(R.id.retryBtn)
    void onRetryBtnClicked() {
        viewModel.getCastDetail(cast.getId());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast_detail);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        cast = Parcels.unwrap(getIntent().getExtras().getParcelable(EXTRA_PERSON_ID));
        ImageUtils.loadImageUrl(cast.getProfilePath(), profileIv, ImageUtils.ImageType.POSTER);

        viewModel = ViewModelProviders.
                of(this, new CastDetailViewModelFactory(getApplication(), DataManager.getInstance(getApplication())))
                .get(CastDetailViewModel.class);

        viewModel.getCastDetail().observe(this, new Observer<CastDetail>() {
            @Override
            public void onChanged(@Nullable CastDetail castDetail) {
                if (castDetail != null) showCastDetail(castDetail);
            }
        });

        viewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                Log.d(TAG, "onChanged: getIsLoading -> " + isLoading);
                showLoadingIndicator(isLoading);
            }
        });

        viewModel.getError().observe(this, new Observer<Throwable>() {
            @Override
            public void onChanged(@Nullable Throwable throwable) {
                Log.d(TAG, "onChanged: getError -> " + throwable);
                if (throwable != null) showError(throwable);
                else messageLayout.setVisibility(View.GONE);

            }
        });

        setupUI();
    }

    private void showCastDetail(CastDetail castDetail) {
        bioTv.setText(castDetail.getBiography());
        String birthDay = castDetail.getBirthday();
        String deathDay = castDetail.getDeathday();
        String birthPlace = castDetail.getPlaceOfBirth();

        ExternalIds externalIds = castDetail.getExternalIds();

        twitterId = externalIds.getTwitterId();
        facebookId = externalIds.getFacebookId();
        instagramId = externalIds.getInstagramId();

        if (birthDay == null && deathDay == null)
            birthDeathdayTv.setVisibility(View.GONE);
        else {
            birthDeathdayTv.setVisibility(View.VISIBLE);
            birthDeathdayTv.setText(getString(R.string.birth_death_day,
                    getFormattedDate(birthDay),
                    getFormattedDate(deathDay)));
        }

        if (birthPlace == null) {
            placeOfBirthTv.setVisibility(View.GONE);
        } else {
            placeOfBirthTv.setVisibility(View.VISIBLE);
            placeOfBirthTv.setText(getString(R.string.place_of_birth, birthPlace));
        }

        if (twitterId == null) {
            twitterIconIv.setVisibility(View.GONE);
        } else {
            twitterIconIv.setVisibility(View.VISIBLE);
            twitterId = getString(R.string.twitter_url, twitterId);
        }
        if (facebookId == null) {
            facebookIconIv.setVisibility(View.GONE);
        } else {
            facebookIconIv.setVisibility(View.VISIBLE);
            facebookId = getString(R.string.facebook_url, facebookId);
        }
        if (instagramId == null) {
            instagramIconIv.setVisibility(View.GONE);
        } else {
            instagramIconIv.setVisibility(View.VISIBLE);
            instagramId = getString(R.string.instagram_url, instagramId);
        }

    }

    private String getFormattedDate(String date) {
        if (date == null) {
            return "";
        } else {
            return DateUtils.getFriendlyReleaseDate(date);
        }
    }

    private void setupUI() {
        setupToolbar();
        loadCastDetail();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(cast.getName());
    }

    private void loadCastDetail() {
        if (viewModel.getCastDetail().getValue() == null) {
            viewModel.getCastDetail(cast.getId());
        }
    }


    private void showLoadingIndicator(Boolean isLoading) {
        if (isLoading) {
            loadingPw.setVisibility(View.VISIBLE);
        } else {
            loadingPw.setVisibility(View.GONE);
        }
    }

    private void showError(Throwable throwable) {
        messageLayout.setVisibility(View.VISIBLE);
        retryBtn.setVisibility(View.VISIBLE);
        messageTv.setText(ErrorUtils.displayFriendlyErrorMessage(this, throwable));
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

    void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        Intent chooser = Intent.createChooser(intent, getString(R.string.social_chooser_title));
        startActivity(chooser);
    }
}
