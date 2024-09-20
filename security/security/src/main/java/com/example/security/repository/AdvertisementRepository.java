package com.example.security.repository;

import com.example.security.model.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

    @Query("SELECT c FROM Advertisement c WHERE c.client.user.id = :clientId")
    ArrayList<Advertisement> findAllByClientId(Long clientId);

    @Query("SELECT a FROM Advertisement a JOIN FETCH a.client JOIN FETCH a.request WHERE a.id = :id")
    Advertisement getById(Long id);
}
