package com.nhn.fitness.ui.dialogs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;

import com.nhn.fitness.R;
import com.nhn.fitness.data.shared.AppSettings;
import com.nhn.fitness.ui.base.BaseDialog;
import com.nhn.fitness.ui.interfaces.DialogResultListener;
import com.nhn.fitness.utils.DateUtils;

import java.util.Calendar;

public class BirthdayDialog extends BaseDialog implements DatePicker.OnDateChangedListener {
    private DialogResultListener listener;
    private DatePicker datePicker;
    private long birthday = -1;
    private boolean isSave = false;

    public BirthdayDialog(DialogResultListener listener) {
        this.hasTitle = false;
        this.listener = listener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.dialog_birthday, container, false);
        initViews();
        initData();
        initEvents();
        initObserve();
        return rootView;
    }

    @Override
    protected void initViews() {
        super.initViews();
        Calendar calendar = DateUtils.convertIdToDate(AppSettings.getInstance().getBirthday());
        datePicker = rootView.findViewById(R.id.date_picker);
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), this);
    }

    @Override
    protected void initEvents() {
        super.initEvents();
        rootView.findViewById(R.id.btn_cancel).setOnClickListener(view -> dismissAllowingStateLoss());
        rootView.findViewById(R.id.btn_save).setOnClickListener(view -> save());
    }

    private void save() {
        if (birthday != -1) {
            AppSettings.getInstance().setBirthday(birthday);
            isSave = true;
        }
        dismissAllowingStateLoss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (isSave && listener != null) {
            listener.onResult(-1, null);
        }
    }

    @Override
    public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, i);
        calendar.set(Calendar.MONTH, i1);
        calendar.set(Calendar.DAY_OF_MONTH, i2);
        birthday = DateUtils.getIdDay(calendar);
    }
}
