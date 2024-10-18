package com.nhn.fitness.ui.interfaces;

public interface MapDataCallback {
    void onDataChanged(float speed, float distance, double calories);
    void onTimeChanged(long time);
}
