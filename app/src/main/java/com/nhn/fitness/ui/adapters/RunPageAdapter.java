package com.nhn.fitness.ui.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.nhn.fitness.data.model.WorkoutUser;
import com.nhn.fitness.ui.fragments.RunFragment;

import java.util.ArrayList;

public class RunPageAdapter extends FragmentPagerAdapter {
    private ArrayList<WorkoutUser> workoutUsers;

    public RunPageAdapter(FragmentManager fm, ArrayList<WorkoutUser> list) {
        super(fm);
        this.workoutUsers = list;
    }

    @Override
    public Fragment getItem(int position) {
        return RunFragment.newInstance(workoutUsers, workoutUsers.get(position), getCount(), position);
    }

    @Override
    public int getCount() {
        return workoutUsers.size();
    }
}
