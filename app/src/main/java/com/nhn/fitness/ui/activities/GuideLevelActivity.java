package com.nhn.fitness.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.nhn.fitness.R;
import com.nhn.fitness.data.shared.AppSettings;
import com.nhn.fitness.ui.base.BaseActivity;

public class GuideLevelActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_level);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
        initViews();
        initEvents();
    }

    private void initEvents() {
        findViewById(R.id.card_1).setOnClickListener(view -> {
            AppSettings.getInstance().setLevel(1);
            gotoNext();
        });
        findViewById(R.id.card_2).setOnClickListener(view -> {
            AppSettings.getInstance().setLevel(2);
            gotoNext();
        });
        findViewById(R.id.card_3).setOnClickListener(view -> {
            AppSettings.getInstance().setLevel(3);
            gotoNext();
        });
    }

    private void initViews() {
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    private void gotoNext() {
        Intent intent = new Intent(this, GuideReminderActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
}
