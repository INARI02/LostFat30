package com.nhn.fitness.service.rest;

import com.nhn.fitness.data.dto.LoginDTO;
import com.nhn.fitness.data.dto.UserDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestApiService {
    @POST("/user/register")
    Call<UserDTO> register(@Body UserDTO userDTO);

    @POST("/user/login")
    Call<UserDTO> login(@Body LoginDTO loginDTO);

    @POST("/user/edit/info")
    Call<UserDTO> edit(@Body UserDTO userDTO);

    @GET("/user/info/{id}")
    Call<UserDTO> getUserInfo(@Path("id") int id);
}
