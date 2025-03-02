package com.example.myapplication.model;

public class Appointment {
    private int appointmentId;
    private int userId;
    private int counselorId;
    private String appointmentTime; // 存 JSON 数据
    private String status;
    private int feedbackId;

    public Appointment() {}

    public Appointment(int appointmentId, int userId, int counselorId, String appointmentTime, String status, int feedbackId) {
        this.appointmentId = appointmentId;
        this.userId = userId;
        this.counselorId = counselorId;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.feedbackId = feedbackId;
    }

    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getCounselorId() { return counselorId; }
    public void setCounselorId(int counselorId) { this.counselorId = counselorId; }

    public String getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(String appointmentTime) { this.appointmentTime = appointmentTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getFeedbackId() { return feedbackId; }
    public void setFeedbackId(int feedbackId) { this.feedbackId = feedbackId; }
}
