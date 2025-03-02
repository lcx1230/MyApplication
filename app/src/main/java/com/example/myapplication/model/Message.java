package com.example.myapplication.model;

public class Message {
    private int messageId;
    private int userId;
    private int appointmentId;
    private String messageText;
    private boolean isRead;

    public Message() {}

    public Message(int messageId, int userId, int appointmentId, String messageText, boolean isRead) {
        this.messageId = messageId;
        this.userId = userId;
        this.appointmentId = appointmentId;
        this.messageText = messageText;
        this.isRead = isRead;
    }

    public int getMessageId() { return messageId; }
    public void setMessageId(int messageId) { this.messageId = messageId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public String getMessageText() { return messageText; }
    public void setMessageText(String messageText) { this.messageText = messageText; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}
