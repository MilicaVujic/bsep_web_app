package com.example.security.controller;

import com.example.security.dto.RegistrationRequestDto;
import com.example.security.model.ClientType;
import com.example.security.model.RegistrationRequest;
import com.example.security.model.RequestStatus;
import com.example.security.service.EmailService;
import com.example.security.service.IClientService;
import com.example.security.service.IRegistrationRequestService;
import com.example.security.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class RegistrationRequestController {
    @Autowired
    IRegistrationRequestService registrationRequestService;
    @Autowired
    IClientService clientService;
    @Autowired
    EmailService emailService;
    @Autowired
    IUserService userService;
    Logger logger = LoggerFactory.getLogger(RegistrationRequestController.class);


    @PostMapping("/registerRequest")
    public ResponseEntity<RegistrationRequest> createRegistrationRequest(@RequestBody RegistrationRequestDto registrationRequestDto) {
        logger.info("createRegistrationRequest method in RegistrationRequestController started.");

        for(RegistrationRequest request:registrationRequestService.findAll()) {
            if (request.getRequestStatus().equals(RequestStatus.REJECTED) && (System.currentTimeMillis() - request.getRejectingDate() < 7 * 24 * 60 * 60 * 100) && registrationRequestDto.getEmail().equals(request.getEmail()))
                return new ResponseEntity<RegistrationRequest>(HttpStatus.METHOD_NOT_ALLOWED);
            if (request.getEmail().equals(registrationRequestDto.getEmail()) && request.getRequestStatus().equals(RequestStatus.NEW))
                return new ResponseEntity<RegistrationRequest>(HttpStatus.BAD_REQUEST);
        }
        if(registrationRequestDto.getEmail()!=null && userService.findByEmail(registrationRequestDto.getEmail())==null) {
            String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,}$";
            boolean isPasswordValid = registrationRequestDto.getPassword().matches(regex);

            boolean isInvalidFields=registrationRequestDto.getName().isEmpty() || registrationRequestDto.getPhone().isEmpty() || registrationRequestDto.getCountry().isEmpty()
                    || registrationRequestDto.getCity().isEmpty() || registrationRequestDto.getStreet().isEmpty() ||
                    registrationRequestDto.getStreetNumber().isEmpty() || registrationRequestDto.getType().toString().isEmpty() || !isPasswordValid;
            if( isInvalidFields)
                return new ResponseEntity<RegistrationRequest>(HttpStatus.BAD_REQUEST);
            if(registrationRequestDto.getType().equals(ClientType.FIZICKO) && registrationRequestDto.getSurname().isEmpty())
                return new ResponseEntity<RegistrationRequest>(HttpStatus.BAD_REQUEST);
            if(registrationRequestDto.getType().equals(ClientType.PRAVNO)) {
                String regexPib = "^\\d{9}$";
                if(!registrationRequestDto.getPib().matches(regexPib))
                    return new ResponseEntity<RegistrationRequest>(HttpStatus.BAD_REQUEST);
            }
            return ResponseEntity.ok(this.registrationRequestService.save(registrationRequestDto));
        } else
            return ResponseEntity.ok(null);
    }
    @PreAuthorize("@permissionService.hasPermission('ALL_REG_REQ')")
    @GetMapping("/registerRequests")
    public ResponseEntity<List<RegistrationRequest>> getAllRegistrationRequests() {
        logger.info("getAllRegistrationRequests method in RegistrationRequestController started.");

        return ResponseEntity.ok(this.registrationRequestService.findAll());
    }
    @PreAuthorize("@permissionService.hasPermission('ACCEPT_REG_REQ')")
    @PutMapping("/registrationRequest/accept")
    public ResponseEntity<List<com.example.security.model.RegistrationRequest>> acceptRegistrationRequest(@RequestBody RegistrationRequest registrationRequest) {
        logger.info("acceptRegistrationRequest method in RegistrationRequestController started.");

        registrationRequestService.accept(registrationRequest);
        clientService.save(registrationRequest);
        emailService.sendActivationEmail(registrationRequest.getEmail(),registrationRequest.getUsername());
        return ResponseEntity.ok(this.registrationRequestService.findAll());
    }
    @PreAuthorize("@permissionService.hasPermission('REJECT_REG_REQ')")
    @PutMapping("/registrationRequest/reject/{id}")
    public ResponseEntity<List<com.example.security.model.RegistrationRequest>> rejectRegistrationRequest(@RequestBody String explantation, @PathVariable("id") Long id) {
        logger.info("rejectRegistrationRequest method in RegistrationRequestController started.");

        com.example.security.model.RegistrationRequest request=registrationRequestService.findById(id);
        if(request!=null) {
            registrationRequestService.reject(request);
            emailService.sendRejectingExplanation(request.getEmail(),explantation);
        }
        return ResponseEntity.ok(this.registrationRequestService.findAll());
    }
}
