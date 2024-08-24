package com.nhn.fitness.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.nhn.fitness.R;
import com.nhn.fitness.ui.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends BaseFragment {


    public ReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_report, container, false);
        initViews();
        initObservers();
        initEvents();
        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();
        replaceChildFragment(R.id.report, new TotalFragment(), null, false, -1);
        replaceChildFragment(R.id.chart_weight, new WeightChartFragment(), null, false, -1);
        replaceChildFragment(R.id.bmi, new BMIFragment(), null, false, -1);
        replaceChildFragment(R.id.chart_calories, new CaloriesChartFragment(), null, false, -1);
       // replaceChildFragment(R.id.chart_waistline, new WaistlineChartFragment(), null, false, -1);
    }
}
