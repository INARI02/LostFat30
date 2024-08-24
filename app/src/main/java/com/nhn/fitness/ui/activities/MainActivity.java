package com.nhn.fitness.ui.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nhn.fitness.R;
import com.nhn.fitness.data.model.MessageEvent;
import com.nhn.fitness.ui.adapters.HomePagerAdapter;
import com.nhn.fitness.ui.base.BaseActivity;
import com.nhn.fitness.ui.customViews.CustomViewPager;
import com.nhn.fitness.utils.NotificationUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.Random;


public class MainActivity extends BaseActivity {
    private CustomViewPager viewPager;
    private BottomNavigationView bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatusBarColor(getResources().getColor(R.color.white));
        viewPager = findViewById(R.id.pager);
        viewPager.setPagingEnabled(false);
        viewPager.setOffscreenPageLimit(4);
        initObservers();
        initEvents();
        initViews();
    }

    private void initEvents() {

    }

    private void initObservers() {

    }

    private void initViews() {
        setStatusBarColor(getResources().getColor(R.color.white));

        initBottomMenu();
        setUpAdapter();

        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.HOUR_OF_DAY) == 17 && calendar.get(Calendar.MINUTE) == 23 && calendar.get(Calendar.SECOND) == 0){
            NotificationUtils.showAlarmReminder(this, new Random().nextInt(Integer.MAX_VALUE));

            if (Calendar.getInstance().after(calendar)) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            Intent intent = new Intent(MainActivity.this, NotificationUtils.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
            }
        }
//        calendar.set(Calendar.HOUR_OF_DAY, 16);
//        calendar.set(Calendar.MINUTE, 30);
//        calendar.set(Calendar.SECOND, 0);

    }

    private void setUpAdapter() {
        HomePagerAdapter adapter = new HomePagerAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0 :
                        bottomBar.getMenu().findItem(R.id.tab_training).setChecked(true);
                        break;
                    case 1 :
                        bottomBar.getMenu().findItem(R.id.tab_workouts).setChecked(true);
                        break;
                    case 2 :
                        bottomBar.getMenu().findItem(R.id.tab_report).setChecked(true);
                        break;
                    case 3 :
                        bottomBar.getMenu().findItem(R.id.tab_settings).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.getKey().equals(MessageEvent.OPEN_WORKOUT_EVENT)) {
            bottomBar.setSelectedItemId(R.id.tab_training);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    private void initBottomMenu() {
        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.tab_training:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.tab_workouts:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.tab_report:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.tab_settings:
                        viewPager.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });
    }



    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}
