package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 设置闪屏页
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 找到注册按钮 (通过在 activity_main.xml 中设置的 id)
        Button registerButton = findViewById(R.id.register_button);

        // 为注册按钮设置点击监听器
        registerButton.setOnClickListener(v -> {
            // 创建一个 Intent (意图)，用于启动 RegisterActivity
            // Intent 就像一个信使，告诉系统我们要从哪里去哪里
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            // "MainActivity.this" 表示当前上下文 (MainActivity)
            // "RegisterActivity.class" 表示我们要启动的活动类

            // 启动 RegisterActivity
            startActivity(intent);
            // startActivity() 方法会执行跳转到 RegisterActivity 的操作
        });
    }
}