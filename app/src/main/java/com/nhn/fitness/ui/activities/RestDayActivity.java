package com.nhn.fitness.ui.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;


import com.nhn.fitness.R;
import com.nhn.fitness.data.model.DailySectionUser;
import com.nhn.fitness.data.repositories.DailySectionRepository;
import com.nhn.fitness.ui.base.BaseActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;


public class RestDayActivity extends BaseActivity {

    private static final String TAG_NAME = RestDayActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_day);
        setStatusBarColor(getResources().getColor(R.color.white));
        initViews();
    }

    @SuppressLint("CheckResult")
    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.btn_ok).setOnClickListener(view -> {
            DailySectionUser dailySectionUser = getIntent().getParcelableExtra("challenge");
            if (dailySectionUser.isCompleted()) {
                onBackPressed();
            } else {
                dailySectionUser.setCompleted(true);
                dailySectionUser.setProgress(100f);
                DailySectionRepository.getInstance().update(dailySectionUser)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            DailySectionRepository.getInstance().nextDaily(dailySectionUser);
                            onBackPressed();
                        });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        finish();
    }

}
