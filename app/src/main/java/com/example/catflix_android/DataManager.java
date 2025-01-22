package com.example.catflix_android;

import android.content.Context;
import android.content.Intent;

import com.example.catflix_android.LoginActivity;


public class DataManager {
    private static String currentUserID;
    private static Boolean isAdmin;

    private static String token;
    private static DataManager instance;
    private static boolean initialized;

    public static boolean isInitialized() {
        return initialized;
    }

    public static void setInitialized(boolean initialized) {
        DataManager.initialized = initialized;
    }

    public static void setToken(String newToken) {
        token = newToken;
    }

    public static String getTokenHeader() {
        return token != null ? "Bearer " + token : null;
    }


    public static synchronized DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public static String getCurrentUserId() {
        return currentUserID;
    }

    public static void setCurrentUserID(String currentUserId) {
        DataManager.currentUserID = currentUserId;
    }

    public static Boolean getIsAdmin() {
        return isAdmin;
    }

    public static void setIsAdmin(Boolean isAdmin) {
        DataManager.isAdmin = isAdmin;
    }
}