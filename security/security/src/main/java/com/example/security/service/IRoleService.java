package com.example.security.service;

import com.example.security.model.Role;

import java.util.List;

public interface IRoleService {
    Role findById(Long id);
    List<Role> findByName(String name);
    List<Role> findRolesByUserId(Long userId);
}
