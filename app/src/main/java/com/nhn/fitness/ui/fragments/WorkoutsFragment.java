package com.nhn.fitness.ui.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.nhn.fitness.R;
import com.nhn.fitness.ui.base.BaseFragment;

public class WorkoutsFragment extends BaseFragment {


    public WorkoutsFragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_workouts, container, false);
        initViews();
        initObservers();
        initEvents();
        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();
        ViewPager viewPager = rootView.findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new WorkoutsPagerAdapter(getChildFragmentManager()));
        TabLayout tabLayout = rootView.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void initEvents() {
        super.initEvents();
    }

    @Override
    protected void initObservers() {
        super.initObservers();
    }

    class WorkoutsPagerAdapter extends FragmentStatePagerAdapter {

        public WorkoutsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new WorkoutsListFragment();
                case 1:
                    return new RountinesListFragment();
                default:
                    return new WorkoutsListFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return getString(R.string.workouts_tab);
            } else {
                return getString(R.string.routines_tab);
            }
        }
    }
}
