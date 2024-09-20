package com.example.security.service;

import com.example.security.model.ActivationLink;
import com.example.security.repository.ActivationLinkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class ActivationLinkService implements IActivationLinkService{
    @Autowired
    private ActivationLinkRepository activationLinkRepository;
    Logger logger= LoggerFactory.getLogger(ActivationLinkService.class);

    @Override
    public boolean isLinkAlreadyUsed(String token) {
        Optional<ActivationLink> activationLink = null;
        try {
            logger.info("isLinkAlreadyUsed method in ActivationLinkService ended.");
            activationLink=this.activationLinkRepository.findByToken(token);
            if(activationLink != null) {
                logger.info("Activation link found by token {}", token);
            }
            else {
                logger.warn("Activation link with token {} not found.", token);

            }
        }
        catch (Exception e) {
            logger.error("An error occurred while finding by token: {}", token, e.getMessage(), e);
        }
        return activationLink.isPresent();
    }

    @Override
    public ActivationLink create(String username, String token) {
        ActivationLink activationLink=new ActivationLink();
        activationLink.setToken(token);
        activationLink.setUsername(username);
        ActivationLink retVal = null;
        try {
            logger.info("create method in ActivationLinkService started.");
            retVal = this.activationLinkRepository.save(activationLink);
            logger.info("create method in ActivationLinkService ended.");
        }
        catch (Exception e) {
            logger.error("An error occurred while creating the link: {}", e.getMessage(), e);
        }
        return retVal;
    }
}
