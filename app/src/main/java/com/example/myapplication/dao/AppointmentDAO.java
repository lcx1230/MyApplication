package com.example.myapplication.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.myapplication.model.Appointment;
import com.example.myapplication.model.AppointmentDisplay;

import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {
    private DatabaseHelper dbHelper;

    public AppointmentDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // 插入预约（Create）
    public long insertAppointment(Appointment appointment) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", appointment.getUserId());
        values.put("counselor_id", appointment.getCounselorId());
        values.put("appointment_time", appointment.getAppointmentTime()); // JSON 格式字符串
        values.put("status", appointment.getStatus());
        values.put("feedback_id", appointment.getFeedbackId());
        long id = db.insert("appointments", null, values);
        db.close();
        return id;
    }

    // 查询所有预约（Retrieve）
    public List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM appointments", null);
        if (cursor.moveToFirst()) {
            do {
                Appointment appointment = new Appointment();
                appointment.setAppointmentId(cursor.getInt(0));
                appointment.setUserId(cursor.getInt(1));
                appointment.setCounselorId(cursor.getInt(2));
                appointment.setAppointmentTime(cursor.getString(3));
                appointment.setStatus(cursor.getString(4));
                appointment.setFeedbackId(cursor.getInt(5));
                list.add(appointment);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    // 根据ID查询预约
    public Appointment getAppointmentById(int appointmentId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM appointments WHERE appointment_id=?", new String[]{String.valueOf(appointmentId)});
        Appointment appointment = null;
        if (cursor.moveToFirst()) {
            appointment = new Appointment();
            appointment.setAppointmentId(cursor.getInt(0));
            appointment.setUserId(cursor.getInt(1));
            appointment.setCounselorId(cursor.getInt(2));
            appointment.setAppointmentTime(cursor.getString(3));
            appointment.setStatus(cursor.getString(4));
            appointment.setFeedbackId(cursor.getInt(5));
        }
        cursor.close();
        db.close();
        return appointment;
    }

    // 更新预约（Update）
    public int updateAppointment(Appointment appointment) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", appointment.getUserId());
        values.put("counselor_id", appointment.getCounselorId());
        values.put("appointment_time", appointment.getAppointmentTime());
        values.put("status", appointment.getStatus());
        values.put("feedback_id", appointment.getFeedbackId());
        int rowsAffected = db.update("appointments", values, "appointment_id=?", new String[]{String.valueOf(appointment.getAppointmentId())});
        db.close();
        return rowsAffected;
    }

    // 删除预约（Delete）
    public void deleteAppointment(int appointmentId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("appointments", "appointment_id=?", new String[]{String.valueOf(appointmentId)});
        db.close();
    }
    public List<AppointmentDisplay> getAppointmentsWithCounselorNameByUser(int userId, String statusFilter) {
        List<AppointmentDisplay> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT a.appointment_id, a.appointment_time, a.status, " +
                "c.name AS counselor_name " +
                "FROM appointments a " +
                "JOIN counselors c ON a.counselor_id = c.counselor_id " +
                "WHERE a.user_id = ? AND a.status = ? " +
                "ORDER BY a.appointment_time DESC";

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(userId), statusFilter});
        while (cursor.moveToNext()) {
            AppointmentDisplay item = new AppointmentDisplay();
            item.setAppointmentId(cursor.getInt(0));
            item.setAppointmentTime(cursor.getString(1));
            item.setStatus(cursor.getString(2));
            item.setCounselorName(cursor.getString(3));
            list.add(item);
        }
        cursor.close();
        db.close();
        return list;
    }
    public void updateAppointmentStatus(int appointmentId, String newStatus) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", newStatus);
        int rows = db.update("appointments", values, "appointment_id = ?", new String[]{String.valueOf(appointmentId)});
        Log.d("DBUpdate", "更新了 " + rows + " 行");
        db.close();
    }
    public boolean updateAppointmentTime(int appointmentId, String jsonTime) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("appointment_time", jsonTime);
        int rows = db.update("appointments", values, "appointment_id = ?", new String[]{String.valueOf(appointmentId)});
        return rows > 0;
    }




}
