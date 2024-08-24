package com.nhn.fitness.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.NumberPicker;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.Reminder;
import com.nhn.fitness.data.repositories.ReminderRepository;
import com.nhn.fitness.data.shared.AppSettings;
import com.nhn.fitness.ui.base.BaseActivity;
import com.nhn.fitness.utils.Utils;

import java.util.Calendar;


public class GuideReminderActivity extends BaseActivity {
    private static final String TAG_NAME = GuideReminderActivity.class.getSimpleName();
    private String[] hours = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"};
    private String[] mins = new String[]{"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"};


    private NumberPicker npHours;
    private NumberPicker npMins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_guide_reminder);

        initViews();
        initEvents();
    }

    private void initEvents() {
        findViewById(R.id.btn_save).setOnClickListener(view -> {
            Reminder reminder = new Reminder(
                    Utils.randomInt(),
                    "",
                    npHours.getValue() == 24 ? 0 : npHours.getValue(),
                    npMins.getValue(),
                    new boolean[]{true, true, true, true, true, true, true},
                    true,
                    false,
                    true,
                    Calendar.getInstance().getTimeInMillis()
            );
            reminder.setAlarm(this);
            reminder.setEnable(true);
            ReminderRepository.getInstance().update(reminder).subscribe();

            addDisposable(ReminderRepository.getInstance().insert(reminder).subscribe(() -> {
                AppSettings.getInstance().markedFirstOpen();
                try {
                    GuideReminderActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(GuideReminderActivity.this, MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();

                        }
                    });

                } catch (Exception e) {
                    AppSettings.getInstance().markedFirstOpen();
                    Intent intent = new Intent(GuideReminderActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }

            }));
        });
    }

    private void initViews() {
        npHours = findViewById(R.id.pk_hours);
        npHours.setMinValue(1);
        npHours.setMaxValue(24);
        npHours.setValue(8);
        npHours.setDisplayedValues(hours);
        npHours.setWrapSelectorWheel(true);

        npMins = findViewById(R.id.pk_mins);
        npMins.setMinValue(0);
        npMins.setMaxValue(59);
        npMins.setValue(0);
        npMins.setDisplayedValues(mins);
        npMins.setWrapSelectorWheel(true);
    }
}
