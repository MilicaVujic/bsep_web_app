package com.example.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;

@Component
public class Encrypt implements AttributeConverter<String,String> {
    @Autowired
    EncryptionUtil encryptionUtil;

    @Override
    public String convertToDatabaseColumn(String s) {
        return encryptionUtil.encryptSym(s, "1234567812345678");
    }

    @Override
    public String convertToEntityAttribute(String s) {
        return encryptionUtil.decryptSym(s, "1234567812345678");
    }
}