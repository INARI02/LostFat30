package com.nhn.fitness.ui.lib.horizontalCalendar;

public interface HorizontalCalendarListener {
    void updateMonthOnScroll(DateModel selectedDate);

    void newDateSelected(DateModel selectedDate, int pos);
}
