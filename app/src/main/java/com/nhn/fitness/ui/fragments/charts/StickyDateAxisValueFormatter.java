package com.nhn.fitness.ui.fragments.charts;

import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.nhn.fitness.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StickyDateAxisValueFormatter extends ValueFormatter {

    private LineChart chart;
    private BarChart barChart;
    private TextView sticky;

    public StickyDateAxisValueFormatter(BarChart chart, TextView sticky) {
        this.barChart = chart;
        this.sticky = sticky;
    }

    public StickyDateAxisValueFormatter(LineChart chart, TextView sticky) {
        this.chart = chart;
        this.sticky = sticky;
    }

    @Override
    public String getFormattedValue(float value) {

        if (barChart == null) {
            if (value < chart.getLowestVisibleX()) {
                return "";
            }
        } else {
            if (value < barChart.getLowestVisibleX()) {
                return "";
            }
        }

        if (value > 15) {
            sticky.setText(formatDate(convertIdToDate((long) (value - 15))));
        }

        return DateUtils.convertDayOfMonth(convertIdToDate((long) value));
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        return super.getAxisLabel(value, axis);
    }

    @Override
    public String getBarLabel(BarEntry barEntry) {
        return super.getBarLabel(barEntry);
    }

    @Override
    public String getBarStackedLabel(float value, BarEntry stackedEntry) {
        return super.getBarStackedLabel(value, stackedEntry);
    }

    @Override
    public String getPointLabel(Entry entry) {
        return super.getPointLabel(entry);
    }

    @Override
    public String getPieLabel(float value, PieEntry pieEntry) {
        return super.getPieLabel(value, pieEntry);
    }

    @Override
    public String getRadarLabel(RadarEntry radarEntry) {
        return super.getRadarLabel(radarEntry);
    }

    @Override
    public String getBubbleLabel(BubbleEntry bubbleEntry) {
        return super.getBubbleLabel(bubbleEntry);
    }

    @Override
    public String getCandleLabel(CandleEntry candleEntry) {
        return super.getCandleLabel(candleEntry);
    }

    public static Calendar convertIdToDate(long id) {
        Calendar calendar = (Calendar) Calendar.getInstance().clone();
        calendar.setTimeInMillis(id * 86400000);
        return calendar;
    }

    public String formatDate(Calendar calendar) {
        return new SimpleDateFormat("MMM, yyyy").format(calendar.getTime());
    }

}
