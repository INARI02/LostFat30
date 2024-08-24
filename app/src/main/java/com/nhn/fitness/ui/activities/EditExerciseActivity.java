package com.nhn.fitness.ui.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.Workout;
import com.nhn.fitness.data.model.WorkoutUser;
import com.nhn.fitness.ui.base.BaseActivity;
import com.nhn.fitness.ui.dialogs.viewmodel.EditWorkoutViewModel;
import com.nhn.fitness.utils.Utils;
import com.nhn.fitness.utils.ViewUtils;



public class EditExerciseActivity extends BaseActivity {

    private static final String TAG_NAME = EditExerciseActivity.class.getSimpleName();
    private EditWorkoutViewModel viewModel;
    private WorkoutUser workoutUser;
    private boolean autoIncrement = false;
    private boolean autoDecrement = false;
    private final long REPEAT_DELAY = 50;
    private Handler repeatUpdateHandler = new Handler();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_exercise);
        setStatusBarColor(getResources().getColor(R.color.white));

        viewModel = new EditWorkoutViewModel();
        Object data = getIntent().getParcelableExtra("com/nhn/fitness/data");
        if (data instanceof WorkoutUser) {
            WorkoutUser workoutUser = (WorkoutUser) data;
            this.workoutUser = workoutUser;
            this.workoutUser.setReplaced(true);
        } else {
            Workout workout = (Workout) data;
            this.workoutUser = new WorkoutUser();
            this.workoutUser.setTime(workout.getTimeDefault());
            this.workoutUser.setCount(workout.getCountDefault());
            this.workoutUser.setData(workout);
            this.workoutUser.setCalories(workout.getCalories());
            this.workoutUser.setReplaced(true);
            this.workoutUser.setId(workout.getId());
            this.workoutUser.setTraining(workout.isTraining());
            this.workoutUser.setWorkoutUserId(workout.getId() + "-" + Utils.randomString(5));
        }

        initData();
        initViews();
        initEvents();
        initObserve();
    }

    protected void initData() {
        if (workoutUser.getData().getType() == 0) {
            viewModel.seconds.setValue(workoutUser.getTime());
        } else {
            viewModel.counts.setValue(workoutUser.getCount());
        }

    }

    protected void initViews() {
        ((TextView) findViewById(R.id.txt_title)).setText(workoutUser.getData().getTitleDisplay());
        ((TextView) findViewById(R.id.txt_description)).setText(workoutUser.getData().getDescriptionDisplay());
        ImageView imageView = findViewById(R.id.img_thumb);
        ViewUtils.bindImage(this, ViewUtils.getPathWorkout(workoutUser.getData().getImageGender()), imageView);
        ((TextView) findViewById(R.id.btn_save)).setText(getResources().getString(R.string.btn_save));
        if (workoutUser.getData().getType() == 1 && workoutUser.getData().isTwoSides()) {
            findViewById(R.id.txt_each_side).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.txt_each_side).setVisibility(View.GONE);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    protected void initEvents() {
        findViewById(R.id.btn_close).setOnClickListener(view -> {
            setResult(Activity.RESULT_CANCELED);
            onBackPressed();
        });
        findViewById(R.id.btn_increase).setOnClickListener(view -> increase());
        findViewById(R.id.btn_increase).setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP && autoIncrement) {
                autoIncrement = false;
            }
            return false;
        });
        findViewById(R.id.btn_increase).setOnLongClickListener(view -> {
            autoIncrement = true;
            repeatUpdateHandler.post(new EditExerciseActivity.RepetitiveUpdater());
            return false;
        });
        findViewById(R.id.btn_reduce).setOnClickListener(view -> reduce());
        findViewById(R.id.btn_reduce).setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP && autoDecrement) {
                autoDecrement = false;
            }
            return false;
        });
        findViewById(R.id.btn_reduce).setOnLongClickListener(view -> {
            autoDecrement = true;
            repeatUpdateHandler.post(new EditExerciseActivity.RepetitiveUpdater());
            return false;
        });
//        findViewById(R.id.btn_reset).setOnClickListener(view -> {
//            if (workoutUser.getData().getType() == 0) {
//                viewModel.seconds.setValue(workoutUser.getData().getTimeDefault());
//            } else {
//                viewModel.counts.setValue(workoutUser.getData().getCountDefault());
//            }
//
//        });
        findViewById(R.id.btn_save).setOnClickListener(view -> save());
        findViewById(R.id.img_cam).setOnClickListener(view -> {
            Intent intent = new Intent(this, VideoWorkoutActivity.class);
            intent.putExtra("com/nhn/fitness/data", workoutUser);
            startActivity(intent);
        });
        findViewById(R.id.txt_cam).setOnClickListener(view -> {
            Intent intent = new Intent(this, VideoWorkoutActivity.class);
            intent.putExtra("com/nhn/fitness/data", workoutUser);
            startActivity(intent);
        });
    }

    private void callback() {
        Intent intent = getIntent();
        intent.putExtra("com/nhn/fitness/data", workoutUser);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void save() {
        if (workoutUser.getData().getType() == 0) {
            workoutUser.setTime(viewModel.seconds.getValue());
        } else {
            workoutUser.setCount(viewModel.counts.getValue());
        }
        callback();
//        addDisposable(WorkoutRepository.getInstance().insertWorkoutUser(workoutUser).subscribe(this::callback));
    }

    private void increase() {
        if (workoutUser.getData().getType() == 0) {
            int current = viewModel.seconds.getValue();
            if (current >= 3598) {
                current = 3598;
            }
            viewModel.seconds.setValue(++current);
        } else {
            int current = viewModel.counts.getValue();
            if (current >= 199) {
                current = 199;
            }
            viewModel.counts.setValue(++current);
        }
    }

    private void reduce() {
        if (workoutUser.getData().getType() == 0) {
            int current = viewModel.seconds.getValue();
            if (current <= 2) {
                current = 2;
            }
            viewModel.seconds.setValue(--current);
        } else {
            int current = viewModel.counts.getValue();
            if (current <= 2) {
                current = 2;
            }
            viewModel.counts.setValue(--current);
        }
    }

    protected void initObserve() {
        if (workoutUser.getData().getType() == 0) {
            viewModel.seconds.observe(this, integer -> ((AppCompatTextView) findViewById(R.id.txt_time)).setText(Utils.convertStringTime(integer)));
        } else {
            viewModel.counts.observe(this, integer -> ((AppCompatTextView) findViewById(R.id.txt_time)).setText("" + integer));
        }
    }

    class RepetitiveUpdater implements Runnable {

        @Override
        public void run() {
            if (autoIncrement) {
                increase();
                repeatUpdateHandler.postDelayed(new EditExerciseActivity.RepetitiveUpdater(), REPEAT_DELAY);
            } else if (autoDecrement) {
                reduce();
                repeatUpdateHandler.postDelayed(new EditExerciseActivity.RepetitiveUpdater(), REPEAT_DELAY);
            }
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }

}

