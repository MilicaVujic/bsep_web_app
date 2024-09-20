package com.example.security.service;

import com.example.security.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class HMACUtils {

    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final String SECRET_KEY = "tajni_kljuc";



    public static String generateHMACSignature(String data) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), HMAC_ALGORITHM);
            mac.init(secretKey);
            byte[] hmacData = mac.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(hmacData);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String generateActivationLink(String username) {
        TokenUtils tokenUtils = new TokenUtils();
        String linkToken = tokenUtils.generatePasswordlesToken(username);
        String hmacSignature = HMACUtils.generateHMACSignature(linkToken);
        return "https://localhost:8443/api/user/activate?token=" + linkToken + "&signature=" + hmacSignature;
    }

    public static String generateLogingLink(String username) {
        TokenUtils tokenUtils=new TokenUtils();
        String linkToken= tokenUtils.generatePasswordlesToken(username);
        String hmacSignature = HMACUtils.generateHMACSignature(linkToken);
        return "https://localhost:4200/authorizing?token=" + linkToken+"&signature="+hmacSignature;

    }

    public static String generateResetTokenLink(String token,String email) {
        return "https://localhost:4200/reset-password?token=" + token+"&email="+email;
    }

}
