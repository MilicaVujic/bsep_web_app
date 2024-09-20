package com.example.security.repository;

import com.example.security.model.Client;
import com.example.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("SELECT c FROM Client c JOIN c.user u WHERE u.id = :id")
    Client getByUserId(@Param("id") Long id);
    @Query("SELECT c FROM Client c WHERE c.id = :id")
    Client getById(@Param("id") Long id);
    @Query("SELECT c FROM Client c JOIN c.user u WHERE u.email = :email")
    Client findByEmail(String email);

    @Query("SELECT c FROM Client c JOIN c.user u WHERE u.username = :email")
    Client findByUsername(String email);
}
