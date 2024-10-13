package com.nhn.fitness.service.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.nhn.fitness.ui.interfaces.Callback;

import java.lang.ref.WeakReference;

public class LocationProvider extends GnssStatus.Callback implements LocationListener {
    private static LocationProvider instance;
    private LocationManager locationManager;
    private WeakReference<Context> context;
    private Callback locationChangedCallback;

    private LocationProvider(Context context) {
        this.context = new WeakReference<>(context);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public static LocationProvider getInstance(Context context) {
        if (instance == null) {
            instance = new LocationProvider(context);
        }
        return instance;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (locationChangedCallback != null) {
            locationChangedCallback.execute(location);
        }
    }

    public void subscribe(Callback locationChangedCallback) {
        this.locationChangedCallback = locationChangedCallback;
        if (ActivityCompat.checkSelfPermission(context.get(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context.get(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0f, this);
        locationManager.registerGnssStatusCallback(this, new Handler(Looper.getMainLooper()));
    }

    public void unsubscribe() {
        this.locationChangedCallback = null;
        locationManager.removeUpdates(this);
    }
}
