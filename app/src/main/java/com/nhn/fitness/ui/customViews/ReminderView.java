package com.nhn.fitness.ui.customViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.Reminder;
import com.nhn.fitness.data.repositories.ReminderRepository;
import com.nhn.fitness.ui.activities.ReminderActivity;

import java.util.Collections;

public class ReminderView extends FrameLayout implements View.OnClickListener {
    public ReminderView(Context context) {
        super(context);
        init();
    }

    public ReminderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ReminderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ReminderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @SuppressLint("CheckResult")
    private void init() {
        inflate(getContext(), R.layout.reminder_layout, this);
        this.setOnClickListener(this);
        SwitchCompat swEnable = findViewById(R.id.sw_enable);
        ReminderRepository.getInstance().hasReminder().subscribe(swEnable::setChecked);
        ReminderRepository.getInstance().getAll().subscribe(response -> {
            Collections.sort(response);
            StringBuilder builder = new StringBuilder();
            for (Reminder reminder : response) {
                if (!reminder.isAdmin()) {
                    builder.append(", ");
                    builder.append(reminder.getTimeFormat());
                }
            }
            if (builder.toString().isEmpty()) {
                ((TextView) findViewById(R.id.txt_time)).setText(getResources().getString(R.string.reminder));
            } else {
                ((TextView) findViewById(R.id.txt_time)).setText(builder.toString().replaceFirst(", ", ""));
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getContext(), ReminderActivity.class);
        getContext().startActivity(intent);
    }
}
