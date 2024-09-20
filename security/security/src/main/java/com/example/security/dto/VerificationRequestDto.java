package com.example.security.dto;

public class VerificationRequestDto {
    private String email;
    private String code;

    public VerificationRequestDto(){}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public VerificationRequestDto(String email, String code) {
        this.email = email;
        this.code = code;
    }
}
