package com.example.security.dto;

import com.example.security.model.ClientType;
import com.example.security.model.PackageType;
import com.example.security.model.RequestStatus;

public class RegistrationRequestDto {
    private String username;
    private String password;
    private String name;
    private String surname;
    private String phone;
    private String pib;
    private ClientType type;
    private String country;
    private String city;
    private String street;
    private String streetNumber;
    private RequestStatus status;
    private PackageType packageType;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

    public PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }

    public RegistrationRequestDto(String username, String password, String name, String surname, String phone, String pib, ClientType type, String country, String city, String street, String streetNumber, RequestStatus status, PackageType packageType, String email) {
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
        this.status = status;
        this.packageType=packageType;
        this.email=email;
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

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public RegistrationRequestDto(){}
}
