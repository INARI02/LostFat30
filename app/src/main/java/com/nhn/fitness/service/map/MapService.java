package com.nhn.fitness.service.map;

import static com.nhn.fitness.utils.Constants.MET_WALKING;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.nhn.fitness.data.dto.UserWorkoutInfoDTO;
import com.nhn.fitness.data.model.DayHistoryModel;
import com.nhn.fitness.data.model.LocationPoint;
import com.nhn.fitness.data.repositories.DayHistoryRepository;
import com.nhn.fitness.data.shared.AppSettings;
import com.nhn.fitness.data.shared.SessionManager;
import com.nhn.fitness.service.rest.RestApiHelper;
import com.nhn.fitness.ui.interfaces.Callback;
import com.nhn.fitness.ui.interfaces.MapDataCallback;
import com.nhn.fitness.utils.DataConverter;
import com.nhn.fitness.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MapService {
    private static MapService instance;
    private final List<LocationPoint> locationPoints;
    private LocationPoint lastLocationPoint;
    private final LocationProvider locationProvider;
    private final Callback locationChangedCallback;
    private Callback locationPointChangedCallback;
    private MapDataCallback mapDataCallback;
    private Handler timeHandler;

    private boolean isRunning;
    private long runningTime = 0; // seconds
    private float instantSpeed; // km/h
    private float totalDistance = 0f; // km
    private double totalCalories = 0; // cal
    private long lastLCTime = 0;
    private float maxSpeed = 0;

    private MapService(Context context) {
        locationPoints = new ArrayList<>();
        locationProvider = LocationProvider.getInstance(context);
        locationChangedCallback = location -> {
            Location lc = (Location) location;
            instantSpeed = Math.round(lc.getSpeed() * 3.6f * 100) / 100f;
            if (maxSpeed < instantSpeed) {
                maxSpeed = instantSpeed;
            }
            LocationPoint locationPoint = new LocationPoint(lc.getLatitude(), lc.getLongitude());
            calculateDistance(locationPoint);
            calculateCalories(lc.getTime());
            locationPoints.add(locationPoint);
            if (locationPointChangedCallback != null) {
                locationPointChangedCallback.execute(locationPoints);
            }
            if (mapDataCallback != null) {
                mapDataCallback.onDataChanged(instantSpeed, totalDistance, totalCalories);
            }
        };
        timeHandler = new Handler(Looper.getMainLooper());
    }

    public static MapService getInstance(Context context) {
        if (instance == null) {
            instance = new MapService(context);
        }
        return instance;
    }

    public void start() {
        isRunning = true;
        locationProvider.subscribe(locationChangedCallback);
        lastLCTime = System.currentTimeMillis();
        startTimer();
    }

    public void stop() {
        isRunning = false;
        locationProvider.unsubscribe();
        stopTimer();
        saveRunningSessionData();
        resetData();
    }

    public void setLocationChangedCallback(Callback locationPointChangedCallback) {
        this.locationPointChangedCallback = locationPointChangedCallback;
    }

    public void setMapDataCallback(MapDataCallback mapDataCallback) {
        this.mapDataCallback = mapDataCallback;
    }

    public boolean isRunning() {
        return isRunning;
    }

    private void startTimer() {
        timeHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runningTime++;
                if (mapDataCallback != null) {
                    mapDataCallback.onTimeChanged(runningTime);
                }
                timeHandler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private void stopTimer() {
        if (mapDataCallback != null) {
            mapDataCallback.onTimeChanged(runningTime);
        }
        timeHandler.removeCallbacksAndMessages(null);
    }

    private void calculateDistance(LocationPoint currentPoint) {
        if (lastLocationPoint == null) {
            lastLocationPoint = currentPoint;
            return;
        }
        totalDistance += DataConverter.getDistanceInMetre(lastLocationPoint, currentPoint) / 1000;
        totalDistance = Math.round(totalDistance * 100) / 100f;
        lastLocationPoint = currentPoint;
    }

    private void calculateCalories(long time) {
        float weight = AppSettings.getInstance().getWeightDefault();
        long diff = (time - lastLCTime); // giay
        long hour = (diff / 3600);
        double met = DataConverter.getMetBySpeed(instantSpeed);

        totalCalories += (met * weight * hour);
        totalCalories = Math.round(totalCalories * 100) / 100d;
        lastLCTime = time;
    }

    private void saveRunningSessionData() {
        double averageSpeed = (totalDistance / runningTime) * 3600; // km/h
        double calo = totalCalories / 1000; // kcalo
        boolean isUpdate = true;
        Calendar calendar = Calendar.getInstance();
        DayHistoryModel dayHistoryModel = DayHistoryRepository.getInstance().getByIdWithoutObserve(DateUtils.getIdDay(calendar));
        if (dayHistoryModel == null) {
            isUpdate = false;
            dayHistoryModel = new DayHistoryModel(calendar);
        }
        dayHistoryModel.addCalories((float) calo);
        if (isUpdate) {
            DayHistoryRepository.getInstance().update(dayHistoryModel).subscribe();
        } else {
            DayHistoryRepository.getInstance().insert(dayHistoryModel).subscribe();
        }

        UserWorkoutInfoDTO workoutInfoDTO = new UserWorkoutInfoDTO();
        workoutInfoDTO.setAverageSpeed((float) averageSpeed);
        workoutInfoDTO.setDistance(totalDistance);
        workoutInfoDTO.setMaxSpeed(maxSpeed);
        workoutInfoDTO.setTime(runningTime);
        workoutInfoDTO.setUserId(SessionManager.getInstance().getCurrentUser().getId());
        // Save workout info to server
        RestApiHelper.getInstance().saveUserWorkoutInfo(workoutInfoDTO, new retrofit2.Callback<UserWorkoutInfoDTO>() {
            @Override
            public void onResponse(@NonNull Call<UserWorkoutInfoDTO> call, @NonNull Response<UserWorkoutInfoDTO> response) {

            }

            @Override
            public void onFailure(@NonNull Call<UserWorkoutInfoDTO> call, @NonNull Throwable t) {
                Log.d("DEBUG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void resetData() {
        locationPoints.clear();
        lastLocationPoint = null;
        totalDistance = 0;
        totalCalories = 0;
        instantSpeed = 0;
        maxSpeed = 0;
        runningTime = 0;
    }
}
