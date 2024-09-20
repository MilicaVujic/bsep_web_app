package com.example.security.service;

import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import dev.samstevens.totp.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service

public class TfaAuthentication {
    Logger logger= LoggerFactory.getLogger(TfaAuthentication.class);


    public String generateNewSecret(){
        return new DefaultSecretGenerator().generate();
    }

    public String generateQRCodeUri(String secret){
        logger.info("generateQRCodeUri method started.");

        QrData qrData=new QrData.Builder()
                .label("Security app")
                .secret(secret)
                .issuer("security")
                .algorithm(HashingAlgorithm.SHA1)
                .digits(6)
                .period(30)
                .build();
        QrGenerator generator= new ZxingPngQrGenerator();
        byte[] imageData=new byte[0];
        try{
            imageData=generator.generate(qrData);
        } catch (QrGenerationException e) {
            throw new RuntimeException(e);
        }
        logger.info("generateQRCodeUri method finished.");
        return Utils.getDataUriForImage(imageData,generator.getImageMimeType());
    }
    public boolean isOtpValid(String secret, String code){
        logger.info("isOtpValid method started.");

        TimeProvider timeProvider=new SystemTimeProvider();
        CodeGenerator codeGenerator=new DefaultCodeGenerator();
        CodeVerifier verifier=new DefaultCodeVerifier(codeGenerator,timeProvider);
        return verifier.isValidCode(secret,code);
    }
    public boolean isOtpNotValid(String secret,String code){
        return !isOtpValid(secret,code);
    }
}
