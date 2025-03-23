package com.example.myapplication.auth;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private Fragment adminManageFragment, adminProfileFragment;
    private Fragment currentFragment; // 记录当前 Fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        bottomNavigationView = findViewById(R.id.bottom_navigation_admin);
        fragmentManager = getSupportFragmentManager();

        // 初始化 Fragment
        adminManageFragment = new AdminManageFragment();
        adminProfileFragment = new AdminProfileFragment();

        // 添加 Fragment（默认显示 "管理"）
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_container, adminManageFragment, "manage");
        transaction.add(R.id.fragment_container, adminProfileFragment, "profile").hide(adminProfileFragment);
        transaction.commit();

        currentFragment = adminManageFragment; // 默认显示管理界面

        // 监听底部导航栏切换
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_manage) {
                switchFragment(adminManageFragment);
                return true;
            } else if (itemId == R.id.nav_profile_admin) {
                switchFragment(adminProfileFragment);
                return true;
            }

            return false;
        });

    }

    private void switchFragment(@NonNull Fragment targetFragment) {
        if (currentFragment != targetFragment) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.hide(currentFragment).show(targetFragment).commit();
            currentFragment = targetFragment;
        }
    }
}
