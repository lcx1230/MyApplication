package com.example.myapplication.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.model.Counselor;

import java.util.ArrayList;
import java.util.List;

public class CounselorDAO {
    private DatabaseHelper dbHelper;

    public CounselorDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // 插入心理咨询师（Create）
    public long insertCounselor(Counselor counselor) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", counselor.getName());
        values.put("gender", counselor.getGender());
        values.put("qualifications", counselor.getQualifications());
        values.put("specialization", counselor.getSpecialization());
        values.put("avatar_url", counselor.getAvatarUrl());
        values.put("available_time", counselor.getAvailableTime()); // JSON 字符串
        long id = db.insert("counselors", null, values);
        db.close();
        return id;
    }

    // 查询所有心理咨询师（Retrieve）
    public List<Counselor> getAllCounselors() {
        List<Counselor> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM counselors", null);
        if (cursor.moveToFirst()) {
            do {
                Counselor counselor = new Counselor();
                counselor.setCounselorId(cursor.getInt(0));
                counselor.setName(cursor.getString(1));
                counselor.setGender(cursor.getString(2));
                counselor.setQualifications(cursor.getString(3));
                counselor.setSpecialization(cursor.getString(4));
                counselor.setAvatarUrl(cursor.getString(5));
                counselor.setAvailableTime(cursor.getString(6));
                list.add(counselor);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    // 根据ID查询心理咨询师
    public Counselor getCounselorById(int counselorId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM counselors WHERE counselor_id=?", new String[]{String.valueOf(counselorId)});
        Counselor counselor = null;
        if (cursor.moveToFirst()) {
            counselor = new Counselor();
            counselor.setCounselorId(cursor.getInt(0));
            counselor.setName(cursor.getString(1));
            counselor.setGender(cursor.getString(2));
            counselor.setQualifications(cursor.getString(3));
            counselor.setSpecialization(cursor.getString(4));
            counselor.setAvatarUrl(cursor.getString(5));
            counselor.setAvailableTime(cursor.getString(6));
        }
        cursor.close();
        db.close();
        return counselor;
    }

    // 更新心理咨询师（Update）
    public int updateCounselor(Counselor counselor) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", counselor.getName());
        values.put("gender", counselor.getGender());
        values.put("qualifications", counselor.getQualifications());
        values.put("specialization", counselor.getSpecialization());
        values.put("avatar_url", counselor.getAvatarUrl());
        values.put("available_time", counselor.getAvailableTime());
        int rowsAffected = db.update("counselors", values, "counselor_id=?", new String[]{String.valueOf(counselor.getCounselorId())});
        db.close();
        return rowsAffected;
    }

    // 删除心理咨询师（Delete）
    public void deleteCounselor(int counselorId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("counselors", "counselor_id=?", new String[]{String.valueOf(counselorId)});
        db.close();
    }
}
