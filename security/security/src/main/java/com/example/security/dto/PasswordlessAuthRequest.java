package com.example.security.dto;

public class PasswordlessAuthRequest {
    private String signature;
    private String token;
    private String username;
    public PasswordlessAuthRequest(){}

    public PasswordlessAuthRequest(String signature, String token, String username) {
        this.signature = signature;
        this.token = token;
        this.username = username;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
