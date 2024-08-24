package com.nhn.fitness.data.room;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nhn.fitness.data.model.MultiLanguageModel;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class DateConverters {

    @TypeConverter
    public Date fromTimestamp(long value) {
        return new Date(value);
    }

    @TypeConverter
    public long dateToTimestamp(Date date) {
        return date.getTime();
    }

    @TypeConverter
    public Calendar calendarFromTimestamp(long value) {
        Calendar calendar = (Calendar) Calendar.getInstance().clone();
        calendar.setTime(new Date(value));
        return calendar;
    }

    @TypeConverter
    public long calendarToTimestamp(Calendar date) {
        return date.getTime().getTime();
    }

    @TypeConverter
    public boolean[] weeklyFromString(String json) {
        if (json == null) {
            return new boolean[7];
        }
        Gson gson = new Gson();
        Type type = new TypeToken<boolean[]>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public String weeklyToString(boolean[] array) {
        if (array == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<boolean[]>() {
        }.getType();
        return gson.toJson(array, type);
    }

    @TypeConverter
    public String multiLanguageToString(MultiLanguageModel languageModel) {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        return gson.toJson(languageModel.getHashMap(), type);
    }

    @TypeConverter
    public MultiLanguageModel jsonToMultiLanguage(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        return new MultiLanguageModel(gson.fromJson(json, type));
    }

}
