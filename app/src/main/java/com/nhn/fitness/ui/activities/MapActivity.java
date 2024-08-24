package com.nhn.fitness.ui.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.nhn.fitness.R;
import com.nhn.fitness.ui.fragments.MapManager;
import com.nhn.fitness.ui.interfaces.OnActionCallBack;

public class MapActivity extends AppCompatActivity implements OnActionCallBack {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        //MapFragment
        initViews();
    }

    private void initViews() {
        MapFragment mapFragment= (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
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

    @Override
    public void showAlert(String distance, LatLng start, LatLng end) {
        AlertDialog alert=new AlertDialog.Builder(this).create();
        alert.setTitle("Thông báo");
        alert.setMessage("Đến đó khoảng: "+distance);
        alert.setButton(DialogInterface.BUTTON_POSITIVE,
                "Chỉ đường", (dialog, which) -> showDirection(start,end));
        alert.show();
    }

    private void showDirection(LatLng start, LatLng end) {
        String text=String.format("http://maps.google.com/maps?saddr=%s,%s&daddr=%s,%s",start.latitude,start.longitude,end.latitude,end.longitude);
        Intent intent=new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(text));
        startActivity(intent);
    }
}