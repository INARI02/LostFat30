package com.nhn.fitness.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nhn.fitness.data.model.FeedBackResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
   // https://manager-apps.sunnytees.co/api/v1/report-feedback?email=dungvuteam@gmail.com&name=anh bau&key_app=losefat30&device_id=324234&content=3423423&type=OTHER


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://localhost:8081")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//    ApiService apiService = new Retrofit.Builder()
//            .baseUrl("https://manager-apps.sunnytees.co/")
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .build()
 //           .create(ApiService.class);
//    ApiService retrofit = new Retrofit.Builder()
//            .baseUrl("http://192.168.1.215:8081")
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .build().create(ApiService.class);

    @POST("/api/v1/report-feedback")
    Call<FeedBackResponse> reportFeedBack (@Query("email") String email, @Query("name") String name,
                                           @Query("key_app") String keyApp, @Query("device_id") String deviceId,
                                           @Query("content") String content, @Query("type") String type);

}
