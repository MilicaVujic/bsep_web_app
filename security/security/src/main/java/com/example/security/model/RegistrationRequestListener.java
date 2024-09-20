package com.example.security.model;

import com.example.security.service.EncryptionUtil;

import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class RegistrationRequestListener {
    @PrePersist
    @PreUpdate
    public void encrypt(RegistrationRequest u) throws Exception {
        // do encryption
        EncryptionUtil encryptionUtil = new EncryptionUtil();
        u =  encryptionUtil.encryptRequest(u.getKeyStorePassword(), u.getKeyStorePassword(), u);
    }


    @PostLoad
    public void decrypt(RegistrationRequest u) throws Exception {
        // do decryption
        EncryptionUtil encryptionUtil = new EncryptionUtil();
        u = encryptionUtil.decryptRequest(u.getKeyStorePassword(), u.getKeyStorePassword(), u);
    }
}
