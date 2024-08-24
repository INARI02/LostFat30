package com.nhn.fitness.ui.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Observer;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.Workout;
import com.nhn.fitness.data.model.WorkoutUser;
import com.nhn.fitness.data.repositories.WorkoutRepository;
import com.nhn.fitness.ui.activities.VideoWorkoutActivity;
import com.nhn.fitness.ui.base.BaseDialog;
import com.nhn.fitness.ui.dialogs.viewmodel.EditWorkoutViewModel;
import com.nhn.fitness.ui.interfaces.DialogResultListener;
import com.nhn.fitness.utils.Utils;
import com.nhn.fitness.utils.ViewUtils;

public class ReplaceWorkoutDialog extends BaseDialog {

    private EditWorkoutViewModel viewModel;
    private WorkoutUser workoutUser;
    private boolean autoIncrement = false;
    private boolean autoDecrement = false;
    private final long REPEAT_DELAY = 50;
    private Handler repeatUpdateHandler = new Handler();

    public ReplaceWorkoutDialog(WorkoutUser workoutUser) {
        this.viewModel = new EditWorkoutViewModel();
        this.hasTitle = false;
        this.workoutUser = workoutUser;
        this.workoutUser.setReplaced(true);
        this.workoutUser.setId(workoutUser.getData().getId());
        this.workoutUser.setWorkoutUserId(workoutUser.getData().getId() + "-" + Utils.randomString(5));
    }

    public ReplaceWorkoutDialog(Workout workout) {
        this.viewModel = new EditWorkoutViewModel();
        this.hasTitle = false;
        this.workoutUser = new WorkoutUser(workout.getId());
        this.workoutUser.setTime(workout.getTimeDefault());
        this.workoutUser.setCount(workout.getCountDefault());
        this.workoutUser.setData(workout);
        this.workoutUser.setCalories(workout.getCalories());
        this.workoutUser.setReplaced(true);
        this.workoutUser.setId(workout.getId());
        this.workoutUser.setWorkoutUserId(workout.getId() + "-" + Utils.randomString(5));
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_edit_workout, container, false);
        return rootView;
    }

    @Override
    protected void initData() {
        super.initData();
        if (workoutUser.getData().getType() == 0) {
            viewModel.seconds.setValue(workoutUser.getTime());
        } else {
            viewModel.counts.setValue(workoutUser.getCount());
        }

    }

    @Override
    protected void initViews() {
        super.initViews();
        ((TextView) rootView.findViewById(R.id.txt_title)).setText(workoutUser.getData().getTitleDisplay());
        ((TextView) rootView.findViewById(R.id.txt_description)).setText(workoutUser.getData().getDescriptionDisplay());
        ImageView imageView = rootView.findViewById(R.id.img_thumb);
        ViewUtils.bindImage(getContext(), ViewUtils.getPathWorkout(workoutUser.getData().getImageGender()), imageView);
        ((Button) rootView.findViewById(R.id.btn_save)).setText(getResources().getString(R.string.replace));
        if (workoutUser.getData().getType() == 1 && workoutUser.getData().isTwoSides()) {
            rootView.findViewById(R.id.txt_each_side).setVisibility(View.VISIBLE);
        } else {
            rootView.findViewById(R.id.txt_each_side).setVisibility(View.GONE);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initEvents() {
        super.initEvents();
        rootView.findViewById(R.id.btn_close).setOnClickListener(view -> dismissAllowingStateLoss());
        rootView.findViewById(R.id.btn_increase).setOnClickListener(view -> increase());
        rootView.findViewById(R.id.btn_increase).setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP && autoIncrement) {
                autoIncrement = false;
            }
            return false;
        });
        rootView.findViewById(R.id.btn_increase).setOnLongClickListener(view -> {
            autoIncrement = true;
            repeatUpdateHandler.post(new RepetitiveUpdater());
            return false;
        });
        rootView.findViewById(R.id.btn_reduce).setOnClickListener(view -> reduce());
        rootView.findViewById(R.id.btn_reduce).setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP && autoDecrement) {
                autoDecrement = false;
            }
            return false;
        });
        rootView.findViewById(R.id.btn_reduce).setOnLongClickListener(view -> {
            autoDecrement = true;
            repeatUpdateHandler.post(new RepetitiveUpdater());
            return false;
        });
        rootView.findViewById(R.id.btn_reset).setOnClickListener(view -> {
            if (workoutUser.getData().getType() == 0) {
                viewModel.seconds.setValue(workoutUser.getData().getTimeDefault());
            } else {
                viewModel.counts.setValue(workoutUser.getData().getCountDefault());
            }

        });
        rootView.findViewById(R.id.btn_save).setOnClickListener(view -> save());
        rootView.findViewById(R.id.img_cam).setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), VideoWorkoutActivity.class);
            intent.putExtra("com/nhn/fitness/data", workoutUser);
            startActivity(intent);
        });
    }

    private void save() {
        if (workoutUser.getData().getType() == 0) {
            workoutUser.setTime(viewModel.seconds.getValue());
        } else {
            workoutUser.setCount(viewModel.counts.getValue());
        }
        addDisposable(WorkoutRepository.getInstance().updateWorkoutUser(workoutUser).subscribe(this::dismissAllowingStateLoss));
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

    @Override
    protected void initObserve() {
        super.initObserve();
        if (workoutUser.getData().getType() == 0) {
            viewModel.seconds.observe(getViewLifecycleOwner(), new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    ((AppCompatTextView) rootView.findViewById(R.id.txt_time)).setText(Utils.convertStringTime(integer));
                }
            });
        } else {
            viewModel.counts.observe(getViewLifecycleOwner(), new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    ((AppCompatTextView) rootView.findViewById(R.id.txt_time)).setText("" + integer);
                }
            });
        }
    }

    class RepetitiveUpdater implements Runnable {

        @Override
        public void run() {
            if (autoIncrement) {
                increase();
                repeatUpdateHandler.postDelayed(new RepetitiveUpdater(), REPEAT_DELAY);
            } else if (autoDecrement) {
                reduce();
                repeatUpdateHandler.postDelayed(new RepetitiveUpdater(), REPEAT_DELAY);
            }
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogResultListener) {
            ((DialogResultListener) activity).onResult(DialogResultListener.REPLACE_WORKOUT, workoutUser);
        }
    }
}

