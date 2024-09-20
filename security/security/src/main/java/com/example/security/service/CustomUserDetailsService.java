package com.example.security.service;

import com.example.security.model.User;
import com.example.security.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // Funkcija koja na osnovu username-a iz baze vraca objekat User-a
    Logger logger= LoggerFactory.getLogger(CustomUserDetailsService.class);


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            logger.info("Loading user by username: {}", email);
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new UsernameNotFoundException(String.format("No user found with username '%s'.", email));
            } else {
                return user;
            }
        } catch (Exception e) {
            logger.error("An error occurred while loading user by username {}: {}", email, e.getMessage(), e);
            throw new UsernameNotFoundException("An error occurred while loading user.");
        }
    }


}
