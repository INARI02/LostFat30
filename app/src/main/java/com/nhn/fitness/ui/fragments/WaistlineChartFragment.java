package com.nhn.fitness.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.nhn.fitness.R;
import com.nhn.fitness.data.model.DayHistoryModel;
import com.nhn.fitness.data.repositories.DayHistoryRepository;
import com.nhn.fitness.data.room.AppDatabase;
import com.nhn.fitness.data.shared.AppSettings;
import com.nhn.fitness.ui.base.BaseFragment;
import com.nhn.fitness.ui.dialogs.WaistlineDialogFragment;
import com.nhn.fitness.ui.fragments.charts.StickyDateAxisValueFormatter;
import com.nhn.fitness.ui.fragments.charts.YAxisValueFormatter;
import com.nhn.fitness.ui.interfaces.DialogResultListener;
import com.nhn.fitness.utils.DateUtils;
import com.nhn.fitness.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WaistlineChartFragment extends BaseFragment implements DialogResultListener {

    private LineChart chart;
    private TextView sticky;
    private TextView labelY;
    private TextView txtUnit;
    private TextView txtLoading;
    private ArrayList<DayHistoryModel> listDays = new ArrayList<>();

    private int unitType = 0;

    public WaistlineChartFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_waistline_chart, container, false);
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
        unitType = AppSettings.getInstance().getUnitType();

        labelY = rootView.findViewById(R.id.label_y);
        txtUnit = rootView.findViewById(R.id.txt_unit);
        sticky = rootView.findViewById(R.id.sticky_label);
        txtLoading = rootView.findViewById(R.id.txt_loading);
        txtLoading.setVisibility(View.GONE);
        chart = rootView.findViewById(R.id.test_chart);

        int unitType = AppSettings.getInstance().getUnitType();
        String unitString = unitType == 0 ? "CM" : "INCH";
        float value = unitType == 0 ? AppSettings.getInstance().getWaistlineDefault() : Utils.convertCmToInch(AppSettings.getInstance().getWaistlineDefault());
        ((TextView) rootView.findViewById(R.id.txt_current)).setText(String.format(Locale.US, "%.2f %s", value, unitString));
        ((TextView) rootView.findViewById(R.id.txt_minimum)).setText(String.format(Locale.US, "%.2f %s", value, unitString));
        ((TextView) rootView.findViewById(R.id.txt_maximum)).setText(String.format(Locale.US, "%.2f %s", value, unitString));

        List<Entry> data = new ArrayList<>();
        initChartView(data);
    }

    private void refresh() {
        unitType = AppSettings.getInstance().getUnitType();

        loadData();
    }

    private void loadData() {
        addDisposable(DayHistoryRepository.getInstance().getAllWaistline()
                .subscribe(response -> {
                    if (response != null && response.size() > 0) {
                        setData(response);
                    }
                }));
    }

    private void setData(List<DayHistoryModel> list) {
//        Log.e("status", list.size() + "");
//        Log.e("status", list.toString());
        this.listDays.clear();
        this.listDays.addAll(list);
        List<Entry> data = new ArrayList<>();
        for (DayHistoryModel dayHistoryModel : list) {
            data.add(new Entry(dayHistoryModel.getDayCount(), unitType == 0 ? dayHistoryModel.getWaistline() : Utils.convertCmToInch(dayHistoryModel.getWaistline())));
        }
        initYAxis();
        setEntry(data);
    }

    private void initYAxis() {
        if (unitType == 0) {
            labelY.setText("cm");
            txtUnit.setText("cm");
            final YAxis xy = chart.getAxisLeft();
            xy.setAxisMaximum(180.0f);
            xy.setGranularity(20f);
            xy.setAxisMinimum(0f);
            xy.setTextSize(10f);
            xy.setValueFormatter(new YAxisValueFormatter());
            xy.setGranularityEnabled(true);
            xy.setGridColor(getResources().getColor(R.color.text_gray_light));
            xy.setDrawGridLines(true);
            xy.setDrawAxisLine(false);
        } else {
            labelY.setText("inch");
            txtUnit.setText("inch");
            final YAxis xy = chart.getAxisLeft();
            xy.setAxisMaximum(80.0f);
            xy.setGranularity(20f);
            xy.setAxisMinimum(0f);
            xy.setTextSize(10f);
            xy.setValueFormatter(new YAxisValueFormatter());
            xy.setGranularityEnabled(true);
            xy.setGridColor(getResources().getColor(R.color.text_gray_light));
            xy.setDrawGridLines(true);
            xy.setDrawAxisLine(false);
        }
    }


    private void setEntry(List<Entry> entries) {
        LineData lineData = chart.getLineData();
        LineDataSet dataSet = (LineDataSet) lineData.getDataSetByIndex(0);

        dataSet.getValues().clear();
        dataSet.getValues().addAll(entries);

        dataSet.notifyDataSetChanged();
        lineData.notifyDataChanged();
        chart.notifyDataSetChanged();
        chart.invalidate();
    }

    private void initChartView(List<Entry> entries) {
        LineDataSet lds = new LineDataSet(entries, "date");
        lds.setCircleColor(getResources().getColor(R.color.colorAccent));
        lds.setCircleRadius(3f);
        lds.setCircleHoleRadius(2f);
        lds.setColor(getResources().getColor(R.color.colorAccent));
        lds.setLineWidth(1f);
        LineData ld = new LineData(lds);
        ld.setDrawValues(false);
        chart.setData(ld);

        final float textSize = 10f;
        final XAxis xa = chart.getXAxis();
        xa.setGranularity(3f);
        xa.setGranularityEnabled(true);
        xa.setLabelCount(10, false);
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

        chart.setVisibleXRange(32f, 32f);
        chart.getAxisRight().setEnabled(false);
        Description desc = new Description();
        desc.setText("");
        chart.setDescription(desc);
        chart.getLegend().setEnabled(false);
        chart.invalidate();
        chart.moveViewToX(DateUtils.getIdNow() - 15);
    }

    @Override
    protected void initEvents() {
        super.initEvents();
        rootView.findViewById(R.id.btn_edit).setOnClickListener(view -> {
            WaistlineDialogFragment dialogFragment = new WaistlineDialogFragment(this);
            dialogFragment.show(getChildFragmentManager(), null);
        });
    }

    @Override
    protected void initObservers() {
        super.initObservers();
        addDisposable(DayHistoryRepository.getInstance().last30Days().subscribe(response -> {
            float summary = 0;
            if (response == null || response.size() == 0) {
                summary = 0;
            } else {
                summary = response.get(response.size() - 1).getWaistline() - response.get(0).getWaistline();
            }
            ((TextView) rootView.findViewById(R.id.txt_summary)).setText(String.format(Locale.US, "%.02f", summary));
        }));
        addDisposable(
                AppDatabase.getInstance().dayHistoryDao().getCurrentWaistline()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            Log.e("status", "waistline current: " + response);
                            int unitType = AppSettings.getInstance().getUnitType();
                            String unitString = unitType == 0 ? "CM" : "INCH";
                            float value = unitType == 0 ? response : Utils.convertCmToInch(response);
                            ((TextView) rootView.findViewById(R.id.txt_current)).setText(String.format(Locale.US, "%.2f %s", value, unitString));
                        })
        );
        addDisposable(
                AppDatabase.getInstance().dayHistoryDao().getMaxWaistline()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            Log.e("status", "waistline max: " + response);
                            int unitType = AppSettings.getInstance().getUnitType();
                            String unitString = unitType == 0 ? "CM" : "INCH";
                            float value = unitType == 0 ? response : Utils.convertCmToInch(response);
                            ((TextView) rootView.findViewById(R.id.txt_maximum)).setText(String.format(Locale.US, "%.2f %s", value, unitString));
                        })
        );
        addDisposable(
                AppDatabase.getInstance().dayHistoryDao().getMinWaistline()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            Log.e("status", "waistline min: " + response);
                            int unitType = AppSettings.getInstance().getUnitType();
                            String unitString = unitType == 0 ? "CM" : "INCH";
                            float value = unitType == 0 ? response : Utils.convertCmToInch(response);
                            ((TextView) rootView.findViewById(R.id.txt_minimum)).setText(String.format(Locale.US, "%.2f %s", value, unitString));
                        })
        );
    }

    @Override
    public void onResult(int type, Object value) {
        if (type == WAISTLINE) {
            refresh();
        }
    }
}
