package com.example.security.model;

import com.example.security.service.EncryptionUtil;

import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class UserListener {
    //@PrePersist
    @PreUpdate
    public void encrypt(User u) throws Exception {
        // do encryption
        EncryptionUtil encryptionUtil = new EncryptionUtil();
        u =  encryptionUtil.encrypt(u.getKeyStorePassword(), u.getKeyStorePassword(), u);
    }


    @PostLoad
    public void decrypt(User u) throws Exception {
        // do decryption
        EncryptionUtil encryptionUtil = new EncryptionUtil();
        u = encryptionUtil.decrypt(u.getKeyStorePassword(), u.getKeyStorePassword(), u);
    }
}
