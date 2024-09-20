package com.example.security.service;

import com.example.security.model.ActivationLink;

public interface IActivationLinkService {
    boolean isLinkAlreadyUsed(String token);
    ActivationLink create(String username, String token);
}
