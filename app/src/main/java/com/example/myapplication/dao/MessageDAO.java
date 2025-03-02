package com.example.myapplication.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    private DatabaseHelper dbHelper;

    public MessageDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // 插入消息（Create）
    public long insertMessage(Message message) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", message.getUserId());
        values.put("appointment_id", message.getAppointmentId());
        values.put("message_text", message.getMessageText());
        values.put("is_read", message.isRead() ? 1 : 0);
        long id = db.insert("messages", null, values);
        db.close();
        return id;
    }

    // 查询所有消息（Retrieve）
    public List<Message> getAllMessages() {
        List<Message> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM messages", null);
        if (cursor.moveToFirst()) {
            do {
                Message message = new Message();
                message.setMessageId(cursor.getInt(0));
                message.setUserId(cursor.getInt(1));
                message.setAppointmentId(cursor.getInt(2));
                message.setMessageText(cursor.getString(3));
                message.setRead(cursor.getInt(4) == 1);
                list.add(message);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    // 根据ID查询消息
    public Message getMessageById(int messageId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM messages WHERE message_id=?", new String[]{String.valueOf(messageId)});
        Message message = null;
        if (cursor.moveToFirst()) {
            message = new Message();
            message.setMessageId(cursor.getInt(0));
            message.setUserId(cursor.getInt(1));
            message.setAppointmentId(cursor.getInt(2));
            message.setMessageText(cursor.getString(3));
            message.setRead(cursor.getInt(4) == 1);
        }
        cursor.close();
        db.close();
        return message;
    }

    // 更新消息（Update）
    public int updateMessage(Message message) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", message.getUserId());
        values.put("appointment_id", message.getAppointmentId());
        values.put("message_text", message.getMessageText());
        values.put("is_read", message.isRead() ? 1 : 0);
        int rowsAffected = db.update("messages", values, "message_id=?", new String[]{String.valueOf(message.getMessageId())});
        db.close();
        return rowsAffected;
    }

    // 删除消息（Delete）
    public void deleteMessage(int messageId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("messages", "message_id=?", new String[]{String.valueOf(messageId)});
        db.close();
    }
}
