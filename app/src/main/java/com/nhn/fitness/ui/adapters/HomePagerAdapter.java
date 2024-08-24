package com.nhn.fitness.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.nhn.fitness.ui.fragments.ReportFragment;
import com.nhn.fitness.ui.fragments.SettingsFragment;
import com.nhn.fitness.ui.fragments.TrainingFragment;
import com.nhn.fitness.ui.fragments.WorkoutsFragment;

public class HomePagerAdapter extends FragmentStatePagerAdapter {

    public HomePagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm,behavior);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return 4;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TrainingFragment();
            case 1:
                return new WorkoutsFragment();
            case 2:
                return new ReportFragment();
            case 3:
                return new SettingsFragment();
            default:
                return new TrainingFragment();
        }
    }

}