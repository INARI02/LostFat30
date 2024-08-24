package com.nhn.fitness.ui.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.applikeysolutions.cosmocalendar.model.Day;
import com.applikeysolutions.cosmocalendar.selection.HighLightManager;
import com.applikeysolutions.cosmocalendar.utils.SelectionType;
import com.applikeysolutions.cosmocalendar.view.CalendarView;
import com.nhn.fitness.R;
import com.nhn.fitness.data.model.DayHistoryModel;
import com.nhn.fitness.data.model.SectionHistory;
import com.nhn.fitness.data.repositories.DayHistoryRepository;
import com.nhn.fitness.data.repositories.SectionHistoryRepository;
import com.nhn.fitness.data.shared.AppSettings;
import com.nhn.fitness.ui.adapters.SectionHistoryAdapter;
import com.nhn.fitness.ui.base.BaseActivity;
import com.nhn.fitness.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class HistoryActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private SectionHistoryAdapter adapter;
    private ArrayList<SectionHistory> sectionHistories = new ArrayList<>();

    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setStatusBarColor(getResources().getColor(R.color.white));

        initViews();
        initObserver();
    }

    private void initObserver() {
        addDisposable(DayHistoryRepository.getInstance().getAllWorkouts().subscribe(response -> {
            ArrayList<Day> days = new ArrayList<>();
            for (DayHistoryModel dayHistoryModel : response) {
                Day day = new Day(dayHistoryModel.getCalendar());
                day.setHighlight(true);
                days.add(day);
            }
            HighLightManager highLightManager = calendarView.getHighLightManager();
            highLightManager.addDay(days);
            calendarView.update();
        }));
    }

    private void initViews() {
//        Toolbar toolbar = findViewById(R.id.toolBar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        findViewById(R.id.history_action_bar).setOnClickListener(view -> {
            onBackPressed();
        });
        calendarView = findViewById(R.id.calendar_view);
        calendarView.setFirstDayOfWeek(AppSettings.getInstance().getFirstDayOfWeek());
        calendarView.setSelectionType(SelectionType.NONE);
        calendarView.setCalendarOrientation(OrientationHelper.HORIZONTAL);
        calendarView.update();

        initHistory();
    }

    private void initHistory() {
        recyclerView = findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SectionHistoryAdapter(sectionHistories);
        recyclerView.setAdapter(adapter);

        addDisposable(
                SectionHistoryRepository.getInstance().getCurrentWeek()
                        .subscribe(response -> {
                            sectionHistories.clear();
                            sectionHistories.addAll(response);
                            adapter.notifyDataSetChanged();
                            initOtherViews();
                        })
        );
    }

    private void initOtherViews() {
        ArrayList<DayHistoryModel> dayHistoryModels = new ArrayList<>(Utils.getCurrentWeek());
        SimpleDateFormat df = new SimpleDateFormat("MMM d", Locale.US);
        String rangeDate = df.format(dayHistoryModels.get(0).getCalendar().getTime())
                + " - " + df.format(dayHistoryModels.get(6).getCalendar().getTime());
        int totalTime = 0;
        float totalCalories = 0f;
        for (SectionHistory sectionHistory : sectionHistories) {
            totalTime += sectionHistory.getTotalTime();
            totalCalories += sectionHistory.getCalories();
        }
        int mins = totalTime / 60;
        int secs = totalTime % 60;

        ((TextView) findViewById(R.id.txt_date)).setText(rangeDate);
        ((TextView) findViewById(R.id.txt_workout_count)).setText(sectionHistories.size() + " " + getResources().getString(R.string.workouts));
        ((TextView) findViewById(R.id.txt_total_timer)).setText(String.format("%02d:%02d", mins, secs));
        ((TextView) findViewById(R.id.txt_total_calories)).setText(String.format(Locale.US, "%.2f %s", totalCalories, getResources().getString(R.string.calories)));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
