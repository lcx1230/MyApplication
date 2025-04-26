package com.example.myapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {

    private static final String PREF_NAME = "user_preferences";
    private static final String KEY_LIKE_STATUS = "like_status_";

    private SharedPreferences sharedPreferences;

    public PreferenceHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // 存储点赞状态
    public void saveLikeStatus(String userId, boolean isLiked) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_LIKE_STATUS + userId, isLiked);
        editor.apply();
    }

    // 获取点赞状态
    public boolean getLikeStatus(String userId) {
        return sharedPreferences.getBoolean(KEY_LIKE_STATUS + userId, false);
    }
}
