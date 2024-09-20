package com.example.security.model;

import com.example.security.service.EncryptionUtil;

import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class AddressListener {
    @PrePersist
    @PreUpdate
    public void encrypt(Address u) throws Exception {
        // do encryption
        EncryptionUtil encryptionUtil = new EncryptionUtil();
        u =  encryptionUtil.encryptAddress(u.getKeyStorePassword(), u.getKeyStorePassword(), u);
    }
/*
    @PreUpdate
    public void encryptUpdate(Address u) throws Exception {
        // do encryption
        EncryptionUtil encryptionUtil = new EncryptionUtil();
        u =  encryptionUtil.encryptAddress(u.getKeyStorePassword(), u.getKeyStorePassword(), u);
    }
*/
    @PostLoad
    public void decrypt(Address u) throws Exception {
        // do decryption
        EncryptionUtil encryptionUtil = new EncryptionUtil();
        u = encryptionUtil.decryptAddress(u.getKeyStorePassword(), u.getKeyStorePassword(), u);
    }
}
