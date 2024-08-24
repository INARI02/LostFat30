package com.nhn.fitness.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.Workout;
import com.nhn.fitness.data.model.WorkoutUser;
import com.nhn.fitness.ui.base.BaseActivity;
import com.nhn.fitness.utils.ViewUtils;


public class VideoWorkoutActivity extends BaseActivity {
    private WorkoutUser workoutUser;
    private Workout workout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_workout);
        initViews();
        initEvents();
        initObservers();
    }

    private void initObservers() {

    }

    private void initEvents() {
        findViewById(R.id.btn_back).setOnClickListener(view -> onBackPressed());
    }

    private void initViews() {
        Object object = getIntent().getParcelableExtra("com/nhn/fitness/data");
        if (object instanceof Workout) {
            workout = (Workout) object;
        } else {
            workoutUser = (WorkoutUser) object;
        }

        if (workout == null) {
            ((TextView) findViewById(R.id.txt_title_anim)).setText(workoutUser.getData().getTitleDisplay() + " " + workoutUser.getTimeCountTitle(false));
            ((TextView) findViewById(R.id.txt_content_anim)).setText(workoutUser.getData().getDescriptionDisplay());
            if (workoutUser.getData().getType() == 0) {
                findViewById(R.id.txt_count_anim).setVisibility(View.GONE);
            } else {
                ((TextView) findViewById(R.id.txt_count_anim)).setText(getResources().getString(R.string.each_side) + " " + workoutUser.getTimeCountTitle(true));
            }
        } else {
            ((TextView) findViewById(R.id.txt_title_anim)).setText(workout.getTitleDisplay() + " " + workout.getTimeCountTitle(false));
            ((TextView) findViewById(R.id.txt_content_anim)).setText(workout.getDescriptionDisplay());
            if (workout.getType() == 0) {
                findViewById(R.id.txt_count_anim).setVisibility(View.GONE);
            } else {
                ((TextView) findViewById(R.id.txt_count_anim)).setText(getResources().getString(R.string.each_side) + " " + workout.getTimeCountTitle(true));
            }
        }

        ImageView imageView = findViewById(R.id.img_thumb);
        ViewUtils.bindImage(this, ViewUtils.getPathWorkout(workout == null ? workoutUser.getData().getImageGender() : workout.getAnim()), imageView);

    }

}