package com.nhn.fitness.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import com.nhn.fitness.R;
import com.nhn.fitness.data.shared.AppSettings;
import com.nhn.fitness.ui.base.BaseActivity;

public class ChooseLevelActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_level);
        setStatusBarColor(getResources().getColor(R.color.white));

        initViews();
        initEvents();
    }

    private void initEvents() {
        findViewById(R.id.btn_choose_1).setOnClickListener(view -> {
            callback(1);
        });
        findViewById(R.id.btn_choose_2).setOnClickListener(view -> {
            callback(2);
        });
        findViewById(R.id.btn_choose_3).setOnClickListener(view -> {
            callback(3);
        });
        findViewById(R.id.btn_choose_4).setOnClickListener(view -> {
            callback(4);
        });
    }

    private void callback(int pos) {
        AppSettings.getInstance().setLevel(pos);
        Intent intent = getIntent();
        intent.putExtra("com/nhn/fitness/data", pos);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void initViews() {
        int pos = AppSettings.getInstance().getLevel();
        AppCompatTextView btn1, btn2, btn3, btn4;
        btn1 = findViewById(R.id.btn_choose_1);
        btn2 = findViewById(R.id.btn_choose_2);
        btn3 = findViewById(R.id.btn_choose_3);
        btn4 = findViewById(R.id.btn_choose_4);
        if (pos == 1) {
            btn1.setBackgroundResource(R.drawable.bg_btn_choose);
        } else if (pos == 2) {
            btn2.setBackgroundResource(R.drawable.bg_btn_choose);
        } else if (pos == 3) {
            btn3.setBackgroundResource(R.drawable.bg_btn_choose);
        } else if (pos == 4) {
            btn4.setBackgroundResource(R.drawable.bg_btn_choose);
        }
        findViewById(R.id.iv_back_select_plans).setOnClickListener(view -> {
            onBackPressed();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}
