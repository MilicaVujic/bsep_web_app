package com.example.security.model;

import com.example.security.service.Encrypt;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@EntityListeners(AddressListener.class)
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "addressId", unique = true, nullable = false)
    private Long addressId;

    @Override
    public String toString() {
        return "Address [addressId=" + addressId + ", street=" + street + ", streetNumber=" + streetNumber + ", city="
                + city + ", country=" + country+"]";
    }

    //@Convert(converter = Encrypt.class)
    @Column(name = "street", nullable = false)
    private String street;

    //@Convert(converter = Encrypt.class)
    @Column(name = "streetNumber", nullable = false)
    private String streetNumber;

    ///@Convert(converter = Encrypt.class)
    @Column(name = "city", nullable = false)
    private String city;

    //@Convert(converter = Encrypt.class)
    @Column(name = "country", nullable = false)
    private String country;

    @Convert(converter = Encrypt.class)
    @Column(name="keyStorePassword")
    private String keyStorePassword;

    public Address() {
    }

    public Address(String street, String streetNumber, String city, String country) {
        super();
        this.street = street;
        this.streetNumber = streetNumber;
        this.city = city;
        this.country = country;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    public static String mapAddress(Address a) {
        StringBuilder address = new StringBuilder();
        address.append(a.getStreet());
        address.append(" ");
        address.append(a.getStreetNumber());
        address.append(" ");
        address.append(a.getCity());
        address.append(", ");
        address.append(a.getCountry());
        return address.toString();
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

}