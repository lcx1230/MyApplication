package com.example.myapplication.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.model.Feedback;

import java.util.ArrayList;
import java.util.List;

public class FeedbackDAO {
    private DatabaseHelper dbHelper;

    public FeedbackDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // 插入反馈（Create）
    public long insertFeedback(Feedback feedback) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", feedback.getUserId());
        values.put("counselor_id", feedback.getCounselorId());
        values.put("rating", feedback.getRating());
        values.put("comment", feedback.getComment());
        long id = db.insert("feedback", null, values);
        db.close();
        return id;
    }

    // 查询所有反馈（Retrieve）
    public List<Feedback> getAllFeedback() {
        List<Feedback> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM feedback", null);
        if (cursor.moveToFirst()) {
            do {
                Feedback feedback = new Feedback();
                feedback.setFeedbackId(cursor.getInt(0));
                feedback.setUserId(cursor.getInt(1));
                feedback.setCounselorId(cursor.getInt(2));
                feedback.setRating(cursor.getInt(3));
                feedback.setComment(cursor.getString(4));
                list.add(feedback);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    // 根据ID查询反馈
    public Feedback getFeedbackById(int feedbackId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM feedback WHERE feedback_id=?", new String[]{String.valueOf(feedbackId)});
        Feedback feedback = null;
        if (cursor.moveToFirst()) {
            feedback = new Feedback();
            feedback.setFeedbackId(cursor.getInt(0));
            feedback.setUserId(cursor.getInt(1));
            feedback.setCounselorId(cursor.getInt(2));
            feedback.setRating(cursor.getInt(3));
            feedback.setComment(cursor.getString(4));
        }
        cursor.close();
        db.close();
        return feedback;
    }

    // 更新反馈（Update）
    public int updateFeedback(Feedback feedback) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", feedback.getUserId());
        values.put("counselor_id", feedback.getCounselorId());
        values.put("rating", feedback.getRating());
        values.put("comment", feedback.getComment());
        int rowsAffected = db.update("feedback", values, "feedback_id=?", new String[]{String.valueOf(feedback.getFeedbackId())});
        db.close();
        return rowsAffected;
    }

    // 删除反馈（Delete）
    public void deleteFeedback(int feedbackId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("feedback", "feedback_id=?", new String[]{String.valueOf(feedbackId)});
        db.close();
    }
}
