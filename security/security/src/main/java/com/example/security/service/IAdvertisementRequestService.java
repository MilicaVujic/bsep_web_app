package com.example.security.service;

import com.example.security.model.AdvertisementRequest;
import com.example.security.model.Client;

import java.util.ArrayList;

public interface IAdvertisementRequestService {
    ArrayList<AdvertisementRequest> getAll();
    AdvertisementRequest save(AdvertisementRequest request,Long id);
    AdvertisementRequest getById(Long id);
    Long getClientId(Long id);
    Long getClientIdByRequest(Long id);

    Long getClientIdByRequestId(Long id);
    AdvertisementRequest update(AdvertisementRequest request);
    AdvertisementRequest rejectRequest(AdvertisementRequest request);

    ArrayList<AdvertisementRequest> findAllByClientId(Long id);
}
