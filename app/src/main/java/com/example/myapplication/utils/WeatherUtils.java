package com.example.myapplication.utils;

import com.example.myapplication.R;

public class WeatherUtils {
    public static String translateSkyconToChinese(String skycon) {
        if ("CLEAR_DAY".equals(skycon)) {
            return "晴";
        } else if ("CLEAR_NIGHT".equals(skycon)) {
            return "晴";
        } else if ("PARTLY_CLOUDY_DAY".equals(skycon)) {
            return "多云";
        } else if ("PARTLY_CLOUDY_NIGHT".equals(skycon)) {
            return "多云";
        } else if ("CLOUDY".equals(skycon)) {
            return "阴";
        } else if ("LIGHT_HAZE".equals(skycon)) {
            return "轻度雾霾";
        } else if ("MODERATE_HAZE".equals(skycon)) {
            return "中度雾霾";
        } else if ("HEAVY_HAZE".equals(skycon)) {
            return "重度雾霾";
        } else if ("LIGHT_RAIN".equals(skycon)) {
            return "小雨";
        } else if ("MODERATE_RAIN".equals(skycon)) {
            return "中雨";
        } else if ("HEAVY_RAIN".equals(skycon)) {
            return "大雨";
        } else if ("STORM_RAIN".equals(skycon)) {
            return "暴雨";
        } else if ("FOG".equals(skycon)) {
            return "雾";
        } else if ("LIGHT_SNOW".equals(skycon)) {
            return "小雪";
        } else if ("MODERATE_SNOW".equals(skycon)) {
            return "中雪";
        } else if ("HEAVY_SNOW".equals(skycon)) {
            return "大雪";
        } else if ("STORM_SNOW".equals(skycon)) {
            return "暴雪";
        } else if ("DUST".equals(skycon)) {
            return "浮尘";
        } else if ("SAND".equals(skycon)) {
            return "沙尘";
        } else if ("WIND".equals(skycon)) {
            return "大风";
        } else {
            return "未知天气";
        }
    }

    public static int getWeatherIcon(String skyconValue) {
        if ("CLEAR_DAY".equals(skyconValue)) {
            return R.drawable.sunny;
        } else if ("CLEAR_NIGHT".equals(skyconValue)) {
            return R.drawable.moon;
        } else if ("PARTLY_CLOUDY_DAY".equals(skyconValue) || "PARTLY_CLOUDY_NIGHT".equals(skyconValue)) {
            return R.drawable.cloudy;
        } else if ("CLOUDY".equals(skyconValue)) {
            return R.drawable.cloudy;
        } else if ("LIGHT_RAIN".equals(skyconValue)||"MODERATE_RAIN".equals(skyconValue)||"HEAVY_RAIN".equals(skyconValue)||"STORM_RAIN".equals(skyconValue)) {
            return R.drawable.rainy;
        } else if ("LIGHT_SNOW".equals(skyconValue)) {
            return R.drawable.snowy;
        } else if ("WIND".equals(skyconValue)) {
            return R.drawable.windy;
        } else {
            return R.drawable.sunny;
        }
    }
    public static int getWeatherBackground(String skycon) {
        switch (skycon) {
            case "CLEAR_DAY":
                return R.drawable.bg;
            case "CLEAR_NIGHT":
                return R.drawable.bg;
            case "PARTLY_CLOUDY_DAY":
            case "PARTLY_CLOUDY_NIGHT":
                return R.drawable.bg;
            case "RAIN":
                return R.drawable.bg;
            case "SNOW":
                return R.drawable.bg;
            case "WIND":
                return R.drawable.bg;
            default:
                return R.drawable.bg;
        }
    }


}
