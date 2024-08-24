package com.nhn.fitness.data.shared;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nhn.fitness.data.model.ContainerVoiceEngine;
import com.nhn.fitness.ui.base.BaseApplication;
import com.nhn.fitness.utils.Constants;
import com.nhn.fitness.utils.DateUtils;

import java.lang.reflect.Type;
import java.util.Calendar;

public class AppSettings {
    private static final String FIRST_OPEN = "first_open";
    private static final String LAST_VERSION = "last_version";
    private static final String NUMBER_DAYS_OF_WEEKLY = "number_days_of_weekly";
    private static final String FIRST_DAY_OF_WEEK = "first_day_of_week";
    private static final String WEIGHT_DEFAULT = "weight_default";
    private static final String WAISTLINE_DEFAULT = "weight_default";
    private static final String HEIGHT_DEFAULT = "height_default";
    private static final String UNIT_TYPE = "unit_type";
    private static final String BIRTH_DAY = "birthday";
    private static final String REST_SET = "rest_set";
    private static final String COUNTDOWN = "countdown_time";
    private static final String SOUND_ENABLE = "sound_enable";
    private static final String VOICE_GUIDE = "voice_guide";
    private static final String COACH_TIPS = "coach_tips";
    private static final String AUDIO_BACKGROUND = "audio_background";
    private static final String ENGINE = "engine";
    private static final String ENGINE_LANGUAGE = "engine_language";
    private static final String LANGUAGE = "language";
    private static final String GENDER = "gender";
    private static final String LEVEL = "level";
    private static final String DB_VERSION = "db_version";


    private static AppSettings instance;
    private SharedPreferences sharedPreferences;

    private AppSettings() {
        sharedPreferences = BaseApplication.getInstance().getSharedPreferences("app_settings", Context.MODE_PRIVATE);
    }

    public static AppSettings getInstance() {
        if (instance == null) {
            instance = new AppSettings();
        }
        return instance;
    }

    public boolean isFirstOpen() {
        return sharedPreferences.getBoolean(FIRST_OPEN, true);
    }

    public void markedFirstOpen() {
        sharedPreferences.edit().putBoolean(FIRST_OPEN, false).apply();
    }

    public int getLastVersion() {
        return sharedPreferences.getInt(LAST_VERSION, 0);
    }

    public void setLastVersion(int version) {
        sharedPreferences.edit().putInt(LAST_VERSION, version).apply();
    }

    public void setNumberDaysOfWeekly(int days) {
        sharedPreferences.edit().putInt(NUMBER_DAYS_OF_WEEKLY, days).apply();
    }

    public int getNumberDaysOfWeekly() {
        return sharedPreferences.getInt(NUMBER_DAYS_OF_WEEKLY, 3);
    }

    public void setFirstDayOfWeek(int firstDayOfWeek) {
        sharedPreferences.edit().putInt(FIRST_DAY_OF_WEEK, firstDayOfWeek).apply();
    }

    public int getFirstDayOfWeek() {
        return sharedPreferences.getInt(FIRST_DAY_OF_WEEK, Calendar.MONDAY);
    }

    public void setHeightDefault(float value) {
        sharedPreferences.edit().putFloat(HEIGHT_DEFAULT, value).apply();
    }

    public float getHeightDefault() {
        return sharedPreferences.getFloat(HEIGHT_DEFAULT, Constants.HEIGHT);
    }

    public void setWeightDefault(float value) {
        sharedPreferences.edit().putFloat(WEIGHT_DEFAULT, value).apply();
    }

    public float getWeightDefault() {
        return sharedPreferences.getFloat(WEIGHT_DEFAULT, Constants.WEIGHT);
    }

    public void setWaistlineDefault(float value) {
        sharedPreferences.edit().putFloat(WAISTLINE_DEFAULT, value).apply();
    }

    public float getWaistlineDefault() {
        return sharedPreferences.getFloat(WAISTLINE_DEFAULT, Constants.WAISTLINE);
    }

    public void setUnitType(int value) {
        sharedPreferences.edit().putInt(UNIT_TYPE, value).apply();
    }

    public int getUnitType() {
        return sharedPreferences.getInt(UNIT_TYPE, Constants.UNIT_TYPE);
    }

    public void setBirthday(long value) {
        sharedPreferences.edit().putLong(BIRTH_DAY, value).apply();
    }

    public long getBirthday() {
        return sharedPreferences.getLong(BIRTH_DAY, DateUtils.getBirthday());
    }

    public int getRestSet() {
        return sharedPreferences.getInt(REST_SET, 30);
    }

    public void setRestSet(int value) {
        sharedPreferences.edit().putInt(REST_SET, value).apply();
    }

    public int getCountDown() {
        return sharedPreferences.getInt(COUNTDOWN, 15);
    }

    public void setCountDown(int value) {
        sharedPreferences.edit().putInt(COUNTDOWN, value).apply();
    }

    public void setSound(boolean enable) {
        sharedPreferences.edit().putBoolean(SOUND_ENABLE, enable).apply();
    }

    public void setSoundCoachTips(boolean enable) {
        sharedPreferences.edit().putBoolean(COACH_TIPS, enable).apply();
    }

    public void setSoundVoice(boolean enable) {
        sharedPreferences.edit().putBoolean(VOICE_GUIDE, enable).apply();
    }

    public boolean getSound() {
        return sharedPreferences.getBoolean(SOUND_ENABLE, true);
    }

    public boolean getSoundCoachTips() {
        return sharedPreferences.getBoolean(COACH_TIPS, true);
    }

    public boolean getSoundVoice() {
        return sharedPreferences.getBoolean(VOICE_GUIDE, true);
    }

    public void setEngineDefault(ContainerVoiceEngine engineDefault) {
        Gson gson = new Gson();
        Type type = new TypeToken<ContainerVoiceEngine>() {
        }.getType();
        sharedPreferences.edit().putString(ENGINE, gson.toJson(engineDefault, type)).apply();
    }

    public ContainerVoiceEngine getEngineDefault() {
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<ContainerVoiceEngine>() {
            }.getType();
            String json = sharedPreferences.getString(ENGINE, null);
            if (json == null || json.isEmpty()) {
                return null;
            } else {
                return gson.fromJson(json, type);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getEngineLanguage() {
        return sharedPreferences.getString(ENGINE_LANGUAGE, "");
    }

    public void setEngineLanguage(String language) {
        sharedPreferences.edit().putString(ENGINE_LANGUAGE, language).apply();
    }

    public boolean isPlayAudioBackground() {
        return sharedPreferences.getBoolean(AUDIO_BACKGROUND, true);
    }

    public void setPlayAudioBackground(boolean value) {
        sharedPreferences.edit().putBoolean(AUDIO_BACKGROUND, value).apply();
    }

    public void setLanguage(String code) {
        sharedPreferences.edit().putString(LANGUAGE, code).apply();
    }

    public String getLanguage() {
        return sharedPreferences.getString(LANGUAGE, "en");
    }

    public int getGender() {
        return sharedPreferences.getInt(GENDER, Constants.GENDER);
    }

    public void setGender(int gender) {
        sharedPreferences.edit().putInt(GENDER, gender).apply();
    }

    public int getLevel() {
        return sharedPreferences.getInt(LEVEL, 1);
    }

    public void setLevel(int level) {
        sharedPreferences.edit().putInt(LEVEL, level).apply();
    }

    public void clearAll() {
        sharedPreferences.edit().clear().apply();
    }

    public int getDBVersion() {
        return sharedPreferences.getInt(DB_VERSION, 0);
    }

    public void setDbVersion(int version) {
        sharedPreferences.edit().putInt(DB_VERSION, version).apply();
    }
}
