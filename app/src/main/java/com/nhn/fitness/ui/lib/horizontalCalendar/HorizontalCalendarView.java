package com.nhn.fitness.ui.lib.horizontalCalendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhn.fitness.R;
import com.nhn.fitness.ui.adapters.decoration.PreCachingLayoutManager;
import com.nhn.fitness.utils.DateUtils;
import com.nhn.fitness.utils.ViewUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class HorizontalCalendarView extends LinearLayout implements HorizontalCalendarListener, View.OnClickListener {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private TextView txtTitle;
    private HorizontalCalendarListener listener;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM, yyyy");
    private ArrayList<DateModel> list;
    private DateModel currentDayModel;
    private DateModel currentMonth;
    private int center = 4;
    private int current = 0;

    LinearLayout leftButton;
    LinearLayout rightButton;
    RecyclerView recyclerView;
    CalAdapter adapter;
    PreCachingLayoutManager linearLayoutManager;
    // For load more
    private int visibleThreshold = 50;
    private boolean loading = false;

    public void init() {
        initData();
        initViews();
        initEvents();

        updateMonthOnScroll(currentMonth);
        newDateSelected(list.get(current), current);
    }

    private void initData() {
        list = new ArrayList<>(DateUtils.generateDateForDialog());
        currentDayModel = new DateModel(Calendar.getInstance());
        currentMonth = currentDayModel;
        current = list.indexOf(currentDayModel);
    }

    private void initViews() {
        inflate(getContext(), R.layout.custom_calender_layout, this);
        txtTitle = findViewById(R.id.txt_title);
        leftButton = findViewById(R.id.swipe_left);
        rightButton = findViewById(R.id.swipe_right);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(100);

        adapter = new CalAdapter(getContext(), list, current);
        adapter.setListener(this);
        linearLayoutManager = new PreCachingLayoutManager(
                getContext(),
                LinearLayoutManager.HORIZONTAL,
                false,
                ViewUtils.getHeightDevicePixel(ViewUtils.getActivity(getContext())) * 2
        );
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void updateTitle() {
        txtTitle.setText(simpleDateFormat.format(currentMonth.getCalendar().getTime()));
    }

    private void initEvents() {
        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleIndex = current + 8;
            int firstVisibleIndex = current;
            int pos = current + 4;
            boolean isScroll = false;

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!isScroll && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    isScroll = true;
                    recyclerView.postDelayed(() -> {
                        adapter.handleClick(pos);
                        isScroll = false;
                    }, 500);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                final int lastVisibleItem, totalItemCount, firstVisibleItem;
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleIndex = linearLayoutManager.findLastVisibleItemPosition();
                firstVisibleIndex = linearLayoutManager.findFirstVisibleItemPosition();
                center = (int) Math.floor(Math.abs(lastVisibleIndex - firstVisibleIndex) / 2f);
                if (dx > 0) {
                    pos = firstVisibleIndex + center;
                    if (!list.get(pos).equals(currentMonth)) {
                        updateMonthOnScroll(list.get(pos));
                    }
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                } else {
                    pos = lastVisibleIndex - center;
                    if (!list.get(pos).equals(currentMonth)) {
                        updateMonthOnScroll(list.get(pos));
                    }
                    firstVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    lastVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                }

                // Load more => Don't use
                if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    // End has been reached
                    // Do something
                    // Handle load more right
                    loading = true;
                    loadMoreRight();
                    // Log.e("status", "loadmore right");
                }
                if (!loading && (firstVisibleItem - visibleThreshold) <= 0 && DateUtils.getIdDay(list.get(0).getCalendar()) > 17167) { // 1/1/2017 + 1
//                    Log.e("status", firstVisibleItem - visibleThreshold + "");
                    // End has been reached
                    // Do something
                    // Handle load more left
                    loading = true;
                    loadMoreLeft();
//                    Log.e("status", "loadmore left");
                }
            }
        });

        linearLayoutManager.scrollToPosition(current - center);
    }

    private void loadMoreRight() {
        addDisposable(Flowable.just(list.get(list.size() - 1).getCalendar())
                .subscribeOn(Schedulers.io())
                .flatMap((Function<Calendar, Flowable<List<DateModel>>>) calendar -> {
                    ArrayList<DateModel> result = new ArrayList<>(DateUtils.generateDateForDialogAfterDate(calendar));
                    return Flowable.just(result);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    int pos = list.size();
                    list.addAll(response);
                    adapter.notifyItemInserted(pos);
                    loading = false;
                }));
    }

    private void loadMoreLeft() {
        addDisposable(Flowable.just(list.get(0).getCalendar())
                .subscribeOn(Schedulers.io())
                .flatMap((Function<Calendar, Flowable<List<DateModel>>>) calendar -> {
                    ArrayList<DateModel> result = new ArrayList<>(DateUtils.generateDateForDialogBeforeDate(calendar));
                    return Flowable.just(result);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    int count = response.size();
                    Collections.reverse(response);
                    Collections.reverse(list);
                    list.addAll(response);
                    Collections.reverse(list);
                    adapter.notifyItemRangeInserted(0, count);
                    loading = false;
                }));
    }

    public HorizontalCalendarView(Context context) {
        super(context);
    }

    public HorizontalCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setListener(HorizontalCalendarListener listener) {
        this.listener = listener;
    }

    @Override
    public void updateMonthOnScroll(DateModel selectedDate) {
        currentMonth = selectedDate;
        listener.updateMonthOnScroll(selectedDate);
        updateTitle();
    }

    @Override
    public void newDateSelected(DateModel selectedDate, int pos) {
        listener.newDateSelected(selectedDate, pos);
        linearLayoutManager.scrollToPositionWithOffset(pos - center, 0);
    }

    protected void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        compositeDisposable.clear();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.swipe_left) {
            leftButton.setOnClickListener(null);
            Calendar calendar = (Calendar) currentMonth.getCalendar().clone();
            calendar.add(Calendar.MONTH, -1);
            DateModel dateModel = new DateModel(calendar);
            int pos = list.indexOf(dateModel);
            adapter.handleClick(pos);
            postDelayed(() -> leftButton.setOnClickListener(HorizontalCalendarView.this), 1000);
        } else if (view.getId() == R.id.swipe_right) {
            rightButton.setOnClickListener(null);
            Calendar calendar = (Calendar) currentMonth.getCalendar().clone();
            calendar.add(Calendar.MONTH, 1);
            DateModel dateModel = new DateModel(calendar);
            int pos = list.indexOf(dateModel);
            adapter.handleClick(pos);
            postDelayed(() -> rightButton.setOnClickListener(HorizontalCalendarView.this), 1000);
        }
    }
}
