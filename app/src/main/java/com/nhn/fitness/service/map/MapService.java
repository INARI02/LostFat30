package com.nhn.fitness.service.map;

import android.content.Context;
import android.location.Location;

import com.nhn.fitness.data.model.LocationPoint;
import com.nhn.fitness.ui.interfaces.Callback;

import java.util.ArrayList;
import java.util.List;

public class MapService {
    private static MapService instance;
    private final List<LocationPoint> locationPoints;
    private final LocationProvider locationProvider;
    private final Callback locationChangedCallback;
    private Callback locationPointChangedCallback;

    private MapService(Context context) {
        locationPoints = new ArrayList<>();
        locationProvider = LocationProvider.getInstance(context);
        locationChangedCallback = location -> {
            Location lc = (Location) location;
            LocationPoint locationPoint = new LocationPoint(lc.getLatitude(), lc.getLongitude());
            locationPoints.add(locationPoint);
            if (locationPointChangedCallback != null) {
                locationPointChangedCallback.execute(locationPoints);
            }
        };
    }

    public static MapService getInstance(Context context) {
        if (instance == null) {
            instance = new MapService(context);
        }
        return instance;
    }

    public void start() {
        locationProvider.subscribe(locationChangedCallback);
    }

    public void stop() {
        locationProvider.unsubscribe();
    }

    public void setLocationChangedCallback(Callback locationPointChangedCallback) {
        this.locationPointChangedCallback = locationPointChangedCallback;
    }
}
