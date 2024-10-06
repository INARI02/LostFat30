package com.nhn.fitness.service.rest;

import android.util.Log;

import com.nhn.fitness.data.dto.LoginDTO;
import com.nhn.fitness.data.dto.UserDTO;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApiHelper {
    private static final String BASE_URL = "http://192.168.137.1:8080";
    private static RestApiHelper sRestApiHelper;
    private Retrofit retrofit;
    private final RestApiService restApiService;

    private RestApiHelper() {
        retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).build();
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
}
