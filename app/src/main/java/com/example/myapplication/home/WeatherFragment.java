package com.example.myapplication.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.myapplication.R;
import com.example.myapplication.model.WeatherDailyResponse;
import com.example.myapplication.model.WeatherResponse;
import com.example.myapplication.utils.RetrofitClient;
import com.example.myapplication.utils.WeatherApiService;
import com.example.myapplication.utils.WeatherUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherFragment extends Fragment {

    private TextView tvTemperature, tvWeatherDesc;
    // 顶部添加这几行：
    private TextView tvDay1Date, tvDay1WeatherDesc, tvDay1TempRange;
    private TextView tvDay2Date, tvDay2WeatherDesc, tvDay2TempRange;
    private TextView tvDay3Date, tvDay3WeatherDesc, tvDay3TempRange;
    private ImageView ivDay1Icon, ivDay2Icon, ivDay3Icon;


    public WeatherFragment() {
        // 默认的构造函数
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 加载布局文件
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        // 初始化UI组件
        tvTemperature = view.findViewById(R.id.tv_temperature);
        tvWeatherDesc = view.findViewById(R.id.tv_weather_desc);
        // onCreateView() 中加上这些 findViewById（接在已有的初始化后）：
        tvDay1Date = view.findViewById(R.id.tv_day1_date);
        tvDay1WeatherDesc = view.findViewById(R.id.tv_day1_weather_desc);
        tvDay1TempRange = view.findViewById(R.id.tv_day1_temp_range);
        ivDay1Icon = view.findViewById(R.id.iv_day1_icon);

        tvDay2Date = view.findViewById(R.id.tv_day2_date);
        tvDay2WeatherDesc = view.findViewById(R.id.tv_day2_weather_desc);
        tvDay2TempRange = view.findViewById(R.id.tv_day2_temp_range);
        ivDay2Icon = view.findViewById(R.id.iv_day2_icon);

        tvDay3Date = view.findViewById(R.id.tv_day3_date);
        tvDay3WeatherDesc = view.findViewById(R.id.tv_day3_weather_desc);
        tvDay3TempRange = view.findViewById(R.id.tv_day3_temp_range);
        ivDay3Icon = view.findViewById(R.id.iv_day3_icon);

        // 请求天气数据
        getRealtimeWeather();
        getDailyWeather();
        return view;
    }

    private void getRealtimeWeather() {
        WeatherApiService apiService = RetrofitClient.getClient().create(WeatherApiService.class);

        Call<WeatherResponse> call = apiService.getRealtimeWeather();
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    WeatherResponse weatherData = response.body();
                    if (weatherData != null && weatherData.getResult() != null) {
                        double temperature = weatherData.getResult().getRealtime().getTemperature();
                        String skycon = weatherData.getResult().getRealtime().getSkycon();
                        String translatedSkycon = WeatherUtils.translateSkyconToChinese(skycon);
                        // 更新UI
                        tvTemperature.setText(temperature + "℃");
                        tvWeatherDesc.setText(translatedSkycon);

                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e("WeatherFragment", "Error: " + t.getMessage());
            }
        });
    }
    private void getDailyWeather() {
        WeatherApiService apiService = RetrofitClient.getClient().create(WeatherApiService.class);
        Call<WeatherDailyResponse> call = apiService.getDailyWeather();

        call.enqueue(new Callback<WeatherDailyResponse>() {
            @Override
            public void onResponse(Call<WeatherDailyResponse> call, Response<WeatherDailyResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherDailyResponse data = response.body();
                    List<WeatherDailyResponse.Temperature> temps = data.result.daily.temperature;
                    List<WeatherDailyResponse.Skycon> skycons = data.result.daily.skycon;

                    if (temps.size() >= 3 && skycons.size() >= 3) {
                        updateDailyForecast(0, tvDay1Date, tvDay1WeatherDesc, tvDay1TempRange, ivDay1Icon, temps, skycons);
                        updateDailyForecast(1, tvDay2Date, tvDay2WeatherDesc, tvDay2TempRange, ivDay2Icon, temps, skycons);
                        updateDailyForecast(2, tvDay3Date, tvDay3WeatherDesc, tvDay3TempRange, ivDay3Icon, temps, skycons);
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherDailyResponse> call, Throwable t) {
                Log.e("WeatherFragment", "Daily fetch failed: " + t.getMessage());
            }
        });
    }
    private void updateDailyForecast(int index, TextView tvDate, TextView tvDesc, TextView tvTempRange, ImageView ivIcon,
                                     List<WeatherDailyResponse.Temperature> temps, List<WeatherDailyResponse.Skycon> skycons) {
        WeatherDailyResponse.Temperature temp = temps.get(index);
        WeatherDailyResponse.Skycon sky = skycons.get(index);

        String date = temp.date.split("T")[0]; // 得到 "2024-04-20"
        String[] parts = date.split("-");
        String formattedDate = Integer.parseInt(parts[1]) + "月" + Integer.parseInt(parts[2]) + "日";

        String weatherDesc = WeatherUtils.translateSkyconToChinese(sky.value);
        String tempRange = (int)temp.min + "°/" + (int)temp.max + "°";

        tvDate.setText(formattedDate);
        tvDesc.setText(weatherDesc);
        tvTempRange.setText(tempRange);

        int iconResId = WeatherUtils.getWeatherIcon(sky.value);
        ivIcon.setImageResource(iconResId);

        int backgroundResId = WeatherUtils.getWeatherBackground(sky.value);
        getView().setBackgroundResource(backgroundResId);  // 更改背景
    }


}
