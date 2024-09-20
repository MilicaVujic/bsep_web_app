package com.example.security.controller;

import com.example.security.dto.NotificationMessageDto;
import com.example.security.model.*;
import com.example.security.service.*;

import javax.websocket.server.PathParam;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IClientService clientService;
    @Autowired
    private IActivationLinkService activationLinkService;
    @Autowired
    private TfaAuthentication tfaAuthentication;
    @Autowired
    private PasswordEncoder passwordEncoder;
    Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    RateLimiterService rateLimiterService;

    @GetMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestParam("token") String token,
                                                  @RequestParam("signature") String hmacSignature) {
//        boolean isSignatureValid = HMACUtils.generateHMACSignature(token).equals(hmacSignature);
        logger.info("activateAccount method in UserController started.");
        Jws<Claims> claims = Jwts.parser()
                .setSigningKey("SECRET")
                .parseClaimsJws(token);
        String username = claims.getBody().getSubject();
        long timestamp = claims.getBody().get("timestamp", Long.class);
        long currentTime = System.currentTimeMillis();
        System.out.println(timestamp);
        System.out.println(currentTime);
        boolean isTokenValid = ((currentTime - timestamp) < 10 * 60 * 1000);//10 min
        boolean isTokenUsed = this.activationLinkService.isLinkAlreadyUsed(token);
        System.out.println("TTTTTTT"+isTokenValid+isTokenUsed);

        if (isTokenValid && !isTokenUsed) {
            userService.enable(username);
            activationLinkService.create(username, token);
            return ResponseEntity.ok("Vaš nalog je uspešno aktiviran!");
        } else if (isTokenUsed) {
            return ResponseEntity.badRequest().body("Već iskorišćen aktivacioni link.");
        } else {
            return ResponseEntity.badRequest().body("Neispravan ili istekao aktivacioni link.");
        }
    }
    @PreAuthorize("@permissionService.hasPermission('EMPLOYEE_PROFILE')")
    @GetMapping("/employeeProfile/{email}")
    public User getEmployee(@PathVariable("email") String email) {
        logger.info("getEmployee method in UserController started.");
        return userService.findByEmail(email);
    }
    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE','ROLE_ADMIN','ROLE_CLIENT')")
    @PutMapping("/update/{hashPassword}/{role}/{oldPassword}")
    public User update(@RequestBody User user, @PathVariable("hashPassword") Boolean hashPassword, @PathVariable("role") String role, @PathVariable("oldPassword") String oldPassword) throws Exception {
        logger.info("update method in UserController started.");
        if(user.getEmail()!=null) {
            boolean isEmailUnique = true;
            User storedUserWithSameEmail = userService.findByEmail(user.getEmail());

            if(storedUserWithSameEmail!=null && storedUserWithSameEmail.getId()!=user.getId())
                isEmailUnique = false;

            user.setSecret(storedUserWithSameEmail.getSecret());
            if(isEmailUnique) {
                String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,}$";
                boolean isPasswordValid = true;
                if (hashPassword)
                {
                    isPasswordValid = user.getPassword().matches(regex);
                    if(!passwordEncoder.matches((oldPassword + storedUserWithSameEmail.getSalt()), storedUserWithSameEmail.getPassword())){
                        return null;
                    }

                }
                boolean isInvalidFields;

                if(role.equals("ROLE_CLIENT")) {
                    Client storedClient = clientService.getByUserId(user.getId());
                    if(storedClient.getType().toString().equals("FIZICKO"))
                        isInvalidFields = user.getFirstName().isEmpty() || user.getLastName().isEmpty() || user.getPhone().isEmpty() || user.getAddress().getCountry().isEmpty()
                                || user.getAddress().getCity().isEmpty() || user.getAddress().getStreet().isEmpty() ||
                                user.getAddress().getStreetNumber().isEmpty() || !isPasswordValid;
                    else
                        isInvalidFields = user.getPhone().isEmpty() || user.getAddress().getCountry().isEmpty()
                                || user.getAddress().getCity().isEmpty() || user.getAddress().getStreet().isEmpty() ||
                                user.getAddress().getStreetNumber().isEmpty() || !isPasswordValid;

                }
                else
                    isInvalidFields = user.getFirstName().isEmpty() || user.getLastName().isEmpty() || user.getPhone().isEmpty() || user.getAddress().getCountry().isEmpty()
                        || user.getAddress().getCity().isEmpty() || user.getAddress().getStreet().isEmpty() ||
                        user.getAddress().getStreetNumber().isEmpty() || !isPasswordValid;
                if (isInvalidFields)
                    return null;
                User u = userService.save(user, hashPassword, role);
                //EncryptionUtil util = new EncryptionUtil();
                //u = util.encrypt(u.getKeyStorePassword(), u.getKeyStorePassword(), u);
                return  u;
            }
            else
                return null;
        } else
            return null;

    }
    @PreAuthorize("@permissionService.hasPermission('ADMIN_PROFILE')")
    @GetMapping("/adminProfile/{email}")
    public User getAdmin(@PathVariable("email") String email) {
        logger.info("getAdmin method in UserController started.");
        return userService.findByEmail(email);
    }
    @PreAuthorize("@permissionService.hasPermission('CLIENT_PROFILE')")
    @GetMapping("/clientProfile/{email}")
    public User getClientProfile(@PathVariable("email") String email) {
        logger.info("getClientProfile method in UserController started.");
        return userService.findByEmail(email);
    }
    @PreAuthorize("@permissionService.hasPermission('CLIENT_PROFILE')")
    @GetMapping("/client/{email}")
    public Client getClient(@PathVariable("email") String email) {
        logger.info("getClient method in UserController started.");
        return clientService.findByEmail(email);
    }

    @PreAuthorize("@permissionService.hasPermission('REG_EMPLOYEE_ADMIN')")
    @PostMapping("/creatingUser/{role}")
    public User createUser(@RequestBody User user, @PathVariable String role) {
        logger.info("createUser method in UserController started.");
        if(user.getEmail()!=null && userService.findByEmail(user.getEmail())==null) {
            String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,}$";
            boolean isPasswordValid = user.getPassword().matches(regex);

            boolean isInvalidFields=user.getFirstName().isEmpty()  || user.getLastName().isEmpty() || user.getPhone().isEmpty() || user.getAddress().getCountry().isEmpty()
                    || user.getAddress().getCity().isEmpty() || user.getAddress().getStreet().isEmpty() ||
                    user.getAddress().getStreetNumber().isEmpty() || !isPasswordValid;
            if(isInvalidFields)
                return  null;
            user.setSecret(tfaAuthentication.generateNewSecret());
            user.setBlocked(false);
            user.setKeyStorePassword(user.generateRandomString());
            user.getAddress().setKeyStorePassword(user.getKeyStorePassword());
            return userService.saveUser(user, role);
        } else
            return null;

    }
    @PreAuthorize("@permissionService.hasPermission('PEOPLE_VIEW')")
    @GetMapping("/employees")
    public ArrayList<User> getEmployees() {
        logger.info("getEmployees method in UserController started.");
        return userService.getEmployees();
    }
    @PreAuthorize("@permissionService.hasPermission('PEOPLE_VIEW')")
    @GetMapping("/clients")
    public ArrayList<User> getClients() {
        logger.info("getClients method in UserController started.");
        return userService.getClients();
    }

    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePass credentials) {
        logger.info("changePassword method in UserController started.");
        User user = userService.findByEmail(credentials.getEmail());
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,}$";
        boolean isPasswordValid = credentials.getPassword().matches(regex);

        if(!isPasswordValid)
            return new ResponseEntity<RegistrationRequest>(HttpStatus.BAD_REQUEST);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        if (!user.isPasswordChanged()) {
            userService.changePassword(credentials.getEmail(), credentials.getPassword());
            return ResponseEntity.ok("Password changed successfully.");
        } else {
            return ResponseEntity.badRequest().body("Password already changed.");
        }
    }
    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE','ROLE_CLIENT','ROLE_ADMIN')")
    @GetMapping("/getAll/{email}")
    public User getUser(@PathVariable("email") String email) {
        logger.info("getUser method in UserController started.");
        return userService.findByEmail(email);
    }
    @GetMapping("/firstLogin/{email}")
    public ResponseEntity<?> authenticateUser(@PathVariable("email") String email) {
        logger.info("authenticateUser method in UserController started.");
        User user=userService.findByEmail(email);
        if (user.isPasswordChanged()) {
            Map<String, Object> response = new HashMap<>();
            response.put("isPasswordChanged", true);
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    @PreAuthorize("@permissionService.hasPermission('BLOCK')")
    @GetMapping("/block/{email}")
    public ResponseEntity<List<User>> blockUser(@PathVariable("email") String email){
        logger.info("blockUser method in UserController started.");
        return ResponseEntity.ok(userService.block(email));
    }
    @PreAuthorize("@permissionService.hasPermission('ALL_USERS_BLOCKING')")
    @GetMapping("/all")
    public ResponseEntity<List<User>> getClientsAndEmployees(){
        logger.info("getClientsAndEmployees method in UserController started.");
        return ResponseEntity.ok(userService.getAll());
    }
    @PreAuthorize("@permissionService.hasPermission('UNBLOCK')")
    @GetMapping("/unblock/{email}")
    public ResponseEntity<List<User>> unblockUser(@PathVariable("email") String email){
        logger.info("unblockUser method in UserController started.");
        return ResponseEntity.ok(userService.unblock(email));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("@permissionService.hasPermission('CLIENT_PROFILE')")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        logger.info("deleteUser method in UserController started.");
        userService.deleteUserData(id);
        logger.info("deleteUser method in UserController completed.");
        return ResponseEntity.ok("Obrisan.");
    }

    @PostMapping("/registerAdmin")
    public User registerAdmin(@RequestBody User user) {
        logger.info("regAdmin method in UserController started.");
        if(user.getEmail()!=null && userService.findByEmail(user.getEmail())==null) {
            String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,}$";
            boolean isPasswordValid = user.getPassword().matches(regex);

            boolean isInvalidFields=user.getFirstName().isEmpty()  || user.getLastName().isEmpty() || user.getPhone().isEmpty() || user.getAddress().getCountry().isEmpty()
                    || user.getAddress().getCity().isEmpty() || user.getAddress().getStreet().isEmpty() ||
                    user.getAddress().getStreetNumber().isEmpty() || !isPasswordValid;
            if(isInvalidFields)
                return  null;
            user.setSecret(tfaAuthentication.generateNewSecret());
            user.setBlocked(false);
            user.setKeyStorePassword(user.generateRandomString());
            user.getAddress().setKeyStorePassword(user.getKeyStorePassword());
            return userService.saveUser(user, "ADMIN");
        } else
            return null;

    }

    /*@MessageMapping("/private")
    public void sendToSpecificUser(@Payload Message message) {
        simpMessagingTemplate.convertAndSendToUser(message.getTo(), "/specific", message);
    }*/

    @PreAuthorize("@permissionService.hasPermission('ADMIN_PROFILE')")
    @GetMapping("/criticalEvents")
    public ResponseEntity<NotificationMessageDto> criticalEvents(){
        logger.info("criticalEvents method in UserController started.");
        return ResponseEntity.ok(rateLimiterService.checkCriticalEventFailedLoginFromFront());
    }
}
