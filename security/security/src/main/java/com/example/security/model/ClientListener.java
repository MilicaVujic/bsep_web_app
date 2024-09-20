package com.example.security.model;

import com.example.security.service.EncryptionUtil;

import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class ClientListener {
    @PrePersist
    @PreUpdate
    public void encrypt(Client u) throws Exception {
        // do encryption
        EncryptionUtil encryptionUtil = new EncryptionUtil();
        u =  encryptionUtil.encryptClient(u.getUser().getKeyStorePassword(), u.getUser().getKeyStorePassword(), u);
        u.setUser(encryptionUtil.encrypt(u.getUser().getKeyStorePassword(), u.getUser().getKeyStorePassword(), u.getUser()));
    }


    @PostLoad
    public void decrypt(Client u) throws Exception {
        // do decryption
        EncryptionUtil encryptionUtil = new EncryptionUtil();
        u = encryptionUtil.decryptClient(u.getUser().getKeyStorePassword(), u.getUser().getKeyStorePassword(), u);
        u.setUser(encryptionUtil.decrypt(u.getUser().getKeyStorePassword(), u.getUser().getKeyStorePassword(), u.getUser()));

    }
}
