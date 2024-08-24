package com.nhn.fitness.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.Workout;
import com.nhn.fitness.data.model.WorkoutUser;
import com.nhn.fitness.data.room.AppDatabaseConst;
import com.nhn.fitness.ui.adapters.AddExerciseAdapter;
import com.nhn.fitness.ui.base.BaseActivity;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AddExerciseActivity extends BaseActivity {
    private AddExerciseAdapter adapter;
    private ArrayList<Workout> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private boolean createMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);
        setStatusBarColor(getResources().getColor(R.color.white));

        initViews();
        loadList();
    }

    private void loadList() {
        addDisposable(AppDatabaseConst.getInstance().workoutDao().getAllTraining()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    list.clear();
                    list.addAll(response);
                    adapter.notifyDataSetChanged();
                })
        );
    }

    private void initViews() {
        createMode = getIntent().getBooleanExtra("com/nhn/fitness/data", true);
        findViewById(R.id.iv_back_add_exercise).setOnClickListener(view -> {
            onBackPressed();
        });
        adapter = new AddExerciseAdapter(this, getSupportFragmentManager(), list);
        recyclerView = findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 123) {
            if (createMode) {
                WorkoutUser workoutUser = data.getParcelableExtra("com/nhn/fitness/data");
                Intent intent = new Intent(this, EditTrainingActivity.class);
                intent.putExtra("com/nhn/fitness/data", workoutUser);
                startActivity(intent);
                finish();
            } else {
                WorkoutUser workoutUser = data.getParcelableExtra("com/nhn/fitness/data");
                Intent intent = getIntent();
                intent.putExtra("com/nhn/fitness/data", workoutUser);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    }
}
