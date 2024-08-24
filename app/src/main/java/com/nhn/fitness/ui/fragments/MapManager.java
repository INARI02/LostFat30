package com.nhn.fitness.ui.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nhn.fitness.R;
import com.nhn.fitness.data.model.PlaceGymEntity;
import com.nhn.fitness.ui.base.BaseApplication;
import com.nhn.fitness.ui.interfaces.OnActionCallBack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapManager implements GoogleMap.OnInfoWindowClickListener {
    private static final long TIME_DURATION = 2000;
    private static MapManager instance;
    private GoogleMap mMap;
    private Marker myPos;
    private OnActionCallBack mCallBack;


    private List<PlaceGymEntity> listPlace;

    private MapManager() {

    }

    public static MapManager getInstance() {
        if (instance == null) {
            instance = new MapManager();
        }
        return instance;
    }

    public void setCallBack(OnActionCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    public void setMap(GoogleMap mMap) {
        this.mMap = mMap;
    }

    public void initMap() {
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setInfoWindowAdapter(initWindow());
        mMap.setOnInfoWindowClickListener(this);
        if (ActivityCompat.checkSelfPermission(BaseApplication.getInstance(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(BaseApplication.getInstance(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
        findMyLocation();
        // for dummy data
        initPlaces();
        addPlaceToMap();
    }

    private GoogleMap.InfoWindowAdapter initWindow() {
        return new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return initViewAdapter(marker);
            }

            @Override
            public View getInfoContents(Marker marker) {
                return initViewAdapter(marker);
            }
        };
    }

    private View initViewAdapter(Marker marker) {
        if (marker.getTag() == null) return null;
        PlaceGymEntity place = (PlaceGymEntity) marker.getTag();
        View view = LayoutInflater.from(BaseApplication.getInstance()).inflate(R.layout.item_view_info, null);
        ImageView ivPlace = view.findViewById(R.id.iv_place);
        TextView tvName = view.findViewById(R.id.tv_name);
        TextView tvAddress = view.findViewById(R.id.tv_address);
        TextView tvContent = view.findViewById(R.id.tv_content);

        ivPlace.setImageResource(place.getPhotoBG());
        tvName.setText(place.getName());
        tvAddress.setText(place.getAddress());
        tvContent.setText(place.getContent());
        return view;
    }

    private void initPlaces() {
        listPlace = new ArrayList<>();
//        listPlace.add(new PlaceGymEntity(new LatLng(21.02893496469526, 105.85217157797551),
//                BaseApplication.getInstance().getString(R.string.txt_ho_guom),
//                BaseApplication.getInstance().getString(R.string.txt_ho_guom_address),
//                BaseApplication.getInstance().getString(R.string.txt_ho_guom_content), R.drawable.bg_ho_guom));
        listPlace.add(new PlaceGymEntity(new LatLng(20.988880625704482, 105.80285176795267),
                BaseApplication.getInstance().getString(R.string.txt_master_gym_club),
                BaseApplication.getInstance().getString(R.string.txt_master_gym_club_address),
                BaseApplication.getInstance().getString(R.string.txt_master_gym_club_content), R.drawable.ic_master_gym));
        listPlace.add(new PlaceGymEntity(new LatLng(20.98364443743391, 105.7916077505568),
                BaseApplication.getInstance().getString(R.string.txt_gym_california_fitness_yoga_machinco),
                BaseApplication.getInstance().getString(R.string.txt_gym_california_fitness_yoga_machinco_address),
                BaseApplication.getInstance().getString(R.string.txt_california_fitness_yoga_machinco_content), R.drawable.ic_cali_place));
//
//        listPlace.add(new PlaceGymEntity(new LatLng(21.0359933794539, 105.83360133934082),
//                BaseApplication.getInstance().getString(R.string.txt_chua_mot_cot),
//                BaseApplication.getInstance().getString(R.string.txt_chua_mot_cot_address),
//                BaseApplication.getInstance().getString(R.string.txt_chua_mot_cot_content), R.drawable.bg_chua_mot_cot));
//        listPlace.add(new PlaceGymEntity(new LatLng(21.048242936969473, 105.83696558351768),
//                BaseApplication.getInstance().getString(R.string.txt_chua_tran_quoc),
//                BaseApplication.getInstance().getString(R.string.txt_chua_tran_quoc_address),
//                BaseApplication.getInstance().getString(R.string.txt_chua_tran_quoc_content), R.drawable.bg_chua_tran_quoc));
//        listPlace.add(new PlaceGymEntity(new LatLng(21.024351390412573, 105.84113885468186),
//                BaseApplication.getInstance().getString(R.string.txt_ga_ha_noi),
//                BaseApplication.getInstance().getString(R.string.txt_ga_ha_noi_address),
//                BaseApplication.getInstance().getString(R.string.txt_ga_ha_noi_content), R.drawable.bg_ga_ha_noi));
//        listPlace.add(new PlaceGymEntity(new LatLng(21.054987322871263, 105.82579582270853),
//                BaseApplication.getInstance().getString(R.string.txt_ho_tay),
//                BaseApplication.getInstance().getString(R.string.txt_ho_tay_address),
//                BaseApplication.getInstance().getString(R.string.txt_ho_tay_content), R.drawable.bg_ho_tay));
//        listPlace.add(new PlaceGymEntity(new LatLng(21.034511166926972, 105.84009273934082),
//                BaseApplication.getInstance().getString(R.string.txt_hoang_thanh_thang_long),
//                BaseApplication.getInstance().getString(R.string.txt_hoang_thanh_thang_long_address),
//                BaseApplication.getInstance().getString(R.string.txt_hoang_thanh_thang_long_content), R.drawable.bg_hoang_thanh_thang_long));
//        listPlace.add(new PlaceGymEntity(new LatLng(21.024289963527174, 105.85793586817599),
//                BaseApplication.getInstance().getString(R.string.txt_nha_hat_lon_ha_noi),
//                BaseApplication.getInstance().getString(R.string.txt_nha_hat_lon_ha_noi_address),
//                BaseApplication.getInstance().getString(R.string.txt_nha_hat_lon_ha_noi_content), R.drawable.bg_nha_hat_lon_ha_noi));
//        listPlace.add(new PlaceGymEntity(new LatLng(21.028886000573365, 105.8488230258467),
//                BaseApplication.getInstance().getString(R.string.txt_nha_tho_lon_ha_noi),
//                BaseApplication.getInstance().getString(R.string.txt_nha_tho_lon_ha_noi_address),
//                BaseApplication.getInstance().getString(R.string.txt_nha_tho_lon_ha_noi_content), R.drawable.bg_nha_tho_lon_ha_noi));
//        listPlace.add(new PlaceGymEntity(new LatLng(21.0253751894491, 105.84636803934072),
//                BaseApplication.getInstance().getString(R.string.txt_nha_tu_hoa_lo),
//                BaseApplication.getInstance().getString(R.string.txt_nha_tu_hoa_lo_address),
//                BaseApplication.getInstance().getString(R.string.txt_nha_tu_hoa_lo_content), R.drawable.bg_nha_tu_hoa_lo));
//        listPlace.add(new PlaceGymEntity(new LatLng(21.031779597527844, 105.85334655468209),
//                BaseApplication.getInstance().getString(R.string.txt_pho_co_ha_noi),
//                BaseApplication.getInstance().getString(R.string.txt_pho_co_ha_noi_address),
//                BaseApplication.getInstance().getString(R.string.txt_pho_co_ha_noi_content), R.drawable.bg_pho_co_ha_noi));

    }

    private void updateMyLocation(LocationResult locationResult) {
        double lat = locationResult.getLastLocation().getLatitude();
        double lgn = locationResult.getLastLocation().getLongitude();
        if (myPos == null) {
            MarkerOptions marker = new MarkerOptions();
            marker.title("I'm here");
            marker.icon(BitmapDescriptorFactory.defaultMarker());
            marker.position(new LatLng(lat, lgn));
            String address = getAddress(lat, lgn);

            marker.snippet(address);

            myPos = mMap.addMarker(marker);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPos.getPosition(), 16));
        }
        if (myPos.getPosition().latitude != lat || myPos.getPosition().longitude != lgn) {
            String address = getAddress(lat, lgn);
            myPos.setSnippet(address);
            myPos.setPosition(new LatLng(lat, lgn));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPos.getPosition(), 16));
        }
        //   mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPos.getPosition(),16));
        // Toast.makeText(App.getInstance(),"My pos"+lat+" - "+lgn,Toast.LENGTH_SHORT).show();
    }

    private String getAddress(double lat, double lgn) {
        try {
            Geocoder geocoder = new Geocoder(BaseApplication.getInstance(), Locale.getDefault());
            List<Address> rs = geocoder.getFromLocation(lat, lgn, 1);
            if (rs != null && !rs.isEmpty()) {
                return rs.get(0).getAddressLine(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Không xác định";
    }

    @SuppressLint("VisibleForTests")
    private void findMyLocation() {
        FusedLocationProviderClient client
                = new FusedLocationProviderClient(BaseApplication.getInstance());

        if (ActivityCompat.checkSelfPermission(BaseApplication.getInstance(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(BaseApplication.getInstance(),
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationRequest req = new LocationRequest();
        req.setInterval(TIME_DURATION);
        req.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        client.requestLocationUpdates(req,
                new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        updateMyLocation(locationResult);
                    }
                }, Looper.getMainLooper());
    }

    private void addPlaceToMap() {
        BitmapDescriptor iconPlace = BitmapDescriptorFactory.fromResource(R.drawable.ic_place_gym);
        for (PlaceGymEntity place : listPlace) {
            MarkerOptions op = new MarkerOptions();
            op.title(place.getName());
            op.snippet(place.getAddress());
            op.icon(iconPlace);
            op.position(place.getLocation());
            Marker marker = mMap.addMarker(op);
            marker.setTag(place);
        }

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        PlaceGymEntity place = (PlaceGymEntity) marker.getTag();
        LatLng end = place.getLocation();
        LatLng start = myPos.getPosition();
        String distance = calcDistance(start, end);
        mCallBack.showAlert(distance, start, end);
    }

    private String calcDistance(LatLng start, LatLng end) {
        double lat1 = start.latitude;
        double lat2 = end.latitude;
        double lgn1 = start.longitude;
        double lgn2 = end.longitude;
        double R = 6371; // Radius of the earth in km
        double dLat = deg2rad(lat2 - lat1);  // deg2rad below
        double dLon = deg2rad(lgn2 - lgn1);
        double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                        Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                                Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c; // Distance in km
        return new DecimalFormat("#.#").format(d) + " km";
    }

    double deg2rad(double deg) {
        return deg * (Math.PI / 180);

    }
}