package com.nhn.fitness.data.model;

import com.google.gson.annotations.SerializedName;

public class FeedBackResponse {

    @SerializedName("success")
    private String success;
    @SerializedName("com/nhn/fitness/data")
    private String data;
    @SerializedName("message")
    private String message;

    public FeedBackResponse(String success, String data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
