package com.nhn.fitness.ui.interfaces;

import android.view.View;

import com.nhn.fitness.data.model.SectionUser;

public interface SectionItemClickListener {
    void onSectionClick(View view, SectionUser sectionUser, int position);
}
