package com.example.myapplication.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.myapplication.model.User;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private DatabaseHelper dbHelper;

    public UserDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    /**
     * 插入用户（Create）
     */
    public long insertUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());
        values.put("name", user.getName());
        values.put("gender", user.getGender());
        values.put("phone", user.getPhone());
        values.put("email", user.getEmail());
        values.put("avatar_url", user.getAvatarUrl());
        values.put("department", user.getDepartment());
        values.put("major", user.getMajor());
        values.put("title", user.getTitle());
        values.put("role", user.getRole());
        long id = db.insert("users", null, values);
        db.close();
        return id;
    }

    /**
     * 查询所有用户（Retrieve）
     */
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users", null);
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setUserId(cursor.getInt(0));
                user.setUsername(cursor.getString(1));
                user.setPassword(cursor.getString(2));
                user.setName(cursor.getString(3));
                user.setGender(cursor.getString(4));
                user.setPhone(cursor.getString(5));
                user.setEmail(cursor.getString(6));
                user.setAvatarUrl(cursor.getString(7));
                user.setDepartment(cursor.getString(8));
                user.setMajor(cursor.getString(9));
                user.setTitle(cursor.getString(10));
                user.setRole(cursor.getString(11));
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userList;
    }

    /**
     * 通过 ID 查询单个用户
     */
    public User getUserById(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE user_id=?", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            User user = new User();
            user.setUserId(cursor.getInt(0));
            user.setUsername(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            user.setName(cursor.getString(3));
            user.setGender(cursor.getString(4));
            user.setPhone(cursor.getString(5));
            user.setEmail(cursor.getString(6));
            user.setAvatarUrl(cursor.getString(7));
            user.setDepartment(cursor.getString(8));
            user.setMajor(cursor.getString(9));
            user.setTitle(cursor.getString(10));
            user.setRole(cursor.getString(11));
            cursor.close();
            db.close();
            return user;
        }
        cursor.close();
        db.close();
        return null; // 如果用户不存在，返回 null
    }

    /**
     * 更新用户信息（Update）
     */
    public int updateUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());
        values.put("name", user.getName());
        values.put("gender", user.getGender());
        values.put("phone", user.getPhone());
        values.put("email", user.getEmail());
        values.put("avatar_url", user.getAvatarUrl());
        values.put("department", user.getDepartment());
        values.put("major", user.getMajor());
        values.put("title", user.getTitle());
        values.put("role", user.getRole());
        int rowsAffected = db.update("users", values, "user_id=?", new String[]{String.valueOf(user.getUserId())});
        db.close();
        return rowsAffected;
    }

    /**
     * 删除用户（Delete）
     */
    public void deleteUser(int userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("users", "user_id=?", new String[]{String.valueOf(userId)});
        db.close();
    }
}
