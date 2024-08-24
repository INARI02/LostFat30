package com.nhn.fitness.ui.dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.WorkoutUser;
import com.nhn.fitness.ui.activities.VideoWorkoutActivity;
import com.nhn.fitness.ui.base.BaseDialog;
import com.nhn.fitness.utils.ViewUtils;

import java.util.List;

public class WorkoutInfoDialog extends BaseDialog {

    private List<WorkoutUser> workoutUsers;
    private WorkoutUser current;
    private int index;

    public WorkoutInfoDialog(List<WorkoutUser> workoutUsers, int position) {
        this.hasTitle = false;
        this.workoutUsers = workoutUsers;
        index = position;
        current = workoutUsers.get(index);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.workout_info_dialog, container, false);
        initViews();
        initEvents();
        refreshViews();
        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();
        ((TextView) rootView.findViewById(R.id.txt_total)).setText("/" + workoutUsers.size());
    }

    @Override
    protected void initEvents() {
        super.initEvents();
        rootView.findViewById(R.id.btn_next).setOnClickListener(view -> {
            if (index < workoutUsers.size() - 1) {
                index++;
                current = workoutUsers.get(index);
                refreshViews();
            }
        });
        rootView.findViewById(R.id.btn_prev).setOnClickListener(view -> {
            if (index > 0) {
                index--;
                current = workoutUsers.get(index);
                refreshViews();
            }
        });
        rootView.findViewById(R.id.btn_close).setOnClickListener(view -> {
            dismissAllowingStateLoss();
        });
        rootView.findViewById(R.id.img_cam).setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), VideoWorkoutActivity.class);
            intent.putExtra("com/nhn/fitness/data", current);
            startActivity(intent);
        });
    }

    private void refreshViews() {
        ImageView imageView = rootView.findViewById(R.id.img_thumb);
        ViewUtils.bindImage(getContext(), ViewUtils.getPathWorkout(current.getData().getImageGender()), imageView);
        ((TextView) rootView.findViewById(R.id.txt_current)).setText(String.valueOf(index + 1));
        ((TextView) rootView.findViewById(R.id.txt_title)).setText(current.getData().getTitleDisplay());
        ((TextView) rootView.findViewById(R.id.txt_description)).setText(current.getData().getDescriptionDisplay());
    }
}
