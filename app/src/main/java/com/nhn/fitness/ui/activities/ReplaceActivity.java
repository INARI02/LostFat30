package com.nhn.fitness.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.SectionUser;
import com.nhn.fitness.data.model.Workout;
import com.nhn.fitness.data.model.WorkoutUser;
import com.nhn.fitness.data.repositories.WorkoutRepository;
import com.nhn.fitness.ui.adapters.WorkoutReplaceAdapter;
import com.nhn.fitness.ui.base.BaseActivity;
import com.nhn.fitness.ui.dialogs.ReplaceWorkoutDialog;
import com.nhn.fitness.ui.interfaces.DialogResultListener;
import com.nhn.fitness.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ReplaceActivity extends BaseActivity implements DialogResultListener {

    private RecyclerView recyclerView;
    private WorkoutReplaceAdapter adapter;
    private ArrayList<Workout> list = new ArrayList<>();
    private SectionUser sectionUser;
    private WorkoutUser workoutUser;
    private WorkoutUser result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(getResources().getColor(R.color.white));
        setContentView(R.layout.activity_replace);
        initViews();
        initEvents();
        loadList();
    }

    private void initEvents() {
        findViewById(R.id.workout).setOnClickListener(view -> {
            new ReplaceWorkoutDialog(workoutUser).show(getSupportFragmentManager(), null);
        });
    }

    private void initViews() {
        sectionUser = getIntent().getParcelableExtra("section");
        workoutUser = getIntent().getParcelableExtra("workout");

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ImageView imageView = findViewById(R.id.img_thumb);
        ViewUtils.bindImage(this, ViewUtils.getPathWorkout(workoutUser.getData().getImageGender()), imageView);
        ((TextView) findViewById(R.id.txt_title)).setText(workoutUser.getData().getTitleDisplay());
        ((TextView) findViewById(R.id.txt_number)).setText(workoutUser.getTimeCountString());
        ((TextView) findViewById(R.id.txt_section)).setText(sectionUser.getData().getTitleDisplay());

        recyclerView = findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WorkoutReplaceAdapter(getSupportFragmentManager(), list);
        recyclerView.setAdapter(adapter);
    }

    private void loadList() {
//        addDisposable(WorkoutRepository.getInstance()
//                .getAllWorkoutByGroup(workoutUser.getData().getGroup())
//                .subscribe(response -> {
//                    list.clear();
//                    list.addAll(response);
//                    list.remove(workoutUser.getData());
//                    adapter.notifyDataSetChanged();
//                }));
        addDisposable(Single.just(sectionUser.getData().getWorkoutsId())
                .subscribeOn(Schedulers.io())
                .map((Function<List<String>, List<Workout>>) strings -> {
                    ArrayList<Workout> workouts = new ArrayList<>();
                    for (String id : strings) {
                        workouts.add(WorkoutRepository.getInstance().getWorkoutById(id));
                    }
                    return workouts;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    list.clear();
                    list.addAll(response);
                    list.remove(workoutUser.getData());
                    adapter.notifyDataSetChanged();
                })
        );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onResult(int type, Object value) {
        result = (WorkoutUser) value;
        Intent intent = new Intent();
        intent.putExtra("result", result);
        setResult(RESULT_OK, intent);
        finish();
    }
}
