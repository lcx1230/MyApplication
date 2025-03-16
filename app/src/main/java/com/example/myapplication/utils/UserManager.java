package com.example.myapplication.utils;

import com.example.myapplication.model.User;

public class UserManager {
    private static UserManager instance;
    private User currentUser;

    private UserManager() {}

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void setUser(User user) {
        this.currentUser = user;
    }

    public User getUser() {
        return currentUser;
    }

    public void clearUser() {
        currentUser = null;
    }
}
