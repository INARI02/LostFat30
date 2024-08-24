package com.nhn.fitness.utils;

import android.annotation.SuppressLint;

import com.nhn.fitness.data.model.DayHistoryModel;
import com.nhn.fitness.ui.lib.horizontalCalendar.DateModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DateUtils {
    public static List<DayHistoryModel> getYearAroundDate(Calendar calendar) {
        Calendar fromDate = (Calendar) calendar.clone();
        fromDate.add(Calendar.MONTH, -6);
        Calendar toDate = (Calendar) calendar.clone();
        toDate.add(Calendar.MONTH, 6);

        List<DayHistoryModel> list = new ArrayList<>();
        long daysOfYears = TimeUnit.MILLISECONDS.toDays(toDate.getTime().getTime() - fromDate.getTime().getTime());

        for (int i = 0; i < daysOfYears; i++) {
            fromDate.add(Calendar.DAY_OF_YEAR, 1);
            list.add(new DayHistoryModel(fromDate));
        }

//        for (DayHistoryModel dayModel : list) {
//            Log.e("status", dayModel.formatDate());
//        }
//
//        Log.e("status", "length:" + daysOfYears);
//        Log.e("status", new DayHistoryModel(fromDate).formatDate());
//        Log.e("status", new DayHistoryModel(toDate).formatDate());

        return list;
    }

    public static List<DayHistoryModel> getYearFromDate(Calendar calendar) {
        Calendar fromDate = (Calendar) calendar.clone();
        Calendar toDate = (Calendar) calendar.clone();
        toDate.add(Calendar.YEAR, 1);

        List<DayHistoryModel> list = new ArrayList<>();
        long daysOfYears = TimeUnit.MILLISECONDS.toDays(toDate.getTime().getTime() - fromDate.getTime().getTime());

        for (int i = 0; i < daysOfYears; i++) {
            fromDate.add(Calendar.DAY_OF_YEAR, 1);
            list.add(new DayHistoryModel(fromDate));
        }

//        for (DayHistoryModel dayModel : list) {
//            Log.e("status", dayModel.formatDate());
//            Log.e("status", dayModel.getDayCount() + "");
//        }
//
//        Log.e("status", "length:" + daysOfYears);
//        Log.e("status", new DayHistoryModel(fromDate).formatDate());
//        Log.e("status", new DayHistoryModel(toDate).formatDate());

        return list;
    }

    public static List<DayHistoryModel> getYearBeforeDate(Calendar calendar) {
        Calendar fromDate = (Calendar) calendar.clone();
        Calendar toDate = (Calendar) calendar.clone();
        fromDate.add(Calendar.YEAR, -1);

        List<DayHistoryModel> list = new ArrayList<>();
        long daysOfYears = TimeUnit.MILLISECONDS.toDays(toDate.getTime().getTime() - fromDate.getTime().getTime());

        for (int i = 0; i < daysOfYears; i++) {
            fromDate.add(Calendar.DAY_OF_YEAR, 1);
            list.add(new DayHistoryModel(fromDate));
        }

//        for (DayHistoryModel dayModel : list) {
//            Log.e("status", dayModel.formatDate());
//            Log.e("status", dayModel.getDayCount() + "");
//        }
//
//        Log.e("status", "length:" + daysOfYears);
//        Log.e("status", new DayHistoryModel(fromDate).formatDate());
//        Log.e("status", new DayHistoryModel(toDate).formatDate());

        return list;
    }

    public static List<DateModel> generateDateForDialog() {
        Calendar fromDate = (Calendar) Calendar.getInstance().clone();
        Calendar toDate = (Calendar) Calendar.getInstance().clone();
        fromDate.set(Calendar.MONTH, -3);
        toDate.add(Calendar.DAY_OF_YEAR, 5);

        List<DateModel> list = new ArrayList<>();
        long daysOfYears = TimeUnit.MILLISECONDS.toDays(toDate.getTime().getTime() - fromDate.getTime().getTime());

        for (int i = 0; i < daysOfYears; i++) {
            fromDate.add(Calendar.DAY_OF_YEAR, 1);
            list.add(new DateModel(fromDate));
        }

        return list;
    }

    public static List<DateModel> generateDateForDialogAfterDate(Calendar calendar) {
        Calendar fromDate = (Calendar) calendar.clone();
        Calendar toDate = (Calendar) calendar.clone();
        toDate.add(Calendar.MONTH, 6);

        List<DateModel> list = new ArrayList<>();
        long daysOfYears = TimeUnit.MILLISECONDS.toDays(toDate.getTime().getTime() - fromDate.getTime().getTime());

        for (int i = 0; i < daysOfYears; i++) {
            fromDate.add(Calendar.DAY_OF_YEAR, 1);
            list.add(new DateModel(fromDate));
        }

        return list;
    }

    public static List<DateModel> generateDateForDialogBeforeDate(Calendar calendar) {
        Calendar mock = (Calendar) Calendar.getInstance().clone();
        mock.set(Calendar.YEAR, 2017);
        mock.set(Calendar.MONTH, 0);
        mock.set(Calendar.DAY_OF_YEAR, 1);

        Calendar fromDate = (Calendar) calendar.clone();
        Calendar toDate = (Calendar) calendar.clone();
        fromDate.add(Calendar.MONTH, -6);

        List<DateModel> list = new ArrayList<>();
        long daysOfYears = TimeUnit.MILLISECONDS.toDays(toDate.getTime().getTime() - fromDate.getTime().getTime());

        if (getIdDay(mock) > getIdDay(fromDate)) {
            fromDate = mock;
            list.add(new DateModel(fromDate));
        }

        for (int i = 0; i < daysOfYears - 1; i++) {
            fromDate.add(Calendar.DAY_OF_YEAR, 1);
            list.add(new DateModel(fromDate));
        }

        return list;
    }

    public static String convertDayOfMonth(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH) + "";
    }

    public static long getIdNow() {
        return TimeUnit.MILLISECONDS.toDays(Calendar.getInstance().getTimeInMillis());
    }

    public static long getIdDay(Calendar calendar) {
        return TimeUnit.MILLISECONDS.toDays(calendar.getTimeInMillis());
    }

    public static long getBirthday() {
        Calendar calendar = (Calendar) Calendar.getInstance().clone();
        calendar.set(Calendar.YEAR, 1990);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        return DateUtils.getIdDay(calendar);
    }

    public static Calendar convertIdToDate(long id) {
        Calendar calendar = (Calendar) Calendar.getInstance().clone();
        calendar.setTimeInMillis(id * 86400000);
        return calendar;
    }

    @SuppressLint("SimpleDateFormat")
    public static String formatBirthday(long id) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(convertIdToDate(id).getTime());
    }
}
