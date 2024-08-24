package com.nhn.fitness.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.nhn.fitness.R;
import com.nhn.fitness.ui.base.BaseActivity;

public class OnBoardingActivity02 extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_onboarding_02);
        initEvents();
    }

    private void initEvents() {
        findViewById(R.id.iv_next_boarding).setOnClickListener(view -> {
            gotoNext();
        });
    }

    private void gotoNext() {
        Intent intent = new Intent(this, OnBoardingActivity03.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
}