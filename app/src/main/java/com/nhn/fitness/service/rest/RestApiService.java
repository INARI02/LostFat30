package com.nhn.fitness.service.rest;

import com.nhn.fitness.data.dto.DailySectionUserDTO;
import com.nhn.fitness.data.dto.DayHistoryDTO;
import com.nhn.fitness.data.dto.LoginDTO;
import com.nhn.fitness.data.dto.SectionHistoryDTO;
import com.nhn.fitness.data.dto.StepDTO;
import com.nhn.fitness.data.dto.UserDTO;
import com.nhn.fitness.data.dto.UserWorkoutInfoDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

    @POST("/api/day_history")
    Call<DayHistoryDTO> saveDayHistory(@Body DayHistoryDTO dayHistoryDTO);

    @GET("/api/day_history/{userId}")
    Call<List<DayHistoryDTO>> getAllDayHistory(@Path("userId") int userId);

    @POST("/api/daily_section_user")
    Call<DailySectionUserDTO> saveDailySectionUser(@Body DailySectionUserDTO dailySectionUserDTO);

    @GET("/api/daily_section_user/{userId}")
    Call<List<DailySectionUserDTO>> getDailySectionUser(@Path("userId") int userId);

    @POST("/api/section_history")
    Call<SectionHistoryDTO> saveSectionHistory(@Body SectionHistoryDTO sectionHistoryDTO);

    @GET("/api/section_history/{userId}")
    Call<List<SectionHistoryDTO>> getSectionHistories(@Path("userId") int userId);

    @POST("/api/user_info_workout")
    Call<UserWorkoutInfoDTO> saveUserWorkoutInfo(@Body UserWorkoutInfoDTO userWorkoutInfoDTO);

    @DELETE("/user/data_all/{id}")
    Call<Void> deleteAllData(@Path("id") int id);

    @POST("/api/step")
    Call<StepDTO> saveStep(@Body StepDTO stepDTO);

    @GET("/api/step/{userId}")
    Call<List<StepDTO>> getSteps(@Path("userId") int userId);
}
