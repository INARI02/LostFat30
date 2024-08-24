package com.nhn.fitness.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.widget.AppCompatRadioButton;

import com.nhn.fitness.R;
import com.nhn.fitness.data.shared.AppSettings;
import com.nhn.fitness.ui.base.BaseActivity;

public class GuideGenderActivity extends BaseActivity {

    AppCompatRadioButton rbFemale, rbMale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_gender);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        initViews();
        initEvents();
    }

    private void initViews() {
        rbFemale = findViewById(R.id.rb_female);
        rbMale = findViewById(R.id.rb_male);
    }

    private void initEvents() {
        findViewById(R.id.btn_next).setOnClickListener(view -> {
            gotoNext();
        });
        rbFemale.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                AppSettings.getInstance().setGender(0);
            }
        });
        rbMale.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                AppSettings.getInstance().setGender(1);
            }
        });
    }

    private void gotoNext() {
        Intent intent = new Intent(this, GuideLevelActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
