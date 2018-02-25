package com.figengungor.popularmovies.data.network;

import com.figengungor.popularmovies.data.model.Status;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by figengungor on 2/20/2018.
 */

public abstract class TmdbCallback<T> implements Callback<T> {

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            onSuccess(response.body());
        } else {
            Gson gson = new Gson();
            Status status = gson.fromJson(response.errorBody().charStream(), Status.class);
            onFail(new Throwable(status.getStatusMessage()));
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onFail(t);
    }

    public abstract void onSuccess(T response);

    public abstract void onFail(Throwable throwable);

}
