package com.example.security.dto;

public class PasswordlessLoginRequest {
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public PasswordlessLoginRequest(){}

    public PasswordlessLoginRequest(String email) {
        this.email = email;
    }
}
