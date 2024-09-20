package com.example.security.service;

import com.example.security.dto.RegistrationRequestDto;
import com.example.security.model.RegistrationRequest;

import java.util.List;

public interface IRegistrationRequestService {
   RegistrationRequest save(RegistrationRequestDto userRequest);
    RegistrationRequest accept(com.example.security.model.RegistrationRequest request);
    RegistrationRequest reject(com.example.security.model.RegistrationRequest request);
    List<RegistrationRequest> findAll();
    RegistrationRequest findById(Long id);


}
