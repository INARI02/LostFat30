package com.nhn.fitness.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

public class SectionHistoryDTO {
    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("calendar")
    @Expose
    private long calendar;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("totalTime")
    @Expose
    private int totalTime;
    @SerializedName("calories")
    @Expose
    private float calories;
    @SerializedName("sectionId")
    @Expose
    private String sectionId;
    @SerializedName("thumb")
    @Expose
    private String thumb;
    @SerializedName("userId")
    @Expose
    private int userId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCalendar() {
        return calendar;
    }

    public void setCalendar(long calendar) {
        this.calendar = calendar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
