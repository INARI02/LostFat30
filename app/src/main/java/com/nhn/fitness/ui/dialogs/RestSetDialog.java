package com.nhn.fitness.ui.dialogs;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.nhn.fitness.R;
import com.nhn.fitness.data.shared.AppSettings;
import com.nhn.fitness.ui.base.BaseDialog;
import com.nhn.fitness.ui.dialogs.viewmodel.RestSetViewModel;
import com.nhn.fitness.ui.interfaces.DialogResultListener;
import com.nhn.fitness.utils.Constants;
import com.nhn.fitness.utils.Utils;

public class RestSetDialog extends BaseDialog {
    private RestSetViewModel viewModel;
    private boolean isRestSet;
    private int min, max;

    private boolean autoIncrement = false;
    private boolean autoDecrement = false;
    private final long REPEAT_DELAY = 50;
    private Handler repeatUpdateHandler = new Handler();

    private DialogResultListener listener;

    public RestSetDialog(DialogResultListener listener, boolean isRestSet) {
        this.listener = listener;
        this.hasTitle = false;
        this.isRestSet = isRestSet;
        viewModel = new RestSetViewModel(isRestSet);
        if (isRestSet) {
            min = Constants.REST_SET_MIN;
            max = Constants.REST_SET_MAX;
        } else {
            min = Constants.COUNTDOWN_MIN;
            max = Constants.COUNTDOWN_MAX;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_rest_set, container, false);
        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();
        if (isRestSet) {
            ((TextView) rootView.findViewById(R.id.txt_title)).setText(getResources().getString(R.string.set_duration_rest));
        } else {
            ((TextView) rootView.findViewById(R.id.txt_title)).setText(getResources().getString(R.string.set_duration_countdown));
        }
    }

    @Override
    protected void initObserve() {
        super.initObserve();
        viewModel.seconds.observe(getViewLifecycleOwner(), integer -> ((AppCompatTextView) rootView.findViewById(R.id.txt_time)).setText(Utils.convertStringTime(integer)));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initEvents() {
        super.initEvents();
        rootView.findViewById(R.id.btn_cancel).setOnClickListener(view -> dismissAllowingStateLoss());
        rootView.findViewById(R.id.btn_save).setOnClickListener(view -> save());
        rootView.findViewById(R.id.btn_right).setOnClickListener(view -> increase());
        rootView.findViewById(R.id.btn_right).setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP && autoIncrement) {
                autoIncrement = false;
            }
            return false;
        });
        rootView.findViewById(R.id.btn_right).setOnLongClickListener(view -> {
            autoIncrement = true;
            repeatUpdateHandler.post(new RepetitiveUpdater());
            return false;
        });
        rootView.findViewById(R.id.btn_left).setOnClickListener(view -> reduce());
        rootView.findViewById(R.id.btn_left).setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP && autoDecrement) {
                autoDecrement = false;
            }
            return false;
        });
        rootView.findViewById(R.id.btn_left).setOnLongClickListener(view -> {
            autoDecrement = true;
            repeatUpdateHandler.post(new RepetitiveUpdater());
            return false;
        });
    }

    private void save() {
        if (isRestSet) {
            AppSettings.getInstance().setRestSet(viewModel.seconds.getValue());
        } else {
            AppSettings.getInstance().setCountDown(viewModel.seconds.getValue());
        }
        dismissAllowingStateLoss();
    }

    private void increase() {
        int current = viewModel.seconds.getValue();
        if (current < max) {
            viewModel.seconds.setValue(++current);
        }
    }

    private void reduce() {
        int current = viewModel.seconds.getValue();
        if (current > min) {
            viewModel.seconds.setValue(--current);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener != null) {
            listener.onResult(isRestSet ? DialogResultListener.REST_SET : DialogResultListener.COUNTDOWN, null);
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
}
