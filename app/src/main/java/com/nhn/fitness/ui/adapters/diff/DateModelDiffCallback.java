package com.nhn.fitness.ui.adapters.diff;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.nhn.fitness.ui.lib.horizontalCalendar.DateModel;

public class DateModelDiffCallback extends DiffUtil.ItemCallback<DateModel> {

    @Override
    public boolean areItemsTheSame(@NonNull DateModel oldItem, @NonNull DateModel newItem) {
        if (oldItem.getDayCount() != newItem.getDayCount()) return false;
        if (!oldItem.getDay().equals(newItem.getDay())) return false;
        if (!oldItem.getDate().equals(newItem.getDate())) return false;
        return true;
    }

    @Override
    public boolean areContentsTheSame(@NonNull DateModel oldItem, @NonNull DateModel newItem) {
        if (oldItem.getDayCount() != newItem.getDayCount()) return false;
        if (!oldItem.getDay().equals(newItem.getDay())) return false;
        if (!oldItem.getDate().equals(newItem.getDate())) return false;
        return true;
    }
}
