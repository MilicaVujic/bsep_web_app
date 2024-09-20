package com.example.security.dto;

public class PermissionDto {
    private String name;
    public PermissionDto() {
    }
    public PermissionDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
