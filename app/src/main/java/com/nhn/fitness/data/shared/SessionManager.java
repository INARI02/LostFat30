package com.nhn.fitness.data.shared;

import android.text.TextUtils;

import com.nhn.fitness.data.dto.UserDTO;
import com.nhn.fitness.data.room.AppDatabase;

import java.util.Date;

public class SessionManager {
    private static SessionManager instance;
    private UserDTO currentUser;
    private final AppSettings appSettings;

    private SessionManager() {
        appSettings = AppSettings.getInstance();
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void cleanSession() {
        AppDatabase.getInstance().clearUserData();
        currentUser = null;
        appSettings.setLoggedIn(false);
        appSettings.setLoggedInId(AppSettings.NO_ID);
    }

    public UserDTO getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserDTO currentUser) {
        this.currentUser = currentUser;
    }

    public String getCurrentUserMail() {
        return TextUtils.isEmpty(currentUser.getEmail())? "Không có" : currentUser.getEmail();
    }

    public String getCurrentUserPhone() {
        return TextUtils.isEmpty(currentUser.getPhoneNumber())? "Không có" : currentUser.getPhoneNumber();
    }

    public Date getCurrentUserBirthdate() {
        return (currentUser.getBirthdate() == null)? new Date() : currentUser.getBirthdate();
    }
}
