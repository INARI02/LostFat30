package com.nhn.fitness.utils;

import android.annotation.SuppressLint;

import com.google.android.gms.maps.model.LatLng;
import com.nhn.fitness.data.dto.DailySectionUserDTO;
import com.nhn.fitness.data.dto.DayHistoryDTO;
import com.nhn.fitness.data.dto.SectionHistoryDTO;
import com.nhn.fitness.data.model.DailySectionUser;
import com.nhn.fitness.data.model.DayHistoryModel;
import com.nhn.fitness.data.model.LocationPoint;
import com.nhn.fitness.data.model.SectionHistory;

import java.util.Calendar;

public class DataConverter {
    public static final float EARTH_RADIUS = 6371009.0f;

    public static DayHistoryModel toModel(DayHistoryDTO dto) {
        return new DayHistoryModel(dto.getId(), dto.getDate(), dto.getWeight(),
                dto.getHeight(), dto.getWaistline(), dto.getCalories(), dto.getExercises());
    }

    public static DailySectionUser toModel(DailySectionUserDTO dto) {
        return new DailySectionUser(dto.getId(), dto.getLevel(), dto.getProgress(),
                dto.isLocked(), dto.isRestDay(), dto.isCompleted(), dto.getDay());
    }

    public static SectionHistory toModel(SectionHistoryDTO dto) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dto.getCalendar());
        return new SectionHistory(dto.getId(), calendar, dto.getTitle(),
                dto.getTotalTime(), dto.getCalories(), dto.getSectionId(), dto.getThumb());
    }

    @SuppressLint("DefaultLocale")
    public static String convertSecondsToHHMMSS(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;

        // Định dạng chuỗi thành hh:mm:ss
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }

    public static float getDistanceInMetre(LocationPoint from, LocationPoint to) {
        // Chuyển độ sang radians
        double fromLatRad = Math.toRadians(from.getLatitude());
        double fromLngRad = Math.toRadians(from.getLongitude());
        double toLatRad = Math.toRadians(to.getLatitude());
        double toLngRad = Math.toRadians(to.getLongitude());

        // Tính độ chênh lệch giữa các tọa độ
        double dLat = fromLatRad - toLatRad;
        double dLng = fromLngRad - toLngRad;

        // Sử dụng công thức Haversine
        return (float) ((2 * Math.asin(
                Math.sqrt(
                        Math.pow(Math.sin(dLat / 2), 2.0) +
                                Math.cos(fromLatRad) * Math.cos(toLatRad) * Math.pow(Math.sin(dLng / 2), 2.0)
                )
        )) * EARTH_RADIUS);
    }

    public static double getMetBySpeed(double speed) {
        double met = 0;
        if (speed > 0 && speed <= 3.2) {
            met = 2.8;
        } else if (speed > 3.2 && speed <= 4.0) {
            met = 3.0;
        } else if (speed > 4.0 && speed <= 4.8) {
            met = 3.5;
        } else if (speed > 4.8 && speed <= 5.6) {
            met = 4.3;
        } else if (speed > 5.6 && speed <= 6.4) {
            met = 5.0;
        } else if (speed > 6.4 && speed <= 8) {
            met = 8.3;
        } else if (speed > 8 && speed <= 9.7) {
            met = 9.8;
        } else if (speed > 9.7 && speed <= 11.3) {
            met = 11.0;
        } else if (speed > 11.3 && speed <= 12.9) {
            met = 11.8;
        } else if (speed > 12.9 && speed <= 14.5) {
            met = 12.8;
        } else if (speed > 14.5) {
            met = 14.5;
        }

        return met;
    }
}
