package com.nhn.fitness.ui.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.nhn.fitness.R;
import com.nhn.fitness.data.model.LocationPoint;
import com.nhn.fitness.service.map.MapService;
import com.nhn.fitness.ui.fragments.InfoMapFragment;
import com.nhn.fitness.ui.fragments.MapManager;
import com.nhn.fitness.ui.interfaces.MapDataCallback;
import com.nhn.fitness.ui.interfaces.OnActionCallBack;

import java.util.List;

public class MapActivity extends AppCompatActivity implements OnActionCallBack {
    private static final int INFO_CONTAINER_ID = R.id.info_container;
    private GoogleMap mGoogleMap;
    private MapService mMapService;
    private BottomSheetBehavior<LinearLayout> mBottomSheetBehavior;
    private InfoMapFragment mInfoMapFragment;

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
        replaceFragment(mInfoMapFragment, INFO_CONTAINER_ID, "InfoMapFragment", false, -1);
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
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 101);
        }
        mBottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet_info));
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        mInfoMapFragment = new InfoMapFragment();
        mInfoMapFragment.setActivityCallback(ignore -> {
            if (mMapService.isRunning()) {
                mMapService.stop();
                mGoogleMap.clear();
            } else {
                mMapService.start();
            }
        });
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
        mMapService.setMapDataCallback(new MapDataCallback() {
            @Override
            public void onDataChanged(float speed, float distance, double calories) {
                mInfoMapFragment.updateMapData(speed, distance, calories);
            }

            @Override
            public void onTimeChanged(long time) {
                mInfoMapFragment.updateRunningTime(time);
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

    private void replaceFragment(
            Fragment fragment, int containerViewId,
            String TAG, boolean addToBackStack,
            int transit
    ) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerViewId, fragment, TAG);
        commitTransaction(transaction, TAG, addToBackStack, transit);
    }

    private void commitTransaction(
            FragmentTransaction transaction, String TAG,
            boolean addToBackStack, int transit
    ) {
        if (addToBackStack) transaction.addToBackStack(TAG);
        if (transit != -1) transaction.setTransition(transit);
        transaction.commitAllowingStateLoss();
    }
}