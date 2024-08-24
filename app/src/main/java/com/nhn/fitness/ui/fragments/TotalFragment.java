package com.nhn.fitness.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.DayHistoryModel;
import com.nhn.fitness.data.repositories.DayHistoryRepository;
import com.nhn.fitness.data.repositories.SectionHistoryRepository;
import com.nhn.fitness.data.shared.AppSettings;
import com.nhn.fitness.ui.activities.HistoryActivity;
import com.nhn.fitness.ui.activities.SettingGoalActivity;
import com.nhn.fitness.ui.base.BaseFragment;
import com.nhn.fitness.ui.customViews.CurrentWeekView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TotalFragment extends BaseFragment {


    public TotalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_total, container, false);
        initViews();
        initObservers();
        initEvents();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        addDisposable(DayHistoryRepository.getInstance().getCurrentWeek().subscribe(this::initCurrentWeek));
        ((TextView) rootView.findViewById(R.id.txt_total_days)).setText("/"+AppSettings.getInstance().getNumberDaysOfWeekly() + "");
    }

    @Override
    protected void initViews() {
        super.initViews();
        ((TextView) rootView.findViewById(R.id.txt_total_days)).setText("/"+AppSettings.getInstance().getNumberDaysOfWeekly() + "");
    }

    private void initCurrentWeek(List<DayHistoryModel> data) {
        CurrentWeekView currentWeekView = rootView.findViewById(R.id.current_week_view);
        currentWeekView.setData(data);
        int count = 0;
        for (DayHistoryModel dayHistoryModel : data) {
            if (dayHistoryModel.isHasWorkout()) {
                count++;
            }
        }
        ((TextView) rootView.findViewById(R.id.txt_current_day)).setText(count + "");
    }

    @Override
    protected void initObservers() {
        super.initObservers();
        addDisposable(DayHistoryRepository.getInstance().getCurrentWeek().subscribe(this::initCurrentWeek));
        addDisposable(SectionHistoryRepository.getInstance().getCount().subscribe(response -> {
            ((TextView) rootView.findViewById(R.id.txt_num_workouts)).setText(response + "");
        }));
        addDisposable(SectionHistoryRepository.getInstance().sumCalories().subscribe(response -> {
            ((TextView) rootView.findViewById(R.id.txt_sum_calories)).setText(String.format("%.0f", response));
        }));
        addDisposable(SectionHistoryRepository.getInstance().sumTotalTime().subscribe(response -> {
            int mins = response / 60;
            ((TextView) rootView.findViewById(R.id.txt_sum_timer)).setText(mins + "");
        }));
    }

    @Override
    protected void initEvents() {
        super.initEvents();
        rootView.findViewById(R.id.current_week_view).setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), HistoryActivity.class);
            getActivity().startActivity(intent);
            getActivity().overridePendingTransition(0, 0);
        });
        rootView.findViewById(R.id.btn_goal).setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), SettingGoalActivity.class);
            getActivity().startActivity(intent);
            getActivity().overridePendingTransition(0, 0);
        });
    }
}
