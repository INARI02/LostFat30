package com.nhn.fitness.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "history")
public class History {
    @PrimaryKey
    @NonNull
    private String id;
    private String title;
    private int time; // seconds. Total time during workout in the section
    private long date;
    private int calories;  // Total calories during workout in the section
    private String icon;

    public History(String id, String title, int time, long date, int calories, String icon) {
        this.id = id;
        this.title = title;
        this.time = time;
        this.date = date;
        this.calories = calories;
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
