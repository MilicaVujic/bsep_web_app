package com.example.security.service;

import com.example.security.model.Advertisement;

import java.util.ArrayList;

public interface IAdvertisementService {
    ArrayList<Advertisement> getAll();
    Advertisement save(Advertisement advertisement,Long id, Long requestId);
    ArrayList<Advertisement> findAllByClientId(Long id);
    Advertisement getById(Long id);
}
