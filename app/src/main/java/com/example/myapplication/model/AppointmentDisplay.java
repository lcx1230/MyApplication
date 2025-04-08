package com.example.myapplication.model;

import org.json.JSONObject;

public class AppointmentDisplay {
    private int appointmentId;
    private String counselorName;
    private String appointmentTime;
    private String status;

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getCounselorName() {
        return counselorName;
    }

    public void setCounselorName(String counselorName) {
        this.counselorName = counselorName;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFormattedDateTime() {
        try {
            JSONObject json = new JSONObject(appointmentTime);
            String date = json.optString("date");
            String time = json.optString("time");
            return date + "，" + time;
        } catch (Exception e) {
            e.printStackTrace();
            return appointmentTime; // 如果解析失败就原样返回
        }
    }
}
