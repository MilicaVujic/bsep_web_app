package com.example.security.dto;

public class VPNMessageDto {
    private String message;
    public VPNMessageDto(){}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public VPNMessageDto(String message) {
        this.message = message;
    }
}
