package com.nhn.fitness.ui.interfaces;

public interface DialogResultListener {
    int FIRST_DAY_OF_WEEK = 1;
    int NUMBER_DAYS_WEEKLY = 2;
    int WEIGHT = 3;
    int HEIGHT = 4;
    int REPLACE_WORKOUT = 5;
    int REST_SET = 6;
    int COUNTDOWN = 7;
    int ADD_EXERCISE = 8;
    int WAISTLINE = 9;

    void onResult(int type, Object value);

}
