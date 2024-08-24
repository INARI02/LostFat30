package com.nhn.fitness.ui.base;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.nhn.fitness.R;


import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

public class BaseApplication extends Application {
    private static BaseApplication instance;


    @Override
    public void onCreate() {
        super.onCreate();
        Glide.get(this).setMemoryCategory(MemoryCategory.HIGH);
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath(String.valueOf(R.string.font_regular))
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());


        instance = this;
    }


    public static BaseApplication getInstance() {
        if (instance == null) {
            Log.e("status", "application null");
        }
        return instance;
    }
}
