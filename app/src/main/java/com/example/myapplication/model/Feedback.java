package com.example.myapplication.model;

public class Feedback {
    private int feedbackId;
    private int userId;
    private int counselorId;
    private int rating;
    private String comment;

    public Feedback() {}

    public Feedback(int feedbackId, int userId, int counselorId, int rating, String comment) {
        this.feedbackId = feedbackId;
        this.userId = userId;
        this.counselorId = counselorId;
        this.rating = rating;
        this.comment = comment;
    }

    public int getFeedbackId() { return feedbackId; }
    public void setFeedbackId(int feedbackId) { this.feedbackId = feedbackId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getCounselorId() { return counselorId; }
    public void setCounselorId(int counselorId) { this.counselorId = counselorId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
