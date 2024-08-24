package com.nhn.fitness.ui.dialogs;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProviders;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.Reminder;
import com.nhn.fitness.data.repositories.ReminderRepository;
import com.nhn.fitness.ui.activities.viewmodel.RunViewModel;
import com.nhn.fitness.ui.base.BaseDialog;
import com.nhn.fitness.ui.base.MyViewModelFactory;
import com.nhn.fitness.utils.Utils;

import java.util.Calendar;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class QuitDialog extends BaseDialog {
    private RunViewModel viewModel;

    public QuitDialog() {
        this.hasTitle = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_quit_dialog, container, false);
        viewModel = ViewModelProviders.of(getActivity(), MyViewModelFactory.getInstance()).get(RunViewModel.class);
        return rootView;
    }

    @Override
    protected void initEvents() {
        super.initEvents();
        rootView.findViewById(R.id.btn_continue).setOnClickListener(view -> dismissAllowingStateLoss());
        rootView.findViewById(R.id.btn_quit).setOnClickListener(view -> {
            dismissAllowingStateLoss();
            viewModel.actionQuit.call();
        });
        TextView btnComeback = rootView.findViewById(R.id.btn_come_back);
        btnComeback.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        btnComeback.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance(Locale.US);
            calendar.add(Calendar.MINUTE, 30);
            Reminder newReminder = new Reminder(
                    Utils.randomInt(),
                    "",
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    new boolean[]{true, true, true, true, true, true, true},
                    true,
                    false,
                    true,
                    Calendar.getInstance().getTimeInMillis());
            newReminder.setAlarm(getContext());
            addDisposable(ReminderRepository.getInstance().insert(newReminder)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        dismissAllowingStateLoss();
                        viewModel.actionQuit.call();
                    }));
        });
    }
}
