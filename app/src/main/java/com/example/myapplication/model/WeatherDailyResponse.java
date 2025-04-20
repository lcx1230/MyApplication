package com.example.myapplication.model;

import java.util.List;

public class WeatherDailyResponse {
    public Result result;

    public static class Result {
        public Daily daily;
    }

    public static class Daily {
        public List<Temperature> temperature;
        public List<Skycon> skycon;
    }

    public static class Temperature {
        public String date;
        public double max;
        public double min;
    }

    public static class Skycon {
        public String date;
        public String value;
    }
}
