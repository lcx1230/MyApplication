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

    // ✅ 1. 旧方法：插入心理咨询师（不包含资格证书）
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

    // ✅ 2. 新方法：插入心理咨询师（包含资格证书）
    public long insertCounselorWithCertificate(Counselor counselor) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", counselor.getName());
        values.put("gender", counselor.getGender());
        values.put("qualifications", counselor.getQualifications());
        values.put("specialization", counselor.getSpecialization());
        values.put("avatar_url", counselor.getAvatarUrl());
        values.put("certificate_url", counselor.getCertificateUrl()); // 资格证书路径
        values.put("available_time", counselor.getAvailableTime()); // JSON 字符串
        long id = db.insert("counselors", null, values);
        db.close();
        return id;
    }

    // ✅ 3. 查询所有心理咨询师（不包含资格证书）
    public List<Counselor> getAllCounselors() {
        List<Counselor> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT counselor_id, name, gender, qualifications, specialization, avatar_url, available_time FROM counselors", null);
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

    // ✅ 4. 查询所有心理咨询师（包含资格证书）
    public List<Counselor> getAllCounselorsWithCertificate() {
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
                counselor.setCertificateUrl(cursor.getString(7)); // 资格证书路径

                list.add(counselor);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    // ✅ 5. 旧方法：更新心理咨询师（不包含资格证书）
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

    // ✅ 6. 新方法：更新心理咨询师（包含资格证书）
    public int updateCounselorWithCertificate(Counselor counselor) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", counselor.getName());
        values.put("gender", counselor.getGender());
        values.put("qualifications", counselor.getQualifications());
        values.put("specialization", counselor.getSpecialization());
        values.put("avatar_url", counselor.getAvatarUrl());
        values.put("certificate_url", counselor.getCertificateUrl()); // 更新资格证书路径
        values.put("available_time", counselor.getAvailableTime());
        int rowsAffected = db.update("counselors", values, "counselor_id=?", new String[]{String.valueOf(counselor.getCounselorId())});
        db.close();
        return rowsAffected;
    }
    // ✅ 7. 删除心理咨询师（Delete）
    public void deleteCounselor(int counselorId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("counselors", "counselor_id=?", new String[]{String.valueOf(counselorId)});
        db.close();
    }
    // 通过ID获取咨询师基本信息（不包含预约时间）
    public Counselor getCounselorBasicInfoById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Counselor counselor = null;

        Cursor cursor = db.rawQuery(
                "SELECT counselor_id, name, gender, avatar_url, certificate_url, qualifications, specialization " +
                        "FROM counselors WHERE counselor_id = ?",
                new String[]{String.valueOf(id)}
        );

        if (cursor.moveToFirst()) {
            counselor = new Counselor();
            counselor.setCounselorId(cursor.getInt(0));
            counselor.setName(cursor.getString(1));
            counselor.setGender(cursor.getString(2));
            counselor.setAvatarUrl(cursor.getString(3));
            counselor.setCertificateUrl(cursor.getString(4));
            counselor.setQualifications(cursor.getString(5));  // **新增获取 qualifications**
            counselor.setSpecialization(cursor.getString(6)); // **新增获取 specialization**
        }

        cursor.close();
        db.close();
        return counselor;
    }


    // 更新咨询师基本信息（不包含预约时间）
    public boolean updateCounselorBasicInfo(Counselor counselor) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", counselor.getName());
        values.put("gender", counselor.getGender());
        values.put("avatar_url", counselor.getAvatarUrl());
        values.put("certificate_url", counselor.getCertificateUrl());
        values.put("qualifications", counselor.getQualifications());  // **新增 qualifications**
        values.put("specialization", counselor.getSpecialization());  // **新增 specialization**

        int rowsAffected = db.update("counselors", values, "counselor_id = ?",
                new String[]{String.valueOf(counselor.getCounselorId())});

        db.close();
        return rowsAffected > 0;
    }



}
