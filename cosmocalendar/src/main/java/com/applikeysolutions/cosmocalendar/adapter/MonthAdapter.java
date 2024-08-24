package com.applikeysolutions.cosmocalendar.adapter;

import android.util.Log;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.applikeysolutions.cosmocalendar.adapter.viewholder.MonthHolder;
import com.applikeysolutions.cosmocalendar.model.Day;
import com.applikeysolutions.cosmocalendar.model.Month;
import com.applikeysolutions.cosmocalendar.selection.BaseSelectionManager;
import com.applikeysolutions.cosmocalendar.selection.HighLightManager;
import com.applikeysolutions.cosmocalendar.settings.lists.DisabledDaysCriteria;
import com.applikeysolutions.cosmocalendar.utils.CalendarUtils;
import com.applikeysolutions.cosmocalendar.utils.DayFlag;
import com.applikeysolutions.cosmocalendar.view.CalendarView;
import com.applikeysolutions.cosmocalendar.view.ItemViewType;
import com.applikeysolutions.cosmocalendar.view.delegate.DayDelegate;
import com.applikeysolutions.cosmocalendar.view.delegate.DayOfWeekDelegate;
import com.applikeysolutions.cosmocalendar.view.delegate.MonthDelegate;
import com.applikeysolutions.cosmocalendar.view.delegate.OtherDayDelegate;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

public class MonthAdapter extends RecyclerView.Adapter<MonthHolder> {

    private final List<Month> months;

    private MonthDelegate monthDelegate;

    private CalendarView calendarView;
    private BaseSelectionManager selectionManager;
    private HighLightManager highLightManager;
    private DaysAdapter daysAdapter;

    private MonthAdapter(List<Month> months,
                         MonthDelegate monthDelegate,
                         CalendarView calendarView,
                         BaseSelectionManager selectionManager,
                         HighLightManager highlightManager
    ) {
        setHasStableIds(true);
        this.months = months;
        this.monthDelegate = monthDelegate;
        this.calendarView = calendarView;
        this.selectionManager = selectionManager;
        this.highLightManager = highlightManager;
    }

    public void setSelectionManager(BaseSelectionManager selectionManager) {
        this.selectionManager = selectionManager;
    }

    public BaseSelectionManager getSelectionManager() {
        return selectionManager;
    }

    public HighLightManager getHighLightManager() {
        return highLightManager;
    }

    @Override
    public MonthHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        daysAdapter = new DaysAdapter.DaysAdapterBuilder()
                .setDayOfWeekDelegate(new DayOfWeekDelegate(calendarView))
                .setOtherDayDelegate(new OtherDayDelegate(calendarView, this))
                .setDayDelegate(new DayDelegate(calendarView, this))
                .setCalendarView(calendarView)
                .createDaysAdapter();
        return monthDelegate.onCreateMonthHolder(daysAdapter, parent, viewType);
    }

    @Override
    public void onBindViewHolder(MonthHolder holder, int position) {
        final Month month = months.get(position);
        monthDelegate.onBindMonthHolder(month, holder, position);
    }

    @Override
    public int getItemCount() {
        return months.size();
    }

    @Override
    public int getItemViewType(int position) {
        return ItemViewType.MONTH;
    }

    @Override
    public long getItemId(int position) {
        return months.get(position).getFirstDay().getCalendar().getTimeInMillis();
    }

    public List<Month> getData() {
        return months;
    }

    public static class MonthAdapterBuilder {

        private List<Month> months;
        private MonthDelegate monthDelegate;
        private CalendarView calendarView;
        private BaseSelectionManager selectionManager;
        private HighLightManager highLightManager;

        public MonthAdapterBuilder setMonths(List<Month> months) {
            this.months = months;
            return this;
        }

        public MonthAdapterBuilder setMonthDelegate(MonthDelegate monthHolderDelegate) {
            this.monthDelegate = monthHolderDelegate;
            return this;
        }

        public MonthAdapterBuilder setCalendarView(CalendarView calendarView) {
            this.calendarView = calendarView;
            return this;
        }

        public MonthAdapterBuilder setSelectionManager(BaseSelectionManager selectionManager) {
            this.selectionManager = selectionManager;
            return this;
        }

        public MonthAdapterBuilder setHighlightManager(HighLightManager highlightManager) {
            this.highLightManager = highlightManager;
            return this;
        }

        public MonthAdapter createMonthAdapter() {
            return new MonthAdapter(months,
                    monthDelegate,
                    calendarView,
                    selectionManager,
                    highLightManager);
        }
    }

    public void setWeekendDays(Set<Long> weekendDays) {
        setDaysAccordingToSet(weekendDays, DayFlag.WEEKEND);
    }

    public void setDisabledDays(Set<Long> disabledDays) {
        setDaysAccordingToSet(disabledDays, DayFlag.DISABLED);
    }

    public void setConnectedCalendarDays(Set<Long> connectedCalendarDays) {
        setDaysAccordingToSet(connectedCalendarDays, DayFlag.FROM_CONNECTED_CALENDAR);
    }

    public void setDisabledDaysCriteria(DisabledDaysCriteria criteria) {
        for (Month month : months) {
            for (Day day : month.getDays()) {
                if (!day.isDisabled()) {
                    day.setDisabled(CalendarUtils.isDayDisabledByCriteria(day, criteria));
                }
            }
        }
        notifyDataSetChanged();
    }

    private void setDaysAccordingToSet(Set<Long> days, DayFlag dayFlag) {
        if (days != null && !days.isEmpty()) {
            for (Month month : months) {
                for (Day day : month.getDays()) {
                    switch (dayFlag) {
                        case WEEKEND:
                            day.setWeekend(days.contains(day.getCalendar().get(Calendar.DAY_OF_WEEK)));
                            break;

                        case DISABLED:
                            day.setDisabled(CalendarUtils.isDayInSet(day, days));
                            break;

                        case FROM_CONNECTED_CALENDAR:
                            day.setFromConnectedCalendar(CalendarUtils.isDayInSet(day, days));
                            break;
                    }
                }
            }
            notifyDataSetChanged();
        }
    }
}
