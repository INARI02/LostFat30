package com.nhn.fitness.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.nhn.fitness.utils.NotificationUtils;

public class MyBroadcastReceiver extends BroadcastReceiver {
    public static final String CANCEL_NOTIFY = "com.lubuteam.sellsource.losefat.cancel_notifycation";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent.getAction().equals(CANCEL_NOTIFY)) {
                int id = intent.getIntExtra("id", 0);
                if (id > 0) {
                    NotificationUtils.cancel(context, id);
                    Log.e("status", "cancel notify");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
