package com.nhn.fitness.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserWorkoutInfoDTO {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("averageSpeed")
    @Expose
    private double averageSpeed;
    @SerializedName("maxSpeed")
    @Expose
    private double maxSpeed;
    @SerializedName("distance")
    @Expose
    private double distance;
    @SerializedName("time")
    @Expose
    private long time;
    @SerializedName("userId")
    @Expose
    private int userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
