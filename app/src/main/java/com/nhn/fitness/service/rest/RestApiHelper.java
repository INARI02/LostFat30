package com.nhn.fitness.service.rest;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nhn.fitness.data.dto.DailySectionUserDTO;
import com.nhn.fitness.data.dto.DayHistoryDTO;
import com.nhn.fitness.data.dto.LoginDTO;
import com.nhn.fitness.data.dto.SectionHistoryDTO;
import com.nhn.fitness.data.dto.StepDTO;
import com.nhn.fitness.data.dto.UserDTO;
import com.nhn.fitness.data.dto.UserWorkoutInfoDTO;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApiHelper {
    private static final String BASE_URL = "http://192.168.137.1:8080";
    private static RestApiHelper sRestApiHelper;
    private Retrofit retrofit;
    private final RestApiService restApiService;

    private RestApiHelper() {
        Gson gson = new GsonBuilder()
                .setDateFormat("dd/MM/yyyy") // Định dạng ngày tháng
                .create();
        retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson)).baseUrl(BASE_URL).build();
        restApiService = retrofit.create(RestApiService.class);
    }

    public static RestApiHelper getInstance() {
        if (sRestApiHelper == null) {
            sRestApiHelper = new RestApiHelper();
        }
        return sRestApiHelper;
    }

    // *** REST API methods ***
    public void login(String username, String password, Callback<UserDTO> callback) {
        LoginDTO loginDTO = new LoginDTO(username, password);
        restApiService.login(loginDTO).enqueue(callback);
    }

    public void register(UserDTO userDTO, Callback<UserDTO> callback) {
        restApiService.register(userDTO).enqueue(callback);
    }

    public void editUserInfo(UserDTO userDTO, Callback<UserDTO> callback) {
        restApiService.edit(userDTO).enqueue(callback);
    }

    public void getUserInfo(int id, Callback<UserDTO> callback) {
        restApiService.getUserInfo(id).enqueue(callback);
    }

    public void saveDayHistory(DayHistoryDTO dayHistoryDTO, Callback<DayHistoryDTO> callback) {
        restApiService.saveDayHistory(dayHistoryDTO).enqueue(callback);
    }

    public void getAllDayHistory(int userId, Callback<List<DayHistoryDTO>> callback) {
        restApiService.getAllDayHistory(userId).enqueue(callback);
    }

    public void saveDailySectionUser(DailySectionUserDTO dailySectionUserDTO, Callback<DailySectionUserDTO> callback) {
        restApiService.saveDailySectionUser(dailySectionUserDTO).enqueue(callback);
    }

    public void getDailySectionUser(int userId, Callback<List<DailySectionUserDTO>> callback) {
        restApiService.getDailySectionUser(userId).enqueue(callback);
    }

    public void saveSectionHistory(SectionHistoryDTO sectionHistoryDTO, Callback<SectionHistoryDTO> callback) {
        restApiService.saveSectionHistory(sectionHistoryDTO).enqueue(callback);
    }

    public void getSectionHistories(int userId, Callback<List<SectionHistoryDTO>> callback) {
        restApiService.getSectionHistories(userId).enqueue(callback);
    }

    public void saveUserWorkoutInfo(UserWorkoutInfoDTO userWorkoutInfoDTO, Callback<UserWorkoutInfoDTO> callback) {
        restApiService.saveUserWorkoutInfo(userWorkoutInfoDTO).enqueue(callback);
    }

    public void deleteAllData(int id, Callback<Void> callback) {
        restApiService.deleteAllData(id).enqueue(callback);
    }

    public void saveStep(StepDTO stepDTO, Callback<StepDTO> callback) {
        restApiService.saveStep(stepDTO).enqueue(callback);
    }

    public void getStep(int userId, Callback<List<StepDTO>> callback) {
        restApiService.getSteps(userId).enqueue(callback);
    }
}
