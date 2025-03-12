package com.example.myapplication.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.example.myapplication.dao.UserDAO;
import com.example.myapplication.home.HomeActivity;
import com.example.myapplication.R;
import com.example.myapplication.auth.RegisterActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;
    private UserDAO userDAO; // 添加 UserDAO 实例

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 设置闪屏页
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 初始化控件
        usernameEditText = findViewById(R.id.username_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginButton = findViewById(R.id.login_button);
        registerButton = findViewById(R.id.register_button);

        userDAO = new UserDAO(this); // 初始化 UserDAO

        // 设置登录按钮的点击监听器
        loginButton.setOnClickListener(v -> {
            login();
        });

        // 为注册按钮设置点击监听器
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
    //登录逻辑
    private void login() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // 检查用户名和密码是否为空
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // 硬编码的登录验证
        if (username.equals("admin") && password.equals("123456")) {
            // 登录成功
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            navigateToHome();
        } else if (userDAO.checkUserCredentials(username, password)) {
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            navigateToHome();
        }
       else {
            // 登录失败
            Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToHome() {
        Log.d("LoginActivity2", "Navigated to HomeActivity");
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish(); // 关闭当前 Activity

    }
}