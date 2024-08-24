package com.nhn.fitness.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.nhn.fitness.R;
import com.nhn.fitness.service.MyBroadcastReceiver;
import com.nhn.fitness.ui.activities.SplashActivity;

public class NotificationUtils {

    public static void showAlarmReminder(Context context, int id) {
        try {
            Intent intent = new Intent(context, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            Intent snoozeIntent = new Intent(context, MyBroadcastReceiver.class);
            snoozeIntent.setAction(MyBroadcastReceiver.CANCEL_NOTIFY);
            snoozeIntent.putExtra("id", id);
            PendingIntent snoozePendingIntent =
                    PendingIntent.getBroadcast(context, 0, snoozeIntent, 0);

            Intent startIntent = new Intent(context, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent startPendingIntent =
                    PendingIntent.getActivity(context, 0, startIntent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setSmallIcon(R.drawable.ic_noti_fitness)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.logo))
                    .setContentTitle(context.getResources().getString(R.string.title_notify))
                    .setContentText(context.getResources().getString(R.string.content_notify))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .addAction(0, context.getResources().getString(R.string.snooze), snoozePendingIntent)
                    .addAction(0, context.getResources().getString(R.string.start_notify), startPendingIntent);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(context);
                builder.setChannelId(CHANNEL_ID);
            }

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(id, builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cancel(Context context, int id) {
        try {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.cancel(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "Female Fitness - Women Workout";
                String description = "Female Fitness - Women Workout";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String CHANNEL_ID = "811";

}