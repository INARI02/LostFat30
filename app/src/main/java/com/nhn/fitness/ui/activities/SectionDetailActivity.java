package com.nhn.fitness.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.ChallengeDayUser;
import com.nhn.fitness.data.model.DailySectionUser;
import com.nhn.fitness.data.model.DayHistoryModel;
import com.nhn.fitness.data.model.SectionHistory;
import com.nhn.fitness.data.model.SectionUser;
import com.nhn.fitness.data.model.WorkoutUser;
import com.nhn.fitness.data.repositories.ChallengeRepository;
import com.nhn.fitness.data.repositories.DayHistoryRepository;
import com.nhn.fitness.data.repositories.SectionHistoryRepository;
import com.nhn.fitness.data.repositories.SectionRepository;
import com.nhn.fitness.data.repositories.WorkoutRepository;
import com.nhn.fitness.ui.adapters.WorkoutAdapter;
import com.nhn.fitness.ui.adapters.decoration.PreCachingLayoutManager;
import com.nhn.fitness.ui.base.BaseActivity;
import com.nhn.fitness.utils.DateUtils;
import com.nhn.fitness.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class SectionDetailActivity extends BaseActivity implements DialogInterface.OnDismissListener {
    private SectionUser sectionUser;

    private WorkoutAdapter adapter;
    private ArrayList<WorkoutUser> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_detail);

        initViews();
        initEvents();
        initObservers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initObservers();
    }

    private void initObservers() {
        // Load com.nhn.fitness.data
        addDisposable(SectionRepository.getInstance()
                .getSectionUserByIdWithFullData(sectionUser.getId())
                .subscribe(response -> {
                    sectionUser = response;
                    loadList();
                }));
    }

    private void loadList() {
        addDisposable(WorkoutRepository.getInstance().getAllWorkoutUserByIdsWithFullData(sectionUser.getWorkoutsId())
                .subscribe(response -> {
//                    Log.e("status", "loadlist");
                    list.clear();
                    list.addAll(response);
                    adapter.notifyDataSetChanged();
                }));
    }

    private void initEvents() {
        findViewById(R.id.menu_sort).setOnClickListener(view -> gotoEdit());
        findViewById(R.id.btn_start).setOnClickListener(view -> {
            Intent intent = new Intent(this, RunActivity.class);
            intent.putExtra("section", sectionUser);
            intent.putExtra("workouts", list);
            if (sectionUser.getData().getType() == 1) {
                DailySectionUser dailySectionUser = getIntent().getParcelableExtra("challenge");
                intent.putExtra("challenge", dailySectionUser);
            }
            startActivity(intent);
            finish();
        });
    }

    private void initViews() {
        sectionUser = getIntent().getParcelableExtra("com/nhn/fitness/data");
        initToolbar();
        if (sectionUser.isTraining()) {
            findViewById(R.id.menu_sort).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.menu_sort).setVisibility(View.GONE);
        }

        RecyclerView recyclerView = findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new PreCachingLayoutManager(this, ViewUtils.getHeightDevicePixel(this) * 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setItemViewCacheSize(20);
        adapter = new WorkoutAdapter(getSupportFragmentManager(), list);
        recyclerView.setAdapter(adapter);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        ImageView thumb = findViewById(R.id.img_thumb);
        ViewUtils.bindImage(this, ViewUtils.getPathSection(sectionUser.getData().getThumb()), thumb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        if (sectionUser.getData().getType() == 0) {
            ((TextView) findViewById(R.id.txt_title)).setText(sectionUser.getData().getTitleDisplay());
            ((TextView) findViewById(R.id.txt_level)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.txt_description)).setText(sectionUser.getData().getDescriptionDisplay());
            ((TextView) findViewById(R.id.txt_num_workouts)).setText(sectionUser.getData().getNumberWorkoutsString(this));
//            saveData(false);
        } else {
           // ((TextView) findViewById(R.id.txt_title)).setText(getResources().getString(R.string.full_body));
            ((TextView) findViewById(R.id.txt_level)).setText(sectionUser.getData().getTitleDisplay().toUpperCase());
//            ((TextView) findViewById(R.id.txt_description)).setText(sectionUser.getData().getDescriptionDisplay());
            ((TextView) findViewById(R.id.txt_num_workouts)).setText(sectionUser.getData().getNumberWorkoutsString(this));
            ((TextView) findViewById(R.id.txt_description)).setVisibility(View.GONE);
            sectionUser.setStatus(2);
//            saveData(true);
        }
    }

    private void saveData(boolean daily) {
        /***
         *  Finish workout
         */
        if (daily) {
            // Save Challenge
            ChallengeDayUser challengeDayUser = getIntent().getParcelableExtra("challenge");
            challengeDayUser.setState(2);
            ChallengeRepository.getInstance().update(challengeDayUser).subscribe();
        }

        // Save history
        addDisposable(WorkoutRepository.getInstance().getAllWorkoutUserByIdsWithFullData(sectionUser.getWorkoutsId())
                .subscribe(response -> {
                    SectionHistory sectionHistory = new SectionHistory();
                    int dayRandom = new Random().nextInt(7);
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_YEAR, dayRandom);
                    sectionHistory.setId(calendar.getTime().getTime());
                    sectionHistory.setCalendar(calendar);
                    sectionHistory.setTitle(sectionUser.getTitleHistory(this));
                    sectionHistory.setTotalTime(60);
                    sectionHistory.setSectionId(sectionUser.getId());
                    sectionHistory.setThumb(sectionUser.getData().getThumb());
                    float totalCalories = 0;
                    for (WorkoutUser workoutUser : response) {
                        totalCalories += workoutUser.getTotalCalories();
                    }
                    sectionHistory.setCalories(totalCalories);
                    SectionHistoryRepository.getInstance().insert(sectionHistory).subscribe();

                    // Save DayHistory
                    boolean isUpdate = true;
                    DayHistoryModel dayHistoryModel = DayHistoryRepository.getInstance().getByIdWithoutObserve(DateUtils.getIdDay(calendar));
                    if (dayHistoryModel == null) {
                        isUpdate = false;
                        dayHistoryModel = new DayHistoryModel(calendar);
                    }
                    dayHistoryModel.addCalories(totalCalories);
                    dayHistoryModel.addExercise(response.size());
                    if (isUpdate) {
                        DayHistoryRepository.getInstance().update(dayHistoryModel).subscribe();
                    } else {
                        DayHistoryRepository.getInstance().insert(dayHistoryModel).subscribe();
                    }
                }));
        /**
         *  Insert workout end
         */
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    private void gotoEdit() {
//        Intent intent = new Intent(this, EditPlanActivity.class);
//        intent.putExtra("com.nhn.fitness.data", sectionUser);
//        startActivity(intent);
        Intent intent = new Intent(this, EditTrainingActivity.class);
        intent.putExtra("com/nhn/fitness/data", sectionUser);
        startActivity(intent);
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        loadList();
    }
}
