package com.nhn.fitness.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.nhn.fitness.R;
import com.nhn.fitness.ui.base.BaseFragment;
import com.nhn.fitness.ui.interfaces.Callback;
import com.nhn.fitness.utils.DataConverter;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class InfoMapFragment extends BaseFragment {
    private TextView mSpeedTextView, mDistanceTextView, mTimeTextView, mKcalTextView;
    private AppCompatButton mStartButton;
    private Callback mActivityCallback;
    private boolean isRunning = false;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_info_map, container, false);
        initViews();
        initEvents();
        initObservers();
        return rootView;
    }

    public void updateMapData(float speed, float distance, double calories) {
        mSpeedTextView.setText(String.valueOf(speed));
        mDistanceTextView.setText(String.valueOf(distance));
        mKcalTextView.setText(String.valueOf(calories));
    }

    public void updateRunningTime(long time) {
        String formattedTime = DataConverter.convertSecondsToHHMMSS(time);
        mTimeTextView.setText(formattedTime);
    }

    public void setActivityCallback(Callback mActivityCallback) {
        this.mActivityCallback = mActivityCallback;
    }

    @Override
    protected void initViews() {
        super.initViews();
        mSpeedTextView = rootView.findViewById(R.id.tv_speed);
        mDistanceTextView = rootView.findViewById(R.id.tv_distance);
        mTimeTextView = rootView.findViewById(R.id.tv_running_time);
        mKcalTextView = rootView.findViewById(R.id.tv_calo);
        mStartButton = rootView.findViewById(R.id.btn_start);
    }

    @Override
    protected void initEvents() {
        super.initEvents();
        mStartButton.setOnClickListener(v -> {
            if (mActivityCallback != null) {
                mActivityCallback.execute(null);
            }
            if (isRunning) {
                mStartButton.setText(R.string.start);
            } else {
                mStartButton.setText(R.string.stop);
            }
            isRunning = !isRunning;
        });
    }

    @Override
    protected void initObservers() {
        super.initObservers();
    }
}
