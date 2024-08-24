package com.nhn.fitness.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.nhn.fitness.R;
import com.nhn.fitness.ui.base.BaseActivity;

public class OnBoardingActivity01 extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_onboarding_01);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                },101);
            }
        }
        initEvents();
    }

    private void initEvents() {
        findViewById(R.id.iv_next_boarding).setOnClickListener(view -> {
            gotoNext();
        });
    }

    private void gotoNext() {
        Intent intent = new Intent(this, OnBoardingActivity02.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
}
