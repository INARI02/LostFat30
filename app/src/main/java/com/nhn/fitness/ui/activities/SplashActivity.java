package com.nhn.fitness.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.nhn.fitness.BuildConfig;
import com.nhn.fitness.R;
import com.nhn.fitness.data.room.AppDatabase;
import com.nhn.fitness.data.room.AppDatabaseConst;
import com.nhn.fitness.data.shared.AppSettings;
import com.nhn.fitness.ui.base.BaseActivity;
import com.nhn.fitness.ui.base.BaseApplication;
import com.nhn.fitness.ui.interfaces.DatabaseListener;

import io.reactivex.schedulers.Schedulers;



public class SplashActivity extends BaseActivity implements DatabaseListener {

    private int timeout = 1000;
    ProgressBar pbLoadData;
    TextView tvStatus;
    Handler mHandler;
    Runnable r;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_splash);
        /**
         *  Important
         */
        if (BuildConfig.DB_VERSION > AppSettings.getInstance().getDBVersion()) {
            AppSettings.getInstance().setDbVersion(BuildConfig.DB_VERSION);

            // Clear and create const db
            AppDatabaseConst.clearInstance();
            AppDatabaseConst.copyAttachedDatabase(BaseApplication.getInstance(), true);
            AppDatabaseConst.getInstance()
                    .sectionDao().getAll()
                    .subscribeOn(Schedulers.single())
                    .observeOn(Schedulers.single())
                    .subscribe(response -> {
                        Log.e("status", "result: " + response.size());
                        AppDatabase.initAppDatabase(this);
                    }, Throwable::printStackTrace);

            // Init Database
            AppSettings.getInstance().setLastVersion(BuildConfig.VERSION_CODE);
        }else{
            mHandler = new Handler();
            r = new Runnable() {
                @Override
                public void run() {
                    gotoHome();

                }
            };

            mHandler.postDelayed(r, timeout);

        }


    }

    private void gotoHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
    private void gotoNext() {
        Intent intent = new Intent(this, OnBoardingActivity01.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
//    private void gotoGuide() {
//        Intent intent = new Intent(this, GuideGenderActivity.class);
//        startActivity(intent);
//        overridePendingTransition(0, 0);
//        finish();
//    }

    @Override
    public void insertAllCompleted() {
        if (AppSettings.getInstance().isFirstOpen()) {
            gotoNext();
        }else{
            gotoHome();
        }
    }
}
