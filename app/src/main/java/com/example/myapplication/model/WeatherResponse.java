package com.example.myapplication.model;

public class WeatherResponse {
    private Result result;

    public Result getResult() {
        return result;
    }

    public static class Result {
        private Realtime realtime;

        public Realtime getRealtime() {
            return realtime;
        }
    }

    public static class Realtime {
        private double temperature;
        private String skycon;

        public double getTemperature() {
            return temperature;
        }

        public String getSkycon() {
            return skycon;
        }
    }
}
