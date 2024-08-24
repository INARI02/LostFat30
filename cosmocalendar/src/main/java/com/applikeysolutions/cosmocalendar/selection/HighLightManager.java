package com.applikeysolutions.cosmocalendar.selection;

import androidx.annotation.NonNull;

import com.applikeysolutions.cosmocalendar.model.Day;
import com.applikeysolutions.cosmocalendar.selection.criteria.BaseCriteria;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class HighLightManager {
    protected OnDayHighLightListener onDayHighLightListener;
    private final List<Day> days = new ArrayList<>();
    protected List<BaseCriteria> criteriaList;

    public HighLightManager(OnDayHighLightListener onDayHighLightListener) {
        this.onDayHighLightListener = onDayHighLightListener;
    }

    public HighLightManager(BaseCriteria criteria, OnDayHighLightListener onDayHighLightListener) {
        this(new ArrayList<>(Collections.singleton(criteria)), onDayHighLightListener);
    }

    public HighLightManager(List<BaseCriteria> criteriaList, OnDayHighLightListener onDayHighLightListener) {
        this.criteriaList = criteriaList;
        this.onDayHighLightListener = onDayHighLightListener;
    }

    public void addDay(Day day) {
        days.add(day);
        onDayHighLightListener.onDateHighLighted();
    }

    public void addDay(List<Day> days) {
        this.days.addAll(days);
        onDayHighLightListener.onDateHighLighted();
    }

    public boolean isDayHighlight(@NonNull Day day) {
        return days.contains(day) || isDaySelectedByCriteria(day);
    }

    public void clearHighlights() {
        days.clear();
    }

    public void removeDay(Day day) {
        days.remove(day);
        onDayHighLightListener.onDateHighLighted();
    }

    public void setCriteriaList(List<BaseCriteria> criteriaList) {
        this.criteriaList = new ArrayList<>(criteriaList);
        notifyCriteriaUpdates();
    }

    public void clearCriteriaList() {
        if (criteriaList != null) {
            criteriaList.clear();
        }
        notifyCriteriaUpdates();
    }

    public void addCriteriaList(List<BaseCriteria> criteriaList) {
        if (this.criteriaList != null) {
            this.criteriaList.addAll(criteriaList);
        } else {
            setCriteriaList(criteriaList);
        }
        notifyCriteriaUpdates();
    }

    public void addCriteria(BaseCriteria criteria) {
        if (criteriaList == null) {
            criteriaList = new ArrayList<>();
        }
        criteriaList.add(criteria);
        notifyCriteriaUpdates();
    }

    public void removeCriteria(BaseCriteria criteria) {
        if (criteriaList != null) {
            criteriaList.remove(criteria);
        }
        notifyCriteriaUpdates();
    }

    public void removeCriteriaList(final List<BaseCriteria> listToDelete) {
        if (criteriaList != null) {
            criteriaList.removeAll(listToDelete);
        }
        notifyCriteriaUpdates();
    }

    private void notifyCriteriaUpdates() {
        if (onDayHighLightListener != null) {
            onDayHighLightListener.onDateHighLighted();
        }
    }

    public boolean hasCriteria() {
        return criteriaList != null && !criteriaList.isEmpty();
    }

    public boolean isDaySelectedByCriteria(Day day) {
        if (hasCriteria()) {
            for (BaseCriteria criteria : criteriaList) {
                if (criteria.isCriteriaPassed(day)) {
                    return true;
                }
            }
        }
        return false;
    }
}
