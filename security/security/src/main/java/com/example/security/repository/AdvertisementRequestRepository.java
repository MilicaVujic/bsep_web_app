package com.example.security.repository;

import com.example.security.model.Advertisement;
import com.example.security.model.AdvertisementRequest;
import com.example.security.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

public interface AdvertisementRequestRepository  extends JpaRepository<AdvertisementRequest, Long> {
    @Query("SELECT c FROM AdvertisementRequest c WHERE c.id = :id")
    AdvertisementRequest getById(@Param("id") Long id);
    @Query("SELECT c.client.id FROM AdvertisementRequest c WHERE c.client.user.id = :id")
    Long getClientId(@Param("id") Long id);

    @Query("SELECT c.client.id FROM AdvertisementRequest c WHERE c.id = :id")
    Long getClientIdByRequest(@Param("id") Long id);

    @Query("SELECT c.client.id FROM AdvertisementRequest c WHERE c.id = :id")
    Long getClientIdByRequestId(@Param("id") Long id);

    @Query("SELECT c FROM AdvertisementRequest c WHERE c.client.user.id = :clientId")
    ArrayList<AdvertisementRequest> findAllByClientId(Long clientId);
}
