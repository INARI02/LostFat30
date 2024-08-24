package com.nhn.fitness.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.nhn.fitness.data.shared.AppSettings;
import com.nhn.fitness.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Entity(tableName = "day_history")
public class DayHistoryModel {
    @PrimaryKey
    @NonNull
    private long id;
    private Date date;
    private float weight = -1;
    private float height = -1;
    private float waistline = -1;
    private float calories = 0;
    private int exercises = 0;
    @Ignore
    private boolean hasWorkout;

    @Ignore
    public DayHistoryModel(Calendar calendar) {
        this.id = DateUtils.getIdDay(calendar);
        this.date = calendar.getTime();
        this.hasWorkout = false;
    }

    public DayHistoryModel(long id, Date date, float weight, float height, float waistline, float calories, int exercises) {
        this.id = id;
        this.date = date;
        this.weight = weight;
        this.height = height;
        this.waistline = waistline;
        this.calories = calories;
        this.exercises = exercises;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public Calendar getCalendar() {
        Calendar calendar = (Calendar) Calendar.getInstance().clone();
        calendar.setTime(date);
        return calendar;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWaistline() {
        return waistline;
    }

    public void setWaistline(float waistline) {
        this.waistline = waistline;
    }

    public String getDateString() {
        return new SimpleDateFormat("dd").format(date);
    }

    public String getDayString() {
        return new SimpleDateFormat("EEE", new Locale(AppSettings.getInstance().getLanguage())).format(date);
    }

    public boolean isHasWorkout() {
        return calories > 0;
    }

    public float getCalories() {
        return calories;
    }

    public void addCalories(float calories) {
        this.calories += calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public int getExercises() {
        return exercises;
    }

    public void setExercises(int exercises) {
        this.exercises = exercises;
    }

    public void addExercise(int number) {
        this.exercises += number;
    }

    public void setHasWorkout(boolean hasWorkout) {
        this.hasWorkout = hasWorkout;
    }

    public String formatDate() {
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    public int getDayOfMonth() {
        Calendar calendar = (Calendar) Calendar.getInstance().clone();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public long getDayCount() {
        Calendar calendar = (Calendar) Calendar.getInstance().clone();
        calendar.setTime(date);
        return TimeUnit.MILLISECONDS.toDays(calendar.getTime().getTime());
    }
}
