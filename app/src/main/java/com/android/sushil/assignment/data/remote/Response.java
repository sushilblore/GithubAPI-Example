package com.android.sushil.assignment.data.remote;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sushiljha on 14/08/2017.
 */

public class Response {
    @SerializedName("status")
    public String status;

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @SuppressWarnings({"unused", "used by Retrofit"})
    public Response() {
    }

    public Response(String status) {
        this.status = status;
    }
}

