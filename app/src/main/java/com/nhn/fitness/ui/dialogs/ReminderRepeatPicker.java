package com.nhn.fitness.ui.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.Reminder;
import com.nhn.fitness.data.repositories.ReminderRepository;
import com.nhn.fitness.ui.base.BaseDialog;
import com.nhn.fitness.utils.Utils;

import java.util.Calendar;

public class ReminderRepeatPicker extends BaseDialog implements CompoundButton.OnCheckedChangeListener {

    private Reminder reminder;
    private boolean[] repeats;
    private boolean isEdit = false;
    private int hours, mins;

    public ReminderRepeatPicker(int hours, int mins) {
        this.hours = hours;
        this.mins = mins;
        this.hasTitle = false;
        this.repeats = new boolean[]{true, true, true, true, true, true, true};
        this.isEdit = false;
    }

    public ReminderRepeatPicker(Reminder reminder) {
        this.reminder = reminder;
        this.hasTitle = false;
        this.repeats = reminder.getRepeats();
        this.isEdit = true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_repeat_reminder, container, false);
        initViews();
        initEvents();
        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();
        refresh();
    }

    private void refresh() {
        ((CheckBox) rootView.findViewById(R.id.cb_sun)).setChecked(repeats[0]);
        ((CheckBox) rootView.findViewById(R.id.cb_mon)).setChecked(repeats[1]);
        ((CheckBox) rootView.findViewById(R.id.cb_tue)).setChecked(repeats[2]);
        ((CheckBox) rootView.findViewById(R.id.cb_wed)).setChecked(repeats[3]);
        ((CheckBox) rootView.findViewById(R.id.cb_thu)).setChecked(repeats[4]);
        ((CheckBox) rootView.findViewById(R.id.cb_fri)).setChecked(repeats[5]);
        ((CheckBox) rootView.findViewById(R.id.cb_sat)).setChecked(repeats[6]);
    }

    @Override
    protected void initEvents() {
        super.initEvents();
        ((CheckBox) rootView.findViewById(R.id.cb_sun)).setOnCheckedChangeListener(this);
        ((CheckBox) rootView.findViewById(R.id.cb_mon)).setOnCheckedChangeListener(this);
        ((CheckBox) rootView.findViewById(R.id.cb_tue)).setOnCheckedChangeListener(this);
        ((CheckBox) rootView.findViewById(R.id.cb_wed)).setOnCheckedChangeListener(this);
        ((CheckBox) rootView.findViewById(R.id.cb_thu)).setOnCheckedChangeListener(this);
        ((CheckBox) rootView.findViewById(R.id.cb_fri)).setOnCheckedChangeListener(this);
        ((CheckBox) rootView.findViewById(R.id.cb_sat)).setOnCheckedChangeListener(this);

        rootView.findViewById(R.id.btn_cancel).setOnClickListener(view -> dismissAllowingStateLoss());
        rootView.findViewById(R.id.btn_ok).setOnClickListener(view -> save());
    }

    private void save() {
        if (isEdit) {
            reminder.setRepeats(repeats);
            addDisposable(ReminderRepository.getInstance().update(reminder).subscribe());
        } else {
            Reminder newReminder = new Reminder(
                    Utils.randomInt(),
                    "",
                    hours,
                    mins,
                    repeats,
                    true,
                    false,
                    true,
                    Calendar.getInstance().getTimeInMillis());
            newReminder.setAlarm(getContext());
            addDisposable(ReminderRepository.getInstance().insert(newReminder).subscribe());
        }
        dismissAllowingStateLoss();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.cb_sun:
                repeats[0] = b;
                break;
            case R.id.cb_mon:
                repeats[1] = b;
                break;
            case R.id.cb_tue:
                repeats[2] = b;
                break;
            case R.id.cb_wed:
                repeats[3] = b;
                break;
            case R.id.cb_thu:
                repeats[4] = b;
                break;
            case R.id.cb_fri:
                repeats[5] = b;
                break;
            case R.id.cb_sat:
                repeats[6] = b;
                break;
        }
    }
}
