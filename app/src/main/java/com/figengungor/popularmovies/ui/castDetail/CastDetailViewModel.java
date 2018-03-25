package com.figengungor.popularmovies.ui.castDetail;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import com.figengungor.popularmovies.data.DataManager;
import com.figengungor.popularmovies.data.model.CastDetail;
import com.figengungor.popularmovies.data.remote.TmdbCallback;

/**
 * Created by figengungor on 3/21/2018.
 */

public class CastDetailViewModel extends AndroidViewModel {

    MutableLiveData<CastDetail> castDetail;
    MutableLiveData<Boolean> isLoading;
    MutableLiveData<Throwable> error;
    DataManager dataManager;

    public CastDetailViewModel(@NonNull Application application, DataManager dataManager) {
        super(application);
        this.dataManager = dataManager;
        castDetail = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        error = new MutableLiveData<>();
    }

    public void getCastDetail(int personId){
        isLoading.setValue(true);
        castDetail.setValue(null);
        error.setValue(null);
        dataManager.getCastDetail(personId, new TmdbCallback<CastDetail>() {
            @Override
            public void onSuccess(CastDetail response) {
                isLoading.setValue(false);
                castDetail.setValue(response);
                error.setValue(null);
            }

            @Override
            public void onFail(Throwable throwable) {
                isLoading.setValue(false);
                error.setValue(throwable);
            }
        });
    }

    public MutableLiveData<CastDetail> getCastDetail() {
        return castDetail;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<Throwable> getError() {
        return error;
    }
}
