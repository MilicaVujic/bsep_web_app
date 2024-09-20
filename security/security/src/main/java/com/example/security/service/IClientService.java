package com.example.security.service;

import com.example.security.model.Client;
import com.example.security.model.RegistrationRequest;
import com.example.security.model.User;

import java.util.List;

public interface IClientService {
    Client save(RegistrationRequest userRequest);
    Client getByUserId(Long id);
    Client getById(Long id);
    Client findByEmail(String email);

}
