package com.nhn.fitness.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.nhn.fitness.R;
import com.nhn.fitness.data.shared.AppSettings;
import com.nhn.fitness.ui.base.BaseActivity;
import com.nhn.fitness.ui.dialogs.FirstDayOfWeekDialogFragment;
import com.nhn.fitness.ui.dialogs.WeeklyDialogFragment;
import com.nhn.fitness.ui.interfaces.DialogResultListener;

import java.util.Calendar;

public class GuideGoalActivity extends BaseActivity implements DialogResultListener {

    private int currentFirst;
    private int currentNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_guide_goal);

        initData();
        initViews();
        initEvents();
        refreshState();
    }

    private void refreshState() {
        String day = getResources().getString(R.string.day);
        String days = getResources().getString(R.string.days);
        String numberDays = currentNum + " " + (currentNum > 1 ? days : day);
        String firstDay = "";
        switch (currentFirst) {
            case Calendar.SUNDAY:
                firstDay = getResources().getString(R.string.sunday);
                break;
            case Calendar.MONDAY:
                firstDay = getResources().getString(R.string.monday);
                break;
            case Calendar.SATURDAY:
                firstDay = getResources().getString(R.string.saturday);
                break;
        }

        ((TextView) findViewById(R.id.txt_number_days_of_week)).setText(numberDays);
        ((TextView) findViewById(R.id.txt_first_day_of_week)).setText(firstDay);
    }

    private void initData() {
        currentFirst = AppSettings.getInstance().getFirstDayOfWeek();
        currentNum = AppSettings.getInstance().getNumberDaysOfWeekly();
    }

    private void initEvents() {
        findViewById(R.id.btn_weekly).setOnClickListener(view -> {
            new WeeklyDialogFragment(currentNum).show(getSupportFragmentManager(), null);
        });

        findViewById(R.id.btn_first_day).setOnClickListener(view -> {
            new FirstDayOfWeekDialogFragment(currentFirst).show(getSupportFragmentManager(), null);
        });

        findViewById(R.id.btn_save).setOnClickListener(view -> {
            AppSettings.getInstance().setFirstDayOfWeek(currentFirst);
            AppSettings.getInstance().setNumberDaysOfWeekly(currentNum);

            Intent intent = new Intent(this, GuideReminderActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });
    }

    private void initViews() {

    }


    @Override
    public void onResult(int type, Object value) {
        if (type == DialogResultListener.FIRST_DAY_OF_WEEK) {
            currentFirst = (int) value;
        } else if (type == DialogResultListener.NUMBER_DAYS_WEEKLY) {
            currentNum = (int) value;
        }
        refreshState();
    }
}
