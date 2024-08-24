package com.nhn.fitness.ui.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.nhn.fitness.R;
import com.nhn.fitness.data.shared.AppSettings;
import com.nhn.fitness.ui.base.BaseActivity;
import com.nhn.fitness.ui.dialogs.BMIDialogFragment;
import com.nhn.fitness.ui.dialogs.BirthdayDialog;
import com.nhn.fitness.ui.interfaces.DialogResultListener;
import com.nhn.fitness.utils.DateUtils;
import com.nhn.fitness.utils.Utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicBoolean;


public class ProfileActivity extends BaseActivity implements DialogResultListener {

    private static final String TAG_NAME = ProfileActivity.class.getSimpleName();
    private AppCompatRadioButton rbKg, rbLb;
    private TextView txtWeight, txtHeight, txtBirthday, tvUnitKgLb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(getResources().getColor(R.color.white));
        setContentView(R.layout.activity_profile);

        initViews();
        initEvents();
        refreshData();
    }

    private void initEvents() {
        findViewById(R.id.row_change_kg_lb).setOnClickListener(view -> {
            displayDialogChangeKgLb();
        });
//        rbKg.setOnCheckedChangeListener((compoundButton, b) -> {
//            if (b) {
//                AppSettings.getInstance().setUnitType(0);
//                refreshData();
//            }
//        });
//        rbLb.setOnCheckedChangeListener((compoundButton, b) -> {
//            if (b) {
//                AppSettings.getInstance().setUnitType(1);
//                refreshData();
//            }
//        });
        View.OnClickListener onClickListener = view -> {
            new BMIDialogFragment(this).show(getSupportFragmentManager(), null);
        };
        findViewById(R.id.row_height).setOnClickListener(onClickListener);
        findViewById(R.id.row_weight).setOnClickListener(onClickListener);

        findViewById(R.id.row_birthday).setOnClickListener(view -> {
            new BirthdayDialog(this).show(getSupportFragmentManager(), null);
        });

    }

    private void displayDialogChangeKgLb() {
        Dialog dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_change_kg_lb);
        AtomicBoolean isClickKgLb = new AtomicBoolean(true);
        TextView tvKgCm= (TextView) dialog.findViewById(R.id.tv_kg_cm);
        TextView tvLbFt= (TextView) dialog.findViewById(R.id.tv_lb_ft);
        if (AppSettings.getInstance().getUnitType() == 0) {
            tvKgCm.setBackgroundResource(R.drawable.bg_change_unit_select);
            tvKgCm.setTextColor(getResources().getColor(R.color.blueLight));
            tvLbFt.setBackgroundColor(getResources().getColor(R.color.white));
            tvLbFt.setTextColor(getResources().getColor(R.color.text_unselect));
        } else {
            tvLbFt.setBackgroundResource(R.drawable.bg_change_unit_select);
            tvLbFt.setTextColor(getResources().getColor(R.color.blueLight));
            tvKgCm.setBackgroundColor(getResources().getColor(R.color.white));
            tvKgCm.setTextColor(getResources().getColor(R.color.text_unselect));
        }
        tvKgCm.setOnClickListener(view -> {
            tvLbFt.setBackgroundColor(getResources().getColor(R.color.white));
            tvLbFt.setTextColor(getResources().getColor(R.color.text_unselect));
            tvKgCm.setBackgroundResource(R.drawable.bg_change_unit_select);
            tvKgCm.setTextColor(getResources().getColor(R.color.blueLight));
            isClickKgLb.set(true);
        });
        tvLbFt.setOnClickListener(view -> {
            tvKgCm.setBackgroundColor(getResources().getColor(R.color.white));
            tvKgCm.setTextColor(getResources().getColor(R.color.text_unselect));
            tvLbFt.setBackgroundResource(R.drawable.bg_change_unit_select);
            tvLbFt.setTextColor(getResources().getColor(R.color.blueLight));
            isClickKgLb.set(false);
        });
        TextView btSave = (TextView) dialog.findViewById(R.id.btn_save);
        btSave.setOnClickListener(view -> {
            if (isClickKgLb.get()) {
                AppSettings.getInstance().setUnitType(0);
            } else {
                AppSettings.getInstance().setUnitType(1);
            }
            refreshData();
            dialog.dismiss();
        });
        TextView btnCancel = (TextView) dialog.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.show();
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void refreshData() {
        int type = AppSettings.getInstance().getUnitType();
        float weight = AppSettings.getInstance().getWeightDefault();
        float height = AppSettings.getInstance().getHeightDefault();
        long birthday = AppSettings.getInstance().getBirthday();
        if (type == 1) {
            weight = Utils.convertKgToLbs(weight);
            height = Utils.convertCmToFt(height);
           // rbLb.setChecked(true);
        } else {
          // rbKg.setChecked(true);
        }
        DecimalFormat df = new DecimalFormat("#.##");
        DecimalFormat df2 = new DecimalFormat("#");
        df.setRoundingMode(RoundingMode.HALF_UP);
        df2.setRoundingMode(RoundingMode.HALF_UP);

        txtWeight.setText(df.format(weight) + (type == 1 ? " LB" : " KG"));
        txtHeight.setText((type == 0 ? df2.format(height) : df.format(height)) + (type == 1 ? " FT" : " CM"));
        txtBirthday.setText(DateUtils.formatBirthday(birthday));
        if (AppSettings.getInstance().getUnitType() == 0){
            tvUnitKgLb.setText("Kg, Cm");
        }else tvUnitKgLb.setText("Lb, Ft");
    }

    private void initViews() {
//        Toolbar toolbar = findViewById(R.id.toolBar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("");
        findViewById(R.id.profile_action_bar).setOnClickListener(view -> {
            onBackPressed();
        });

        txtBirthday = findViewById(R.id.txt_birthday);
        txtHeight = findViewById(R.id.txt_height);
        txtWeight = findViewById(R.id.txt_weight);
        tvUnitKgLb = findViewById(R.id.tv_unit_kg_lb);

//        rbKg = findViewById(R.id.rb_kg);
//        rbLb = findViewById(R.id.rb_lb);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onResult(int type, Object value) {
        refreshData();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
