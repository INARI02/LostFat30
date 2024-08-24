package com.nhn.fitness.ui.customViews;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.nhn.fitness.R;
import com.nhn.fitness.data.model.DayHistoryModel;

public class SingleDayView extends FrameLayout {
    private DayHistoryModel dayHistoryModel;

    public SingleDayView(Context context) {
        super(context);
    }

    public SingleDayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SingleDayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SingleDayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.single_day_layout, this);
    }

    public void setData(DayHistoryModel data) {
        dayHistoryModel = data;
        ((TextView) findViewById(R.id.day_name)).setText(dayHistoryModel.getDayString());
        ((TextView) findViewById(R.id.current_date)).setText(dayHistoryModel.getDateString());

        if (dayHistoryModel.isHasWorkout()) {
            findViewById(R.id.current_date).setVisibility(View.GONE);
            findViewById(R.id.date_image).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.current_date).setVisibility(View.VISIBLE);
            findViewById(R.id.date_image).setVisibility(View.GONE);
        }
    }
}
