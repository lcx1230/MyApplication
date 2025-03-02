package com.example.myapplication.model;

public class Counselor {
    private int counselorId;
    private String name;
    private String gender;
    private String qualifications;
    private String specialization;
    private String avatarUrl;
    private String availableTime; // 存 JSON 数据

    public Counselor() {}

    public Counselor(int counselorId, String name, String gender, String qualifications, String specialization, String avatarUrl, String availableTime) {
        this.counselorId = counselorId;
        this.name = name;
        this.gender = gender;
        this.qualifications = qualifications;
        this.specialization = specialization;
        this.avatarUrl = avatarUrl;
        this.availableTime = availableTime;
    }

    public int getCounselorId() { return counselorId; }
    public void setCounselorId(int counselorId) { this.counselorId = counselorId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getQualifications() { return qualifications; }
    public void setQualifications(String qualifications) { this.qualifications = qualifications; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public String getAvailableTime() { return availableTime; }
    public void setAvailableTime(String availableTime) { this.availableTime = availableTime; }
}
