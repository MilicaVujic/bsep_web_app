package com.example.security.service;

import com.example.security.model.PackageType;

public interface IRateLimiterService {


    boolean tryAcquire(String packageType, Long advertisementId);
}
