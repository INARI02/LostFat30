package com.nhn.fitness.ui.customViews;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.RequiresApi;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.DayHistoryModel;

import java.util.List;

public class CurrentWeekView extends FrameLayout {


    public CurrentWeekView(Context context) {
        super(context);
    }

    public CurrentWeekView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CurrentWeekView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CurrentWeekView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        View.inflate(getContext(), R.layout.current_week_layout, this);
    }

    public void setData(List<DayHistoryModel> list) {
        if (list != null && list.size() == 7) {
            ((SingleDayView) findViewById(R.id.day_one)).setData(list.get(0));
            ((SingleDayView) findViewById(R.id.day_two)).setData(list.get(1));
            ((SingleDayView) findViewById(R.id.day_three)).setData(list.get(2));
            ((SingleDayView) findViewById(R.id.day_four)).setData(list.get(3));
            ((SingleDayView) findViewById(R.id.day_five)).setData(list.get(4));
            ((SingleDayView) findViewById(R.id.day_six)).setData(list.get(5));
            ((SingleDayView) findViewById(R.id.day_seven)).setData(list.get(6));
        }
    }
}
