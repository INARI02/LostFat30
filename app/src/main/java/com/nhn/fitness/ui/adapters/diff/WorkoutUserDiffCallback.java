package com.nhn.fitness.ui.adapters.diff;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.nhn.fitness.data.model.WorkoutUser;

public class WorkoutUserDiffCallback extends DiffUtil.ItemCallback<WorkoutUser> {

    @Override
    public boolean areItemsTheSame(@NonNull WorkoutUser oldItem, @NonNull WorkoutUser newItem) {
        return oldItem.getId().equals(newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull WorkoutUser oldItem, @NonNull WorkoutUser newItem) {
        if (oldItem.getTime() != newItem.getTime()) return false;
        if (oldItem.getCount() != newItem.getCount()) return false;
        if (oldItem.getStatus() != newItem.getStatus()) return false;
        if (oldItem.getCalories() != newItem.getCalories()) return false;
        if (!oldItem.getData().getTitleDisplay().equals(newItem.getData().getTitleDisplay())) return false;
        return true;
    }
}
