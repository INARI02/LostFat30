package com.applikeysolutions.cosmocalendar.view.delegate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applikeysolutions.cosmocalendar.adapter.MonthAdapter;
import com.applikeysolutions.cosmocalendar.adapter.viewholder.OtherDayHolder;
import com.applikeysolutions.cosmocalendar.model.Day;
import com.applikeysolutions.cosmocalendar.selection.HighLightManager;
import com.applikeysolutions.cosmocalendar.view.CalendarView;
import com.applikeysolutions.customizablecalendar.R;

public class OtherDayDelegate extends BaseDelegate {

    private CalendarView calendarView;
    private MonthAdapter monthAdapter;

    public OtherDayDelegate(CalendarView calendarView,  MonthAdapter monthAdapter) {
        this.calendarView = calendarView;
        this.monthAdapter = monthAdapter;
    }

    public OtherDayHolder onCreateDayHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_day, parent, false);
        return new OtherDayHolder(view, calendarView);
    }

    public void onBindDayHolder(Day day, OtherDayHolder holder, int position) {
        holder.bind(day, monthAdapter.getHighLightManager());
    }
}
