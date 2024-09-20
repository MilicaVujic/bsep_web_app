package com.example.security.controller;

import com.example.security.model.Advertisement;
import com.example.security.model.Client;
import com.example.security.model.Permission;
import com.example.security.model.Role;
import com.example.security.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/notAuthenticated", produces = MediaType.APPLICATION_JSON_VALUE)
public class NonAuthenticatedController {

    @Autowired
    IRoleService roleService;
    @Autowired
    private IClientService clientService;
    @Autowired
    private IAdvertisementService advertisementService;

    @Autowired
    private IRateLimiterService rateLimiterService;
    Logger logger = LoggerFactory.getLogger(NonAuthenticatedController.class);
    @GetMapping("/visiting/{id}")
    @PermitAll
    public ResponseEntity<Advertisement> getAdvertisement(@PathVariable Long id) {
        logger.info("getAdvertisement method in AdvertisementController started.");
        String packageType = "";    //tip klijenta koji je vlasnik reklame
        List<Role> roles=roleService.findByName("ROLE_CLIENT");
        ArrayList<Advertisement> advertisements=advertisementService.getAll();
        Advertisement adv = advertisementService.getById(id);
        Client client = clientService.getById(adv.getClient().getId());
        packageType = client.getPackageType().toString();

        boolean allowed = rateLimiterService.tryAcquire(packageType, id);
        if (!allowed) {
            return ResponseEntity.status(429).build();
        }
        Advertisement retVal = advertisementService.getById(id);
        return ResponseEntity.ok(retVal);
    }

}
