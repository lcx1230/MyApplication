package com.example.myapplication.utils;

import com.example.myapplication.model.WeatherDailyResponse;
import com.example.myapplication.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface WeatherApiService {
    @GET("v2.5/bPxH0puhEOyEg5ud/119.470186,32.198285/realtime")
    Call<WeatherResponse> getRealtimeWeather();

    @GET("v2.6/bPxH0puhEOyEg5ud/119.470186,32.198285/daily?dailysteps=3")
    Call<WeatherDailyResponse> getDailyWeather();

}


