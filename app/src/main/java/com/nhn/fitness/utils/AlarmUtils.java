package com.nhn.fitness.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.AlarmManagerCompat;

import com.nhn.fitness.service.AlarmReceiver;

import java.util.Calendar;

public class AlarmUtils {

    public static final String ACTION_AUTO_START_ALARM = "com.app.action.alarmmanager";

    public static void cancel(Context context, int id) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(ACTION_AUTO_START_ALARM);
        PendingIntent sender = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    public static void setAlarm(Context context, int id, Calendar calendar) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(ACTION_AUTO_START_ALARM);
        intent.putExtra("id", id);
        PendingIntent sender = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(sender);
        AlarmManagerCompat.setExactAndAllowWhileIdle(alarm, AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
    }
}

