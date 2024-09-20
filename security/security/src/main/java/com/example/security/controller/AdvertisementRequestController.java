package com.example.security.controller;

import com.example.security.model.AdvertisementRequest;
import com.example.security.model.User;
import com.example.security.service.IAdvertisementRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;

@RestController
@RequestMapping(value = "/api/advertisementRequest", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class AdvertisementRequestController {

    @Autowired
    private IAdvertisementRequestService advertisementRequestService;
    Logger logger = LoggerFactory.getLogger(AdvertisementRequestController.class);

    @PreAuthorize("@permissionService.hasPermission('REQ_VIEW')")

    @GetMapping("")
    public ArrayList<AdvertisementRequest> getAll() {
        logger.info("getAll method in AdvertisementRequestController started.");

        return advertisementRequestService.getAll();
    }
    @PreAuthorize("@permissionService.hasPermission('CREATE_ADV_REQUEST')")

    @PostMapping("/creating/{id}")
    public AdvertisementRequest save(@RequestBody AdvertisementRequest request,@PathVariable("id") Long id) {
        logger.info("save method in AdvertisementRequestController started.");

        if(request.getActivityEnd()!=null && request.getActivityStart()!=null && request.getExpirationTime()!=null && !request.getDescription().equals("")
        && (new Date(request.getExpirationTime().getTime())).after(new Date()) && (new Date(request.getActivityStart().getTime()).before(new Date(request.getActivityEnd().getTime()))) && (new Date(request.getActivityEnd().getTime()).before(new Date(request.getExpirationTime().getTime()))))
             return advertisementRequestService.save(request,id);
        return null;
    }
    @PreAuthorize("@permissionService.hasPermission('REQ_VIEW')")

    @GetMapping("/{id}")
    public AdvertisementRequest getById(@PathVariable("id") Long id) {
        logger.info("getById method in AdvertisementRequestController started.");

        return advertisementRequestService.getById(id);
    }
    /*@PreAuthorize("@permissionService.hasPermission('CREATE_ADV')")
    @GetMapping("/clientId/{id}")
    public Long getClientId(@PathVariable("id") Long id) {
        logger.info("getClientId method in AdvertisementRequestController started.");

<<<<<<< HEAD
        return advertisementRequestService.getClientIdByRequest(id);
=======
        return advertisementRequestService.getClientId(id);
    }*/

    @PreAuthorize("@permissionService.hasPermission('CREATE_ADV')")
    @GetMapping("/clientId/{id}")
    public Long getClientIdByRequestId(@PathVariable("id") Long id) {
        logger.info("getClientIdByRequestId method in AdvertisementRequestController started.");
        return advertisementRequestService.getClientIdByRequestId(id);
    }
    @PreAuthorize("@permissionService.hasPermission('REQ_VIEW')")

    @PutMapping("/reject")
    public AdvertisementRequest rejectRequest(@RequestBody AdvertisementRequest request) {
        logger.info("rejectRequest method in AdvertisementRequestController started.");

        return advertisementRequestService.rejectRequest(request);
    }

}
