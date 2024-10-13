package com.nhn.fitness.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class DayHistoryDTO {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("date")
    @Expose
    private Date date;
    @SerializedName("weight")
    @Expose
    private float weight = -1;
    @SerializedName("height")
    @Expose
    private float height = -1;
    @SerializedName("waistline")
    @Expose
    private float waistline = -1;
    @SerializedName("calories")
    @Expose
    private float calories = 0;
    @SerializedName("exercises")
    @Expose
    private int exercises = 0;
    @SerializedName("userId")
    @Expose
    private int userId;

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setWaistline(float waistline) {
        this.waistline = waistline;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public void setExercises(int exercises) {
        this.exercises = exercises;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public float getWeight() {
        return weight;
    }

    public float getHeight() {
        return height;
    }

    public float getWaistline() {
        return waistline;
    }

    public float getCalories() {
        return calories;
    }

    public int getExercises() {
        return exercises;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
