package com.nhn.fitness.utils;

import com.nhn.fitness.data.dto.DailySectionUserDTO;
import com.nhn.fitness.data.dto.DayHistoryDTO;
import com.nhn.fitness.data.dto.SectionHistoryDTO;
import com.nhn.fitness.data.model.DailySectionUser;
import com.nhn.fitness.data.model.DayHistoryModel;
import com.nhn.fitness.data.model.SectionHistory;

import java.util.Calendar;

public class DataConverter {
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
}
