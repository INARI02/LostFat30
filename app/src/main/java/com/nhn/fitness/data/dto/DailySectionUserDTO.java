package com.nhn.fitness.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DailySectionUserDTO {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("level")
    @Expose
    private int level;
    @SerializedName("day")
    @Expose
    private int day;
    @SerializedName("progress")
    @Expose
    private float progress;
    @SerializedName("locked")
    @Expose
    private boolean locked;
    @SerializedName("isRestDay")
    @Expose
    private boolean isRestDay;
    @SerializedName("completed")
    @Expose
    private boolean completed;
    @SerializedName("userId")
    @Expose
    private int userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isRestDay() {
        return isRestDay;
    }

    public void setRestDay(boolean restDay) {
        isRestDay = restDay;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
