package com.nhn.fitness.ui.customViews;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhn.fitness.R;
import com.nhn.fitness.ui.adapters.IndicatorAdapter;

public class StepProgressBarView extends FrameLayout {

    private RecyclerView recyclerView;
    private IndicatorAdapter adapter;
    private int count = 0;
    private int pos = 0;

    public StepProgressBarView(Context context) {
        super(context);
        init();
    }

    public StepProgressBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StepProgressBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StepProgressBarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.step_progress_run, this);
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
    }

    public void setup(int count) {
        this.count = count;
        adapter = new IndicatorAdapter(count);
        recyclerView.setAdapter(adapter);
    }

    public void progress(int pos) {
        this.pos = pos;
        adapter.setPos(pos);
        adapter.notifyDataSetChanged();
    }
}
