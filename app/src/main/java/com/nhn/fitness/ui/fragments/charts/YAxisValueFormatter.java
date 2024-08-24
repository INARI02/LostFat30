package com.nhn.fitness.ui.fragments.charts;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.Locale;

public class YAxisValueFormatter extends ValueFormatter {
    @Override
    public String getFormattedValue(float value) {
//        if (value < 10) {
//            return "";
//        }

        return String.format(Locale.US, "%.0f", value);
    }
}
