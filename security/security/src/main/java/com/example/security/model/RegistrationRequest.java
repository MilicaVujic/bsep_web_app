package com.example.security.model;

import com.example.security.service.Encrypt;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Date;
import java.util.Random;

@Entity
@EntityListeners(RegistrationRequestListener.class)
public class RegistrationRequest {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name="password")
    private String password;

    public long getRejectingDate() {
        return rejectingDate;
    }

    public void setRejectingDate(long rejectingDate) {
        this.rejectingDate = rejectingDate;
    }
    @Nullable
    @Column(name="rejectingDate")
    private long rejectingDate;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name="email")
    private String email;
    @Column(name="name")
    private String name;
    @Column(name="surname")
    private String surname;

    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }

    @Column(name="phone")
    private String phone;

    @Column(name="pib")
    private String pib;

    @Column(name="type")
    private ClientType type;

    @Column(name="country")
    private String country;

    @Column(name="city")
    private String city;

    @Column(name="streer")
    private String street;

    @Column(name="streetNumber")
    private String streetNumber;

    @Column(name="requestStatus")
    private RequestStatus requestStatus;
    @Column(name="packageType")
    private PackageType packageType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPib() {
        return pib;
    }

    public void setPib(String pib) {
        this.pib = pib;
    }

    public ClientType getType() {
        return type;
    }

    public void setType(ClientType type) {
        this.type = type;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public RegistrationRequest(Long id, String username, String password, String name, String surname, String phone, String pib, ClientType type, String country, String city, String street, String streetNumber, RequestStatus requestStatus,PackageType packageType,String email,long rejectingDate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.pib = pib;
        this.type = type;
        this.country = country;
        this.city = city;
        this.street = street;
        this.streetNumber = streetNumber;
        this.requestStatus = requestStatus;
        this.packageType=packageType;
        this.email=email;
        this.rejectingDate=rejectingDate;
    }

    public RegistrationRequest(){
        this.requestStatus=RequestStatus.NEW;
    }

    @Convert(converter = Encrypt.class)
    @Column(name="keyStorePassword")
    private String keyStorePassword;

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    public String generateRandomString() {
        StringBuilder sb = new StringBuilder(3);
        Random random = new Random();
        for (int i = 0; i < 15; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

}
