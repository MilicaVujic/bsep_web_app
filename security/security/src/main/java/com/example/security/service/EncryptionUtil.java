package com.example.security.service;

import com.example.security.model.Address;
import com.example.security.model.Client;
import com.example.security.model.RegistrationRequest;
import com.example.security.model.User;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;

import java.security.cert.CertificateException;

@Component
public class EncryptionUtil {
    private final String key = "password";
    private final String initVector = "1234567812345678";
    private final String algo = "AES/CBC/PKCS5PADDING";
    private KeyStore keyStore;
    private String keyStoreName = "sensitiveData_ks.p12";
    private String requestKeyStoreName = "request_ks.p12";
    private String clientKeyStoreName = "client_ks.p12";
    private String addressKeyStoreName = "address_ks.p12";

    public String encryptSym(String value, String encKey) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(encKey.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance(algo);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String decryptSym(String encrypted, String encKey) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(encKey.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance(algo);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public User encrypt(String alias, String password, User u) throws Exception {
        keyStore = KeyStore.getInstance("JCEKS");
        //saveKeyStore(keyStoreName, key.toCharArray());
        loadKeyStore(keyStoreName, key.toCharArray());

        String encKey = write(alias, password.toCharArray());
        saveKeyStore(keyStoreName, key.toCharArray());

        //u.getAddress().setCity(Base64.encodeBase64String(cipher.doFinal(u.getAddress().getCity().getBytes(StandardCharsets.UTF_8))));
        //u.getAddress().setCity(encryptSym(u.getAddress().getCity(), encKey));
        //u.getAddress().setCountry(encryptSym(u.getAddress().getCountry(), encKey));
        //u.getAddress().setStreetNumber(encryptSym(u.getAddress().getStreetNumber(), encKey));
        //u.getAddress().setStreet(encryptSym(u.getAddress().getStreet(), encKey));
        //u.setUsername(encryptAndEncode(cipher, u.getUsername()));
        u.setFirstName(encryptSym(u.getFirstName(), encKey));
        u.setLastName(encryptSym( u.getLastName(), encKey));
        //u.setEmail(encryptSym(u.getEmail(), encKey));
        u.setPhone(encryptSym(u.getPhone(), encKey));
        return u;
    }

    public User decrypt(String alias, String password, User u) throws Exception {
        keyStore = KeyStore.getInstance("JCEKS");
        loadKeyStore(keyStoreName, key.toCharArray());

        String encKey = convertSecretKeyToString(read(alias, password));
        //u.getAddress().setCity(new String(cipher.doFinal(Base64.decodeBase64(u.getAddress().getCity())), StandardCharsets.UTF_8));
        //u.getAddress().setCity(decryptSym(u.getAddress().getCity(), encKey));
        //u.getAddress().setCountry(decryptSym(u.getAddress().getCountry(), encKey));
        //u.getAddress().setStreetNumber(decryptSym(u.getAddress().getStreetNumber(), encKey));
        //u.getAddress().setStreet(decryptSym(u.getAddress().getStreet(), encKey));
        //u.setUsername(decryptAndDecode(cipher, u.getUsername()));
        u.setFirstName(decryptSym(u.getFirstName(), encKey));
        u.setLastName(decryptSym(u.getLastName(), encKey));
        //u.setEmail(decryptSym(u.getEmail(), encKey));
        u.setPhone(decryptSym(u.getPhone(), encKey));

        return u;
    }

    public Address encryptAddress(String alias, String password, Address u) throws Exception {
        keyStore = KeyStore.getInstance("JCEKS");
        //saveKeyStore(keyStoreName, key.toCharArray());
        loadKeyStore(addressKeyStoreName, key.toCharArray());

        String encKey = write(alias, password.toCharArray());
        saveKeyStore(addressKeyStoreName, key.toCharArray());

        //u.getAddress().setCity(Base64.encodeBase64String(cipher.doFinal(u.getAddress().getCity().getBytes(StandardCharsets.UTF_8))));
        u.setCity(encryptSym(u.getCity(), encKey));
        u.setCountry(encryptSym(u.getCountry(), encKey));
        u.setStreetNumber(encryptSym(u.getStreetNumber(), encKey));
        u.setStreet(encryptSym(u.getStreet(), encKey));

        return u;
    }

    public Address decryptAddress(String alias, String password, Address u) throws Exception {
        keyStore = KeyStore.getInstance("JCEKS");
        loadKeyStore(addressKeyStoreName, key.toCharArray());

        String encKey = convertSecretKeyToString(read(alias, password));
        //u.getAddress().setCity(new String(cipher.doFinal(Base64.decodeBase64(u.getAddress().getCity())), StandardCharsets.UTF_8));
        u.setCity(decryptSym(u.getCity(), encKey));
        u.setCountry(decryptSym(u.getCountry(), encKey));
        u.setStreetNumber(decryptSym(u.getStreetNumber(), encKey));
        u.setStreet(decryptSym(u.getStreet(), encKey));

        return u;
    }

    public void loadKeyStore(String fileName, char[] password) {
        try {
            if(fileName != null) {
                keyStore.load(new FileInputStream(fileName), password);
            } else {
                //Ako je cilj kreirati novi KeyStore poziva se i dalje load, pri cemu je prvi parametar null
                keyStore.load(null, password);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RegistrationRequest encryptRequest(String alias, String password, RegistrationRequest u) throws Exception {
        keyStore = KeyStore.getInstance("JCEKS");
        //saveKeyStore(keyStoreName, key.toCharArray());
        loadKeyStore(requestKeyStoreName, key.toCharArray());

        String encKey = write(alias, password.toCharArray());
        saveKeyStore(requestKeyStoreName, key.toCharArray());

        u.setUsername(encryptSym(u.getUsername(), encKey));
        u.setPassword(encryptSym( u.getPassword(), encKey));
        u.setEmail(encryptSym(u.getEmail(), encKey));
        u.setName(encryptSym(u.getName(), encKey));
        u.setSurname(encryptSym(u.getSurname(), encKey));
        u.setEmail(encryptSym(u.getEmail(), encKey));
        u.setPib(encryptSym(u.getPib(), encKey));
        u.setCountry(encryptSym(u.getCountry(), encKey));
        u.setCity(encryptSym(u.getCity(), encKey));
        u.setStreet(encryptSym(u.getStreet(), encKey));
        u.setStreetNumber(encryptSym(u.getStreetNumber(), encKey));

        u.setPhone(encryptSym(u.getPhone(), encKey));
        return u;
    }

    public RegistrationRequest decryptRequest(String alias, String password, RegistrationRequest u) throws Exception {
        keyStore = KeyStore.getInstance("JCEKS");
        loadKeyStore(requestKeyStoreName, key.toCharArray());

        String encKey = convertSecretKeyToString(read(alias, password));

        u.setUsername(decryptSym(u.getUsername(), encKey));
        u.setPassword(decryptSym( u.getPassword(), encKey));
        u.setEmail(decryptSym(u.getEmail(), encKey));
        u.setName(decryptSym(u.getName(), encKey));
        u.setSurname(decryptSym(u.getSurname(), encKey));
        u.setPhone(decryptSym(u.getPhone(), encKey));
        u.setPib(decryptSym(u.getPib(), encKey));
        u.setCountry(decryptSym(u.getCountry(), encKey));
        u.setCity(decryptSym(u.getCity(), encKey));
        u.setStreet(decryptSym(u.getStreet(), encKey));
        u.setStreetNumber(decryptSym(u.getStreetNumber(), encKey));
        u.setEmail(decryptSym(u.getEmail(), encKey));

        return u;
    }

    public Client encryptClient(String alias, String password, Client u) throws Exception {
        keyStore = KeyStore.getInstance("JCEKS");
        //saveKeyStore(keyStoreName, key.toCharArray());
        loadKeyStore(clientKeyStoreName, key.toCharArray());

        String encKey = write(alias, password.toCharArray());
        saveKeyStore(clientKeyStoreName, key.toCharArray());

        u.setPhone(encryptSym(u.getPhone(), encKey));
        u.setPib(encryptSym( u.getPib(), encKey));

        return u;
    }

    public Client decryptClient(String alias, String password, Client u) throws Exception {
        keyStore = KeyStore.getInstance("JCEKS");
        loadKeyStore(clientKeyStoreName, key.toCharArray());

        String encKey = convertSecretKeyToString(read(alias, password));

        u.setPhone(decryptSym(u.getPhone(), encKey));
        u.setPib(decryptSym(u.getPib(), encKey));

        return u;
    }
    public String write(String alias, char[] password) {
        try {
            KeyStore.SecretKeyEntry sc = new KeyStore.SecretKeyEntry(generateKey(128));
            keyStore.setEntry(alias, sc, new KeyStore.PasswordProtection(password));
            return convertSecretKeyToString(sc.getSecretKey());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return "";
    }
    public void saveKeyStore(String fileName, char[] password) {
        try {
            keyStore.store(new FileOutputStream(fileName), password);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SecretKey generateKey(int n) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(n);
        SecretKey originalKey = keyGenerator.generateKey();
        return originalKey;
    }

    public SecretKey read(String alias, String pass) {
        try {
            return (SecretKey) keyStore.getKey(alias, pass.toCharArray());

        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (UnrecoverableEntryException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static String convertSecretKeyToString(SecretKey secretKey) throws NoSuchAlgorithmException {
        byte[] rawData = secretKey.getEncoded();
        String encodedKey = Base64.encodeBase64String(rawData);
        return encodedKey;
    }
}