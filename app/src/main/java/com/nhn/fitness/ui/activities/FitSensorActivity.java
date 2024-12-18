package com.nhn.fitness.ui.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.Step;
import com.nhn.fitness.data.repositories.StepRepository;
import com.nhn.fitness.data.room.AppDatabase;
import com.nhn.fitness.ui.base.BaseActivity;
import com.nhn.fitness.utils.NotificationFit;
import com.nhn.fitness.utils.Utils;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FitSensorActivity extends BaseActivity implements SensorEventListener {
    final static int DEFAULT_GOAL = 10000;
    private TextView stepsView, tvTotal, tvFit, tvUnit;
    private PieModel sliceGoal, sliceCurrent;
    private PieChart pieChart;
    private BarChart barChart;
    final static float DEFAULT_STEP_SIZE = Locale.getDefault() == Locale.US ? 2.5f : 75f;
    final static String DEFAULT_STEP_UNIT = Locale.getDefault() == Locale.US ? "ft" : "cm";

    private int total_start, goal, since_boot, total_days;
    public final static NumberFormat formatter = NumberFormat.getInstance(Locale.getDefault());
    private boolean showSteps = true;
    private Sensor mSensor;
    private AppDatabase mAppDatabase;
    private StepRepository mStepRepository;
    private boolean flagOpen = true;
    private int todayOffset = 0; // So buoc da di trong ngay hom nay truoc khi bat dau session
    private int startSteps = 0; // So buoc di bat dau khi mo session sensor nay
    private int currentSessionSteps = 0; // Tong so buoc di trong session nay

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fit_sensor);
        checkNeededPermission();
        initViews();
        initEvents();
    }

    private void checkNeededPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                        1);
            }
        }
    }

    private void initEvents() {

    }

    private void initViews() {
        mAppDatabase = AppDatabase.getInstance();
        mStepRepository = StepRepository.getInstance();
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationFit.startForegroundService(this,
                    new Intent(this, SensorEventListener.class));
        } else {
            startService(new Intent(this, SensorEventListener.class));
        }
        tvUnit = findViewById(R.id.unit);
        tvFit = findViewById(R.id.tv_fit);
        tvTotal = findViewById(R.id.tv_fit_total);
        stepsView = findViewById(R.id.steps);
        pieChart = findViewById(R.id.pieGraph);
        barChart = findViewById(R.id.barGraph);
        // slice for the steps taken today
        sliceCurrent = new PieModel("", 0, Color.parseColor("#99CC00"));
        pieChart.addPieSlice(sliceCurrent);

        // slice for the "missing" steps until reaching the goal
        sliceGoal = new PieModel("", DEFAULT_GOAL, Color.parseColor("#CC0000"));
        pieChart.addPieSlice(sliceGoal);

        pieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                showSteps = !showSteps;
                stepsDistanceChanged();
            }
        });

        pieChart.setDrawValueInPie(false);
        pieChart.setUsePieRotation(true);
        pieChart.startAnimation();

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (mSensor == null) {
            showToast("No step sensor");
        } else {
            showToast("Register sensor");
            sensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
        }

    }

    private void stepsDistanceChanged() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (showSteps) {
                            tvUnit.setText("steps");
                        } else {
                            String unit = getSharedPreferences("pedometer", Context.MODE_PRIVATE)
                                    .getString("stepsize_unit", DEFAULT_STEP_UNIT);
                            if (unit.equals("cm")) {
                                unit = "km";
                            } else {
                                unit = "mi";
                            }
                            tvUnit.setText(unit);
                        }

                        updatePie();
                        updateBars();
                        try {
                            Log.i("KMFG", "run: ");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void updatePie() {
        // todayOffset might still be Integer.MIN_VALUE on first start
        int steps_today = Math.max(todayOffset + currentSessionSteps, 0);
//        if (since_boot-steps_today >= 14) {
//            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//// Vibrate for 500 milliseconds
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                v.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
//            } else {
//                //deprecated in API 26
//                v.vibrate(1000);
//            }
//            showToast("Bạn có đang chạy không?");
//        }
        sliceCurrent.setValue(steps_today);
        Log.i("KMFG", "updatePie: " + steps_today);
        if (goal - steps_today > 0) {
            // goal not reached yet
            if (pieChart.getData().size() == 1) {
                // can happen if the goal value was changed: old goal value was
                // reached but now there are some steps missing for the new goal
                pieChart.addPieSlice(sliceGoal);
            }
            sliceGoal.setValue(goal - steps_today);
        } else {
            // goal reached
            pieChart.clearChart();
            pieChart.addPieSlice(sliceCurrent);
        }
        pieChart.update();
        if (showSteps) {
            // Tong so buoc di duoc trong hom nay
            stepsView.setText(formatter.format(steps_today));
            // Tong so buoc di duoc trong tat ca cac ngay
            tvTotal.setText(formatter.format(total_start + steps_today));
            // So buoc di trung binh trong mot ngay
            tvFit.setText(formatter.format((total_start + steps_today) / total_days));
        } else {
            // update only every 10 steps when displaying distance
            SharedPreferences prefs = getSharedPreferences("pedometer", Context.MODE_PRIVATE);
            float stepsize = prefs.getFloat("stepsize_value", DEFAULT_STEP_SIZE);
            float distance_today = steps_today * stepsize;
            float distance_total = (total_start + steps_today) * stepsize;
            if (prefs.getString("stepsize_unit", DEFAULT_STEP_UNIT)
                    .equals("cm")) {
                distance_today /= 100000;
                distance_total /= 100000;
            } else {
                distance_today /= 5280;
                distance_total /= 5280;
            }
            stepsView.setText(formatter.format(distance_today));
            tvTotal.setText(formatter.format(distance_total));
            tvFit.setText(formatter.format(distance_total / total_days));
        }
    }

    /**
     * Updates the bar graph to show the steps/distance of the last week. Should
     * be called when switching from step count to distance.
     */
    private void updateBars() {
        SimpleDateFormat df = new SimpleDateFormat("E", Locale.getDefault());
        if (barChart.getData().size() > 0) barChart.clearChart();
        int steps;
        float distance, stepsize = DEFAULT_STEP_SIZE;
        boolean stepsize_cm = true;
        if (!showSteps) {
            // load some more settings if distance is needed
            SharedPreferences prefs = getSharedPreferences("pedometer", Context.MODE_PRIVATE);
            stepsize = prefs.getFloat("stepsize_value", DEFAULT_STEP_SIZE);
            stepsize_cm = prefs.getString("stepsize_unit", DEFAULT_STEP_UNIT)
                    .equals("cm");
        }
        barChart.setShowDecimal(!showSteps); // show decimal in distance view only
        BarModel bm;
        List<Step> lastEntries = mAppDatabase.stepDao().getLastEntries(8);
        for (int i = lastEntries.size() - 1; i >= 0; i--) {
            Step current = lastEntries.get(i);
            steps = current.getSteps();
            if (steps > 0) {
                bm = new BarModel(df.format(new Date(current.getDate())), 0,
                        steps > goal ? Color.parseColor("#99CC00") : Color.parseColor("#0099cc"));
                if (showSteps) {
                    bm.setValue(steps);
                } else {
                    distance = steps * stepsize;
                    if (stepsize_cm) {
                        distance /= 100000;
                    } else {
                        distance /= 5280;
                    }
                    distance = Math.round(distance * 1000) / 1000f; // 3 decimals
                    bm.setValue(distance);
                }
                barChart.addBar(bm);
            }
        }
        if (barChart.getData().size() > 0) {
            barChart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    //   Dialog_Statistics.getDialog(getActivity(), since_boot).show();
                }
            });
            barChart.startAnimation();
        } else {
            barChart.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAccuracyChanged(final Sensor sensor, int accuracy) {
        // won't happen
    }

    @Override
    public void onSensorChanged(final SensorEvent event) {
        Log.d("MinhNTn", "onSensorChanged: " + event.values[0]);
        // So buoc di hien tai trong session nay
        int currentSteps = 0;
        if (flagOpen) {
            flagOpen = false;
            startSteps = (int) event.values[0];
        }
        if (event.values[0] > Integer.MAX_VALUE || event.values[0] == 0) {
            return;
        }
        currentSteps = (int) event.values[0];
        since_boot = (int) event.values[0];
        currentSessionSteps = currentSteps - startSteps;
        updatePie();
    }

    @Override
    public void onResume() {
        super.onResume();

        //  if (BuildConfig.DEBUG) db.logState();
        // read todays offset
        todayOffset = mStepRepository.getSteps(Utils.getToday());

        SharedPreferences prefs = getSharedPreferences("pedometer", Context.MODE_PRIVATE);

        goal = prefs.getInt("goal", DEFAULT_GOAL);
        since_boot = mStepRepository.getCurrentSteps();
        int pauseDifference = since_boot - prefs.getInt("pauseCount", since_boot);

        // register a sensorlistener to live update the UI if a step is taken
        SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (sensor == null) {
            new AlertDialog.Builder(this).setTitle("No sensor")
                    .setMessage("No sensor explain")
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(final DialogInterface dialogInterface) {
                            finish();
                        }
                    }).setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create().show();
        } else {
            sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI, 0);
        }

        since_boot -= pauseDifference;

        total_start = mStepRepository.getTotalWithoutToday();
        total_days = mStepRepository.getDays();

        stepsDistanceChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            SensorManager sm =
                    (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            sm.unregisterListener(this);
        } catch (Exception e) {

        }
        mStepRepository.saveCurrentSteps(Utils.getToday(), todayOffset + currentSessionSteps);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //TODO do something

        } else {
            finish();
        }
    }
}
