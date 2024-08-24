package com.nhn.fitness.ui.adapters.diff;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.nhn.fitness.data.model.SectionUser;

public class SectionUserDiffCallBack extends DiffUtil.ItemCallback<SectionUser> {

    @Override
    public boolean areItemsTheSame(@NonNull SectionUser oldItem, @NonNull SectionUser newItem) {
        return oldItem.getId().equals(newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull SectionUser oldItem, @NonNull SectionUser newItem) {
        if (oldItem.isFavorite() != newItem.isFavorite()) return false;
        if (oldItem.getUpdated().getTime() != newItem.getUpdated().getTime()) return false;
        if (!oldItem.getData().getTitleDisplay().equals(newItem.getData().getTitleDisplay())) return false;
        if (oldItem.getData().getType() != newItem.getData().getType()) return false;
        if (oldItem.getData().getStatus() != newItem.getData().getStatus()) return false;
        return true;
    }
}
