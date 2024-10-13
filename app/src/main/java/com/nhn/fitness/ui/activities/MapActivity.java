package com.nhn.fitness.ui.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nhn.fitness.R;
import com.nhn.fitness.data.model.LocationPoint;
import com.nhn.fitness.service.map.MapService;
import com.nhn.fitness.ui.fragments.MapManager;
import com.nhn.fitness.ui.interfaces.OnActionCallBack;

import java.util.List;

public class MapActivity extends AppCompatActivity implements OnActionCallBack {
    private GoogleMap mGoogleMap;
    private MapService mMapService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        mMapService = MapService.getInstance(this);
        //MapFragment
        initViews();
        initEvents();
    }

    @Override
    public void showAlert(String distance, LatLng start, LatLng end) {
        AlertDialog alert=new AlertDialog.Builder(this).create();
        alert.setTitle("Thông báo");
        alert.setMessage("Đến đó khoảng: "+distance);
        alert.setButton(DialogInterface.BUTTON_POSITIVE,
                "Chỉ đường", (dialog, which) -> showDirection(start,end));
        alert.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapService.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapService.stop();
    }

    private void initViews() {
        MapFragment mapFragment= (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mGoogleMap = googleMap;
                MapManager.getInstance().setMap(googleMap);
                MapManager.getInstance().initMap();
                MapManager.getInstance().setCallBack(MapActivity.this);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                },101);
            }
        }
    }

    private void initEvents() {
        mMapService.setLocationChangedCallback(locationPoints -> {
            List<LocationPoint> points = (List<LocationPoint>) locationPoints;
            if (!points.isEmpty()) {
                PolylineOptions polylineOptions = new PolylineOptions();
                for (LocationPoint point : points) {
                    LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
                    polylineOptions.add(latLng);
                }
                polylineOptions.color(Color.BLUE);
                polylineOptions.width(12f);
                // ve len map
                mGoogleMap.addPolyline(polylineOptions);
            }
        });
    }

    private void showDirection(LatLng start, LatLng end) {
        String text=String.format("http://maps.google.com/maps?saddr=%s,%s&daddr=%s,%s",start.latitude,start.longitude,end.latitude,end.longitude);
        Intent intent=new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(text));
        startActivity(intent);
    }

    private void plotRoute(List<PolylineOptions> polylineOptions) {
        for (PolylineOptions polylineOption : polylineOptions) {
            mGoogleMap.addPolyline(polylineOption);
        }
    }
}