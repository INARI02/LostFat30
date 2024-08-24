package com.nhn.fitness.ui.lib.horizontalCalendar;

import com.nhn.fitness.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class DateModel {
    private Calendar calendar;
    private boolean isToday;
    private SimpleDateFormat dayFormat = new SimpleDateFormat("EEE");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
    private boolean isEnable;

    public DateModel(Calendar calendar) {
        this.calendar = (Calendar) calendar.clone();
        isToday = Calendar.getInstance().get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR) &&
                Calendar.getInstance().get(Calendar.YEAR) == calendar.get(Calendar.YEAR);
        isEnable = DateUtils.getIdDay(calendar) <= DateUtils.getIdNow();
    }

    public boolean isEnable() {
        return isEnable;
    }

    public String getDay() {
        return dayFormat.format(calendar.getTime());
    }

    public String getDate() {
        return dateFormat.format(calendar.getTime());
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public long getDayCount() {
        return TimeUnit.MILLISECONDS.toDays(calendar.getTime().getTime());
    }

    public boolean isToday() {
        return isToday;
    }

    @Override
    public boolean equals(Object obj) {
        return ((DateModel) obj).getDayCount() == getDayCount();
    }
}
