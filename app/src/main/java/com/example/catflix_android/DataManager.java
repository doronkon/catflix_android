package com.example.catflix_android;


public class DataManager {
    private static String currentUserID = null;
    private static Boolean isAdmin = null;

    private static String token = null;
    private static DataManager instance;


    public static void setToken(String newToken) {
        token = newToken;
    }

    public static String getTokenHeader() {
        return token != null ? token : null;
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