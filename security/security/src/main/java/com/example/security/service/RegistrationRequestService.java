package com.example.security.service;

import com.example.security.dto.RegistrationRequestDto;
import com.example.security.model.RequestStatus;
import com.example.security.repository.RegistrationRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RegistrationRequestService implements  IRegistrationRequestService{
    @Autowired
    RegistrationRequestRepository registrationRequestRepository;
    Logger logger= LoggerFactory.getLogger(RegistrationRequestService.class);

    @Override
    public com.example.security.model.RegistrationRequest save(RegistrationRequestDto registrationRequestDto) {

        try {
            logger.info("Saving registration request for user '{}'", registrationRequestDto.getUsername());
            com.example.security.model.RegistrationRequest request = new com.example.security.model.RegistrationRequest();
            request.setRequestStatus(RequestStatus.NEW);
            request.setCity(registrationRequestDto.getCity());
            request.setCountry(registrationRequestDto.getCountry());
            request.setName(registrationRequestDto.getName());
            request.setCountry(registrationRequestDto.getCountry());
            request.setPhone(registrationRequestDto.getPhone());
            request.setPackageType(registrationRequestDto.getPackageType());
            request.setStreet(registrationRequestDto.getStreet());
            request.setStreetNumber(registrationRequestDto.getStreetNumber());
            request.setPib(registrationRequestDto.getPib());
            request.setSurname(registrationRequestDto.getSurname());
            request.setUsername(registrationRequestDto.getUsername());
            request.setPassword(registrationRequestDto.getPassword());
            request.setType(registrationRequestDto.getType());
            request.setEmail(registrationRequestDto.getEmail());
            request.setKeyStorePassword(request.generateRandomString());
            com.example.security.model.RegistrationRequest retVal = this.registrationRequestRepository.save(request);
            if(retVal != null) {
                logger.info("Registration request {} has been saved.", request.getUsername());

            }
            else {
                logger.warn("Registration request {} not found.", request.getUsername());

            }
            return retVal;
        } catch (Exception e) {
            logger.error("An error occurred while saving registration request for user '{}': {}", registrationRequestDto.getUsername(), e.getMessage(), e);
            throw null;
        }
    }




    @Override
    public com.example.security.model.RegistrationRequest accept(com.example.security.model.RegistrationRequest request) {
        logger.info("Starting acceptance of registration request");
        try {
            request.setRequestStatus(RequestStatus.ACCEPTED);
            com.example.security.model.RegistrationRequest acceptedRequest = registrationRequestRepository.save(request);
            logger.info("Registration request successfully accepted");
            return acceptedRequest;
        } catch (Exception e) {
            logger.error("Error occurred during acceptance of registration request: {}", e.getMessage(), e);
            return null;
        }
    }






    @Override
    public com.example.security.model.RegistrationRequest reject(com.example.security.model.RegistrationRequest request) {
        logger.info("Starting rejection of registration request");
        try {
            request.setRequestStatus(RequestStatus.REJECTED);
            request.setRejectingDate(System.currentTimeMillis());
            com.example.security.model.RegistrationRequest rejectedRequest = registrationRequestRepository.save(request);
            logger.info("Registration request successfully rejected");
            return rejectedRequest;
        } catch (Exception e) {
            logger.error("Error occurred during rejection of registration request: {}", e.getMessage(), e);
            return null;
        }
    }


    @Override
    public List<com.example.security.model.RegistrationRequest> findAll() {
        logger.info("Starting retrieval of all registration requests");
        try {
            List<com.example.security.model.RegistrationRequest> allRequests = registrationRequestRepository.findAll();
            logger.info("Successfully retrieved all registration requests");
            return allRequests;
        } catch (Exception e) {
            logger.error("Error occurred during retrieval of all registration requests: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }




    @Override
    public com.example.security.model.RegistrationRequest findById(Long id) {
        logger.info("Starting retrieval of registration request by ID '{}'", id);
        try {
            com.example.security.model.RegistrationRequest request = registrationRequestRepository.findById(id).orElse(null);
            if (request != null) {
                logger.info("Successfully retrieved registration request with ID '{}'", id);
            } else {
                logger.info("No registration request found with ID '{}'", id);
            }
            return request;
        } catch (Exception e) {
            logger.error("Error occurred during retrieval of registration request with ID '{}': {}", id, e.getMessage(), e);
            return null;
        }
    }

}
