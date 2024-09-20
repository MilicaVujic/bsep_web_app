package com.example.security.repository;

import com.example.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = 'ROLE_EMPLOYEE'")
    List<User> findEmployees();

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = 'ROLE_CLIENT'")
    List<User> findClients();

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = 'ROLE_ADMIN'")
    List<User> findAdmins();
}

