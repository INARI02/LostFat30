package com.nhn.fitness.ui.base;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.nhn.fitness.data.shared.AppSettings;
import com.nhn.fitness.ui.activities.MainActivity;

import java.util.Locale;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseActivity extends AppCompatActivity {
    protected CompositeDisposable compositeDisposable = new CompositeDisposable();
    protected boolean fullscreen = false;
    protected FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !fullscreen) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.setNavigationBarColor(Color.BLACK);

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        initLocale();
    }

    public void initLocale() {
        String language = AppSettings.getInstance().getLanguage();

        if (AppSettings.getInstance().isFirstOpen()) {
            String current = Locale.getDefault().getLanguage();
            if (current.toLowerCase().contains("vi")) {
                AppSettings.getInstance().setLanguage(current);
                language = current;
            }
        }

        Locale myLocale = new Locale(language);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, MainActivity.class);
        refresh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(refresh);
        finish();
    }

    protected void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
        }
    }

    protected void setBottomNavColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.setNavigationBarColor(Color.BLACK);
        }
    }

    public void addFragment(
            Fragment fragment, int containerViewId,
            String TAG, boolean addToBackStack,
            int transit
    ) {
        if (TAG != null && findFragment(TAG) != null) {
            popFragment(TAG, transit);
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(containerViewId, fragment, TAG);
            commitTransaction(transaction, TAG, addToBackStack, transit);
        }
    }

    public void replaceFragment(
            Fragment fragment, int containerViewId,
            String TAG, boolean addToBackStack,
            int transit
    ) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerViewId, fragment, TAG);
        commitTransaction(transaction, TAG, addToBackStack, transit);
    }

    protected void popFragment(String TAG, int flag) {
        if (null != TAG) {
            getSupportFragmentManager().popBackStack(TAG, flag);
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    protected Fragment findFragment(String TAG) {
        return getSupportFragmentManager().findFragmentByTag(TAG);
    }

    private void commitTransaction(
            FragmentTransaction transaction, String TAG,
            boolean addToBackStack, int transit
    ) {
        if (addToBackStack) transaction.addToBackStack(TAG);
        if (transit != -1) transaction.setTransition(transit);
        transaction.commitAllowingStateLoss();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        if (Build.VERSION.SDK_INT >= 21) {
            super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
        } else {
            super.attachBaseContext(newBase);
        }
    }

    protected void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLocale();
    }

    private void updateLocale() {
        String language = AppSettings.getInstance().getLanguage();
        Locale myLocale = new Locale(language);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        res.updateConfiguration(conf, dm);
        if (!myLocale.getLanguage().equals(conf.locale.getLanguage())) {
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            Intent refresh = new Intent(this, this.getClass());
            refresh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(refresh);
            finish();
        }
    }

}
