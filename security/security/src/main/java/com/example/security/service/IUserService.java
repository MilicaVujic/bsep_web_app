package com.example.security.service;

import com.example.security.model.User;

import java.util.ArrayList;
import java.util.List;

public interface IUserService {
    User enable(String username);
    User findByEmail(String email);
    User save(User user, Boolean hashPassword, String role);
    User saveUser(User user, String role);
    ArrayList<User> getEmployees();
    ArrayList<User> getClients();
    void changePassword(String email, String password);
    List<User> getAll();
    User getById(Long id);
    List<User> block (String email);
    List<User> unblock (String email);
    User resetToken(String email);
    User setNewPassword(String email, String password);
    void deleteUserData(Long id);

}
