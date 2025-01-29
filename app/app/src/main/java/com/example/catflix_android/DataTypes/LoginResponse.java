package com.example.catflix_android.DataTypes;

public class LoginResponse {
    private String token;
    private boolean admin;
    private String id;

    public LoginResponse(String token, boolean admin, String id) {
        this.token = token;
        this.admin = admin;
        this.id = id;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

