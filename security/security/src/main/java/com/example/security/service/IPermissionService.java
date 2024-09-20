package com.example.security.service;

import com.example.security.model.Permission;

import java.util.List;

public interface IPermissionService {
    Permission findByName(String name);
    Boolean hasPermission(String permission);
    List<Permission> findAll();
    List<Permission> existingForRole(String role);
    List<Permission> unexistingForRole(String role);
    Permission addPermision(String name,String role);
    Permission removePermission(String name,String role);
}
