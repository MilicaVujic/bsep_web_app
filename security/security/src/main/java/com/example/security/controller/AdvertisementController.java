package com.example.security.controller;

import com.example.security.model.Advertisement;
import com.example.security.model.AdvertisementRequest;
import com.example.security.model.PackageType;
import com.example.security.service.IAdvertisementService;
import com.example.security.service.IRateLimiterService;
import com.example.security.service.RateLimiterService;
import com.example.security.util.AdvertisementSimulator;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.websocket.server.PathParam;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/api/advertisement", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class AdvertisementController {
    private final Cache<Long, Bucket> cache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();
    Logger logger = LoggerFactory.getLogger(AdvertisementController.class);





    @Autowired
    private IAdvertisementService advertisementService;
    @PreAuthorize("@permissionService.hasPermission('ALL_ADV')")

    @GetMapping("")
    public ArrayList<Advertisement> getAll() {
        logger.info("getAll method in AdvertisementController started.");
        return advertisementService.getAll();
    }

    @PreAuthorize("@permissionService.hasPermission('CREATE_ADV')")

    @PostMapping("/creating/{id}/{requestId}")
    public Advertisement save(@RequestBody Advertisement advertisement,@PathVariable("id") Long id,@PathVariable("requestId") Long requestId) {
        logger.info("save method in AdvertisementController started.");

        if(!advertisement.getSlogan().equals("") && !advertisement.getDescription().equals("") && advertisement.getDuration()>0)
            return advertisementService.save(advertisement,id,requestId);
        return null;
    }
    @PreAuthorize("@permissionService.hasPermission('MY_ADV')")

    @GetMapping("/gettingByClient/{id}")
    public ArrayList<Advertisement> findAllByClientId(@PathVariable("id") Long id) {
        logger.info("findAllByClientId method in AdvertisementController started.");

        return advertisementService.findAllByClientId(id);
    }


    @GetMapping("/{id}")
    public Advertisement getById(@PathVariable("id") Long id) {
        logger.info("getById method in AdvertisementController started.");

        return advertisementService.getById(id);
    }


}
