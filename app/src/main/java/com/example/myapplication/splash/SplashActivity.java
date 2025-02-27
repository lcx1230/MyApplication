package com.example.myapplication.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.auth.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash); // 设置布局文件为 activity_splash2

        // 使用 Handler 延迟 2 秒后跳转到 MainActivity
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class); // 创建 Intent，用于从 SplashActivity2 跳转到 MainActivity
                startActivity(intent); // 启动 MainActivity
                finish(); // 关闭当前的 SplashActivity2
            }
        }, 1500); // 2000 毫秒 = 2 秒的延迟
    }
}