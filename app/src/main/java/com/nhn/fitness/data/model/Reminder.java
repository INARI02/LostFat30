package com.nhn.fitness.data.model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.nhn.fitness.R;
import com.nhn.fitness.utils.AlarmUtils;

import java.util.Calendar;
import java.util.Locale;

@Entity(tableName = "reminder")
public class Reminder implements Comparable<Reminder> {
    @PrimaryKey
    @NonNull
    private int id;
    private String title;
    private int hours;
    private int mins;
    private boolean[] repeats;   // length = 7
    private boolean isEnable;
    private boolean isAdmin;
    private boolean isDisplay;
    private long timestamp;

    public Reminder(@NonNull int id, String title, int hours, int mins, boolean[] repeats, boolean isEnable, boolean isAdmin, boolean isDisplay, long timestamp) {
        this.id = id;
        this.title = title;
        this.hours = hours;
        this.mins = mins;
        this.repeats = repeats;
        this.isEnable = isEnable;
        this.isAdmin = isAdmin;
        this.isDisplay = isDisplay;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMins() {
        return mins;
    }

    public void setMins(int mins) {
        this.mins = mins;
    }

    public boolean[] getRepeats() {
        return repeats;
    }

    public void setRepeats(boolean[] repeats) {
        this.repeats = repeats;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean hasToday() {
        return repeats[Calendar.getInstance(Locale.US).get(Calendar.DAY_OF_WEEK) - 1];
    }

    public String getTimeFormat() {
        return String.format("%02d:%02d", hours, mins);
    }

    public Calendar getCalendarToday() {
        Calendar calendar = (Calendar) Calendar.getInstance().clone();
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, mins);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public Calendar getCalendarTomorrow() {
        Calendar calendar = (Calendar) Calendar.getInstance().clone();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, mins);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public void setAlarm(Context context) {
        Calendar calendar = (Calendar) Calendar.getInstance().clone();
        if (calendar.get(Calendar.HOUR_OF_DAY) > hours) {
            setAlarmTomorrow(context);
        } else if (calendar.get(Calendar.HOUR_OF_DAY) < hours) {
            setAlarmToday(context);
        } else if (calendar.get(Calendar.MINUTE) > mins) {
            setAlarmTomorrow(context);
        } else {
            setAlarmToday(context);
        }
    }

    public boolean isDisplay() {
        return isDisplay;
    }

    public void setDisplay(boolean display) {
        isDisplay = display;
    }

    private void setAlarmTomorrow(Context context) {
        AlarmUtils.setAlarm(context, id, getCalendarTomorrow());
    }

    private void setAlarmToday(Context context) {
        AlarmUtils.setAlarm(context, id, getCalendarToday());
    }

    public void cancelAlarm(Context context) {
        AlarmUtils.cancel(context, id);
    }

    public String getRepeatsString(Context context) {
        StringBuilder builder = new StringBuilder();
        if (repeats[0]) {
            builder.append(", " + context.getResources().getString(R.string.repeat_sun));
        }
        if (repeats[1]) {
            builder.append(", " + context.getResources().getString(R.string.repeat_mon));
        }
        if (repeats[2]) {
            builder.append(", " + context.getResources().getString(R.string.repeat_tue));
        }
        if (repeats[3]) {
            builder.append(", " + context.getResources().getString(R.string.repeat_wed));
        }
        if (repeats[4]) {
            builder.append(", " + context.getResources().getString(R.string.repeat_thu));
        }
        if (repeats[5]) {
            builder.append(", " + context.getResources().getString(R.string.repeat_fri));
        }
        if (repeats[6]) {
            builder.append(", " + context.getResources().getString(R.string.repeat_sat));
        }
        return builder.toString().replaceFirst(", ", "");
    }

    @Override
    public int compareTo(Reminder reminder) {
        if (isAdmin && !reminder.isAdmin) {
            return 1;
        } else if (!isAdmin && reminder.isAdmin) {
            return -1;
        } else if (hours > reminder.hours) {
            return 1;
        } else if (hours < reminder.hours) {
            return -1;
        } else return Integer.compare(mins, reminder.mins);
    }
}
