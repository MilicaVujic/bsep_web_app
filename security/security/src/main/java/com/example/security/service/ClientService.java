package com.example.security.service;

import com.example.security.model.*;
import com.example.security.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClientService implements IClientService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private  TfaAuthentication tfaAuthentication;
    Logger logger= LoggerFactory.getLogger(ClientService.class);



    @Override
    public Client save(RegistrationRequest userRequest) {

        try {
            logger.info("Saving client from registration request...");

            Client client = new Client();
            Address address = new Address();
            address.setCity(userRequest.getCity());
            address.setCountry(userRequest.getCountry());
            address.setStreet(userRequest.getStreet());
            address.setStreetNumber(userRequest.getStreetNumber());

            client.setPhone(userRequest.getPhone());
            client.setType(userRequest.getType());
            client.setPackageType(userRequest.getPackageType());

            if (client.getType().equals(ClientType.PRAVNO))
                client.setPib(userRequest.getPib());

            User user = new User();
            user.setBlocked(false);
            user.setSecret(tfaAuthentication.generateNewSecret());
            user.setEmail(userRequest.getEmail());
            user.setEnabled(false);
            user.setFirstName(userRequest.getName());
            user.setLastName(userRequest.getSurname());
            user.setUsername(userRequest.getUsername());
            user.setAddress(address);
            user.setPhone(userRequest.getPhone());
            user.setKeyStorePassword(user.generateRandomString());
            user.getAddress().setKeyStorePassword(user.getKeyStorePassword());

            String salt = generateSalt();
            user.setSalt(salt);
            user.setPassword(passwordEncoder.encode(userRequest.getPassword() + salt));

            List<Role> roles = roleService.findByName("ROLE_CLIENT");
            user.setRoles(roles);

            client.setUser(user);
            clientRepository.save(client);

            logger.info("Client saved successfully from registration request.");

            return null;
        } catch (Exception e) {
            logger.error("An error occurred while saving client from registration request: {}", e.getMessage(), e);
            return null;
        }
    }


    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return bytesToHex(saltBytes);
    }
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }


    @Override
    public Client getByUserId(Long email) {
        try {
            logger.info("Fetching client by user ID: {}", email);
            Client client = clientRepository.getByUserId(email);
            if (client == null) {
                logger.warn("Client with user ID {} not found.", email);
            } else {
                logger.info("Client with user ID {} found.", email);
            }
            return client;
        } catch (Exception e) {
            logger.error("An error occurred while fetching client by user ID {}: {}", email, e.getMessage(), e);
            return null;
        }
    }



    @Override
    public Client getById(Long email) {
        try {
            logger.info("Fetching client by ID: {}", email);
            Client client = clientRepository.getById(email);
            if (client == null) {
                logger.warn("Client with ID {} not found.", email);
            } else {
                logger.info("Client with ID {} found.", email);
            }
            return client;
        } catch (Exception e) {
            logger.error("An error occurred while fetching client by ID {}: {}", email, e.getMessage(), e);
            return null;
        }
    }


    @Override
    public Client findByEmail(String email) {
        try {
            logger.info("Fetching client by email: {}", email);
            Client client = clientRepository.findByEmail(email);
            if (client == null) {
                logger.warn("Client with email {} not found.", email);
            } else {
                logger.info("Client with email {} found.", email);
            }
            return client;
        } catch (Exception e) {
            logger.error("An error occurred while fetching client by email {}: {}", email, e.getMessage(), e);
            return null;
        }
    }


}
