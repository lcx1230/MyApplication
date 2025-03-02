package com.example.myapplication.model;

public class User {
    private int userId;
    private String username;
    private String password;
    private String name;
    private String gender;
    private String phone;
    private String email;
    private String avatarUrl;
    private String department;
    private String major;
    private String title;
    private String role;

    public User() {}

    public User(int userId, String username, String password, String name, String gender, String phone, String email, String avatarUrl, String department, String major, String title, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.department = department;
        this.major = major;
        this.title = title;
        this.role = role;
    }

    // Getters 和 Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
