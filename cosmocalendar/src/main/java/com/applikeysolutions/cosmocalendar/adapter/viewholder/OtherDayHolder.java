package com.applikeysolutions.cosmocalendar.adapter.viewholder;

import android.graphics.Color;
import android.view.View;

import com.applikeysolutions.cosmocalendar.model.Day;
import com.applikeysolutions.cosmocalendar.selection.HighLightManager;
import com.applikeysolutions.cosmocalendar.view.CalendarView;
import com.applikeysolutions.cosmocalendar.view.customviews.CircleAnimationTextView;
import com.applikeysolutions.customizablecalendar.R;

public class OtherDayHolder extends BaseDayHolder {
    private CircleAnimationTextView ctvDay;

    public OtherDayHolder(View itemView, CalendarView calendarView) {
        super(itemView, calendarView);
        ctvDay = itemView.findViewById(R.id.tv_day_number);
    }

    public void bind(Day day, HighLightManager highLightManager) {
        if (highLightManager.isDayHighlight(day)) {
            day.setHighlight(true);
            ctvDay.setTextColor(Color.WHITE);
        } else {
            ctvDay.setTextColor(calendarView.getOtherDayTextColor());
        }
        ctvDay.setDay(day);
        ctvDay.setText(String.valueOf(day.getDayNumber()));
    }
}
