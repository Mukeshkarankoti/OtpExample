package com.exampleOtp.OtpExample.service.Implementation;


import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.util.Base64;

@Service
public class AdvancedEncryptDecrypt {

    public String encryptOtp(String otp,SecretKey secretKey,Cipher cipher) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        byte[]otpBytes=otp.getBytes();
        cipher.init(Cipher.ENCRYPT_MODE,secretKey);
        byte[]encryptedBytes=cipher.doFinal(otpBytes);
        Base64.Encoder encoder=Base64.getEncoder();
        return  encoder.encodeToString(encryptedBytes);


    }

    public String decriptedotp(String encryptedOtp,SecretKey secretKey,Cipher cipher) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Base64.Decoder decoder=Base64.getDecoder();
       byte[]encryptedByteOtp =decoder.decode(encryptedOtp);
       cipher.init(Cipher.DECRYPT_MODE,secretKey);
       byte[]decryptedByteCode=cipher.doFinal(encryptedByteOtp);
       String decryptedCode=new String(decryptedByteCode);
       System.out.println("decryptedCode ::"+decryptedCode);
       return  decryptedCode;
    }
}
