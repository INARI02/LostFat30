package com.nhn.fitness.ui.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.nhn.fitness.R;
import com.nhn.fitness.data.model.DayHistoryModel;
import com.nhn.fitness.data.repositories.DayHistoryRepository;
import com.nhn.fitness.ui.base.BaseFragment;
import com.nhn.fitness.ui.fragments.charts.StickyDateAxisValueFormatter;
import com.nhn.fitness.ui.fragments.charts.YAxisValueFormatter;
import com.nhn.fitness.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CaloriesChartFragment extends BaseFragment {

    private BarChart chart;
    private TextView sticky;
    private TextView labelY;
    private ArrayList<DayHistoryModel> listDays = new ArrayList<>();

    public CaloriesChartFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_calories_chart, container, false);
        initViews();
        initObservers();
        initEvents();
        loadData();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void initViews() {
        super.initViews();
        labelY = rootView.findViewById(R.id.label_y);
        sticky = rootView.findViewById(R.id.sticky_label);
        chart = rootView.findViewById(R.id.test_chart);

        List<BarEntry> data = new ArrayList<>();
        initChartView(data);
    }

    private void refresh() {
        loadData();
    }

    private void loadData() {
        addDisposable(DayHistoryRepository.getInstance().getAllCalories()
                .subscribe(response -> {
//                    Log.e("status", "chart size " + response.size() + "");
                    if (response != null && response.size() > 0) {
                        setData(response);
                    }
                }));
    }

    private void setData(List<DayHistoryModel> list) {
//        Log.e("status", "chart size " + list.size() + "");
//        Log.e("status", list.toString());
        this.listDays.clear();
        this.listDays.addAll(list);
        List<BarEntry> data = new ArrayList<>();
        for (DayHistoryModel dayHistoryModel : list) {
            data.add(new BarEntry(dayHistoryModel.getDayCount(), dayHistoryModel.getCalories()));
        }
        initYAxis();
        setEntry(data);
    }

    private void initYAxis() {
        final YAxis xy = chart.getAxisLeft();
        xy.setAxisMaximum(530.0f);
        xy.setGranularity(100f);
        xy.setAxisMinimum(0f);
        xy.setTextSize(10f);
        xy.setValueFormatter(new YAxisValueFormatter());
        xy.setGranularityEnabled(true);
        xy.setGridColor(getResources().getColor(R.color.text_gray_light));
        xy.setDrawGridLines(true);
        xy.setDrawAxisLine(false);
    }


    private void setEntry(List<BarEntry> entries) {
        BarData barData = chart.getBarData();
        BarDataSet dataSet = (BarDataSet) barData.getDataSetByIndex(0);

        dataSet.getValues().clear();
        dataSet.getValues().addAll(entries);

        dataSet.notifyDataSetChanged();
        barData.notifyDataChanged();
        chart.notifyDataSetChanged();
        chart.invalidate();
    }

    private void initChartView(List<BarEntry> entries) {
        BarDataSet lds = new BarDataSet(entries, "date");
        lds.setBarBorderWidth(0f);
        lds.setColor(getResources().getColor(R.color.blueLight));
        lds.setValueTextSize(10f);
        lds.setValueTextColor(getResources().getColor(R.color.blueLight));
        BarData ld = new BarData(lds);
        ld.setDrawValues(true);
        ld.setBarWidth(0.6f);
        chart.setData(ld);

        final float textSize = 10f;
        final XAxis xa = chart.getXAxis();
        xa.setGranularity(1f);
        xa.setGranularityEnabled(true);
        xa.setLabelCount(7, false);
        xa.setAxisMinimum(10000f);
        xa.setAxisMaximum(25000f);
        xa.setValueFormatter(new StickyDateAxisValueFormatter(chart, sticky));
        xa.setPosition(XAxis.XAxisPosition.BOTTOM);
        xa.setTextSize(textSize);
        xa.setDrawAxisLine(false);
        xa.setDrawGridLines(false);
        initYAxis();

        chart.setPadding(0, 0, 0, 0);
        chart.setPinchZoom(false);
        chart.setScaleEnabled(false);
        chart.setDoubleTapToZoomEnabled(false);
        sticky.setTextSize(textSize);

        chart.setVisibleXRange(7f, 7f);
        chart.getAxisRight().setEnabled(false);
        Description desc = new Description();
        desc.setText("");
        chart.setDescription(desc);
        chart.getLegend().setEnabled(false);
        chart.invalidate();
        chart.moveViewToX(DateUtils.getIdNow() - 4);
    }

    @Override
    protected void initEvents() {
        super.initEvents();

    }

    @Override
    protected void initObservers() {
        super.initObservers();
    }

}
