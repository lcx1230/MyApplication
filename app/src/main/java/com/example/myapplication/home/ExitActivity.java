package com.example.myapplication.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.auth.LoginActivity;

public class ExitActivity extends AppCompatActivity {
    private Button exitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit);
        exitButton=findViewById(R.id.exit);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 创建确认退出的弹窗
                new android.app.AlertDialog.Builder(ExitActivity.this)
                        .setMessage("确定要退出吗？") // 弹窗的内容
                        .setCancelable(false) // 设置点击弹窗外部不能关闭
                        .setPositiveButton("确定", (dialog, id) -> {
                            // 点击确定后跳转到 LoginActivity 并结束当前界面
                            Intent intent = new Intent(ExitActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish(); // 结束当前活动，不能返回
                        })
                        .setNegativeButton("取消", (dialog, id) -> {
                            // 点击取消后，关闭弹窗
                            dialog.dismiss();
                        })
                        .create()
                        .show();
            }
        });


    }
}