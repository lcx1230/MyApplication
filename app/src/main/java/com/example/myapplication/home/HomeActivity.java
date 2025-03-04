package com.example.myapplication.home;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;

import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements HomeFragment.OnDataPass {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // 初始化底部导航栏
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // 默认加载首页 Fragment
        loadFragment(new HomeFragment());

        // 设置底部导航栏点击事件
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                loadFragment(new HomeFragment());
                return true;
            } else if (item.getItemId() == R.id.nav_consultation) {
                loadFragment(new CounselorFragment());
                return true;
            } else if (item.getItemId() == R.id.nav_reading) {
                loadFragment(new ReadingFragment());
                return true;
            } else if (item.getItemId() == R.id.nav_messages) {
                loadFragment(new MessagesFragment());
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                loadFragment(new ProfileFragment());
                return true;
            } else {
                return false;
            }
        });

        // 设置底部导航栏的选中项的颜色
        bottomNavigationView.setItemIconTintList(ContextCompat.getColorStateList(this, R.color.nav_item_icon_tint));
        bottomNavigationView.setItemTextColor(ContextCompat.getColorStateList(this, R.color.nav_item_icon_tint));
    }

    // 加载指定的 Fragment
    private void loadFragment(Fragment fragment) {
        // 创建 FragmentTransaction
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 使用替换方式切换 Fragment
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);  // 允许用户返回
        transaction.commit();
    }

    // 实现 OnDataPass 接口
    @Override
    public void onDataPass(String data) {
        Toast.makeText(this, "Received data: " + data, Toast.LENGTH_SHORT).show();
    }
}
