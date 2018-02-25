
package com.figengungor.popularmovies.data.model;

import com.google.gson.annotations.SerializedName;

public class Status {

    @SerializedName("status_code")
    public Integer statusCode;
    @SerializedName("status_message")
    public String statusMessage;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

}
