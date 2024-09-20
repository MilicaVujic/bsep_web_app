package com.example.security.service;

import com.example.security.controller.UserController;
import com.example.security.model.*;
import com.example.security.repository.AddressRepository;
import com.example.security.repository.ClientRepository;
import com.example.security.repository.RoleRepository;
import com.example.security.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AdvertisementService advertisementService;
    @Autowired
    private AdvertisementRequestService advertisementRequestService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private EmailService emailService;
    Logger logger= LoggerFactory.getLogger(UserService.class);
    @Override
    public User enable(String username) {
        User u = null;
        try {
            u = userRepository.findByUsername(username);
            if (u != null) {
                u.setEnabled(true);
                userRepository.save(u);
                logger.info("User {} has been enabled.", username);
            } else {
                logger.warn("User with username {} not found.", username);
            }
        } catch (Exception e) {
            logger.error("An error occurred while enabling the user {}: {}", username, e.getMessage(), e);
        }
        return u;
    }

    @Override
    public User findByEmail(String email) {
        User u= new User();
        //EncryptionUtil eu = new EncryptionUtil();
        //eu.loadKeyStore("sensitiveData_ks", "1234567812345678".toCharArray());
        //eu.saveKeyStore("sensitiveData_ks", "1234567812345678".toCharArray());
        try{
            logger.info("findByEmail method in UserService started.");
            u=userRepository.findByUsername(email);
            logger.info("findByEmail method in UserService ended.");
        }catch(Exception e){
            logger.error("Exception in findByEmail of service class");
            //e.printStackTrace();
        }
        return u;
    }

    public User save(User user, Boolean hashPassword, String role) {
        User u = null;
        try {
            logger.info("save method in UserService started.");
            User storedUser= userRepository.findByEmail(user.getEmail());
            user.setSalt(storedUser.getSalt());
            user.setEnabled(true);
            List<Role> roles = new ArrayList<>();
            roles=roleService.findRolesByUserId(user.getId());
            user.setRoles(roles);
            user.setPasswordChanged(storedUser.isPasswordChanged());
            if(hashPassword) {
                user.setSalt(generateSalt());
                user.setPassword(passwordEncoder.encode(user.getPassword() + user.getSalt()));
            }else{
                user.setPassword(storedUser.getPassword());
                user.setLastPasswordResetDate(storedUser.getLastPasswordResetDate());
            }
            Address existingAddress = storedUser.getAddress();
            Address updatedAddress = user.getAddress();
            existingAddress.setCity(updatedAddress.getCity());
            existingAddress.setCountry(updatedAddress.getCountry());
            existingAddress.setStreet(updatedAddress.getStreet());
            existingAddress.setStreetNumber(updatedAddress.getStreetNumber());
            user.setAddress(existingAddress);
            u= userRepository.save(user);
            logger.info("save method in UserService ended.");
        } catch (Exception e) {
            logger.error("An error occurred while saving the user: {}", e.getMessage(), e);
        }
        return u;
    }
    @Override
    public User saveUser(User user, String role) {
        try {
            user.setEnabled(true);
            String salt = generateSalt();
            user.setSalt(salt);
            user.setPassword(passwordEncoder.encode(user.getPassword() + salt));
            List<Role> roles = roleService.findByName(role);
            user.setRoles(roles);
            user.setPasswordChanged(false);
            userRepository.save(user);
            logger.info("User {} has been saved with role {}.", user.getUsername(), role);
            return user;
        } catch (Exception e) {
            logger.error("An error occurred while saving the user {}: {}", user.getUsername(), e.getMessage(), e);
            return null;
        }
    }

    @Override
    public ArrayList<User> getEmployees() {
        ArrayList<User> employees=new ArrayList<>();
        try{
            logger.info("getEmployees method in UserService started.");
            employees=(ArrayList<User>) userRepository.findEmployees();
            logger.info("getEmployees method in UserService ended.");
        }catch(Exception e){
            logger.error("Exception in getEmployees of service class.");
            e.printStackTrace();
        }
        return employees;
    }

    @Override
    public ArrayList<User> getClients() {
        ArrayList<User> clients=new ArrayList<>();
        try{
            logger.info("getClients method in UserService started.");
            clients=(ArrayList<User>) userRepository.findClients();
            logger.info("getClients method in UserService ended.");
        }catch(Exception e){
            logger.error("Exception in getClients of UserService class.");
            e.printStackTrace();
        }
        return clients;
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
    public void changePassword(String username, String newPassword) {
        try {
            User user = userRepository.findByUsername(username);
            if (user == null) {
                logger.warn("User with username {} not found.", username);
                return;
            }
            user.setSalt(generateSalt());
            user.setPassword(passwordEncoder.encode(newPassword + user.getSalt()));
            user.setPasswordChanged(true);
            userRepository.save(user);
            logger.info("Password for user {} has been changed successfully.", username);
        } catch (Exception e) {
            logger.error("An error occurred while changing password for user {}: {}", username, e.getMessage(), e);
        }
    }

    public List<User> getAll() {
        List<User> clientsAndEmployees = new ArrayList<>();
        try {
            logger.info("getAll method in UserService started.");
            for (User u : userRepository.findAll()) {
                for (Role r : u.getRoles()) {
                    if (r.getName().equals("ROLE_CLIENT") || r.getName().equals("ROLE_EMPLOYEE")) {
                        clientsAndEmployees.add(u);
                    }
                }
            }
            logger.info("getAll method in UserService ended.");
        } catch (Exception e) {
            logger.error("An error occurred while fetching users: {}", e.getMessage(), e);
        }
        return clientsAndEmployees;
    }


    @Override
    public User getById(Long id) {
        try {
            User user = userRepository.getById(id);
            if (user == null) {
                logger.warn("User with ID {} not found.", id);
            } else {
                logger.info("User with ID {} found.", id);
            }
            return user;
        } catch (Exception e) {
            logger.error("An error occurred while retrieving user with ID {}: {}", id, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<User> block(String email) {
        try {
            User user = userRepository.findByUsername(email);
            if (user != null) {
                user.setBlocked(true);
                userRepository.save(user);
                logger.info("User with email {} has been blocked.", email);
            } else {
                logger.warn("User with email {} not found.", email);
            }
        } catch (Exception e) {
            logger.error("An error occurred while blocking the user with email {}: {}", email, e.getMessage(), e);
        }
        return getAll();
    }

    @Override
    public List<User> unblock(String email) {
        try {
            User user = userRepository.findByUsername(email);
            if (user != null) {
                user.setBlocked(false);
                userRepository.save(user);
                logger.info("User with email {} has been unblocked.", email);
            } else {
                logger.warn("User with email {} not found.", email);
            }
        } catch (Exception e) {
            logger.error("An error occurred while unblocking the user with email {}: {}", email, e.getMessage(), e);
        }
        return getAll();
    }

    @Override
    public User resetToken(String email) {
        User user=userRepository.findByUsername(email);
        if(user!=null || !user.isBlocked()){
            String resetToken= UUID.randomUUID().toString();
            user.setResetToken(resetToken);
            userRepository.save(user);
            emailService.sendResetToken(user.getEmail(),resetToken);
        }
        return user;
    }

    @Override
    public User setNewPassword(String email, String password) {
        try {
            User user = userRepository.findByUsername(email);
            if (user != null) {
                String salt = generateSalt();
                user.setSalt(salt);
                user.setPassword(passwordEncoder.encode(password + salt));
                User updatedUser = userRepository.save(user);
                logger.info("Password for user with email {} has been updated.", email);
                return updatedUser;
            } else {
                logger.warn("User with email {} not found.", email);
                return null;
            }
        } catch (Exception e) {
            logger.error("An error occurred while setting new password for user with email {}: {}", email, e.getMessage(), e);
            return null;
        }
    }

    public void deleteUserData(Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found."));
            /*User newUser=new User();
            newUser.setId(user.getId());
            newUser.setSecret(user.getSecret());
            newUser.setSalt(user.getSalt());
            newUser.setPassword(user.getPassword());
            newUser.setPasswordChanged(user.isPasswordChanged());
            newUser.setEmail(user.getEmail());
            newUser.setUsername(user.getUsername());
            newUser.setEnabled(user.isEnabled());
            newUser.setBlocked(user.isBlocked());
            newUser.setKeyStorePassword(newUser.getKeyStorePassword());
            newUser.setPhone("");
            newUser.setRoles(user.getRoles());
            newUser.setAddress(null);
            newUser.setFirstName("");
            newUser.setLastName("");*/
            Client client = clientRepository.findByUsername(user.getUsername());
            /*Client newClient=new Client();
            newClient.setId(client.getId());
            newClient.setUser(client.getUser());
            newClient.setPib(client.getPib());
            newClient.setPhone("");
            newClient.setType(client.getType());
            newClient.setPackageType(client.getPackageType());*/
            //newClient.getUser().setKeyStorePassword(user.getKeyStorePassword());
            User newUser=new User();
            try {
                newUser=changeUserData(id);
                //userRepository.save(newUser);
            } catch (Exception e) {
                logger.error("Error updating user data for user id: " + id, e);
                throw new RuntimeException("Error updating user data", e);
            }

            if (client != null) {
                try {
                    client.setPhone("");
                    client.setUser(newUser);
                    clientRepository.save(client);
                   // clientRepository.save(newClient);
                    ArrayList<Advertisement> clientAdvertisements = advertisementService.findAllByClientId(user.getId());
                    advertisementService.deleteAll(clientAdvertisements);
                    ArrayList<AdvertisementRequest> clientRequests = advertisementRequestService.findAllByClientId(user.getId());
                    advertisementRequestService.deleteAll(clientRequests);
                    clientRepository.save(client);
                } catch (Exception e) {
                    logger.error("Error updating client data for client id: " + client.getId(), e);
                    throw new RuntimeException("Error updating client data", e);
                }
            }
        } catch (RuntimeException e) {
            logger.error("User with id " + id + " not found.", e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error occurred while deleting user data for user id: " + id, e);
            throw new RuntimeException("Unexpected error occurred", e);
        }
    }

    public void deleteAddress(Long id) {
        try{
            logger.info("Delete address by id started.");
            addressRepository.deleteById(id);
            logger.info("Delete address by id ended");
        }
        catch (Exception e) {
            logger.error("An error occurred while deleting address: {}", e.getMessage(), e);
        }
    }
    public User changeUserData(Long id) {
        User user=null;
        try{
            logger.info("changeUserData method started.");
            user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found."));
            Long addressId=user.getAddress().getAddressId();
            user.setAddress(null);
            user.setFirstName("");
            user.setLastName("");
            user.setPhone("");
            userRepository.save(user);
            deleteAddress(addressId);
            logger.info("changeUserData method ended.");
        }catch(Exception e){
            logger.error("An error occurred while changing data: {}",e.getMessage(),e);
        }
        return user;
    }
}
