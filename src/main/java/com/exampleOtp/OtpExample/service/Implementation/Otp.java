package com.exampleOtp.OtpExample.service.Implementation;
import com.exampleOtp.OtpExample.Repository.CustomerRepo;
import com.exampleOtp.OtpExample.Repository.OtpRepo;
import com.exampleOtp.OtpExample.entity.CustomerMobiles;
import com.exampleOtp.OtpExample.entity.OtpEntity;
import com.exampleOtp.OtpExample.entity.OtpVerifyEntity;
import com.exampleOtp.OtpExample.service.OtpI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
@Service
public class Otp implements OtpI {
    @Autowired
    OtpRepo otpRepo;
    @Autowired
    CustomerRepo customerRepo;
    @Autowired
    AdvancedEncryptDecrypt advancedEncryptDecrypt;

    static  Cipher cipher;

    SecretKey secretKey;

    Otp() throws NoSuchAlgorithmException, NoSuchPaddingException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128); // block size is 128bits
       secretKey = keyGenerator.generateKey();
        cipher=Cipher.getInstance("AES");

    }

    @Override
    public boolean checkMobileNumber(CustomerMobiles mobileData) {
        Optional<CustomerMobiles> optionalMobileData = Optional.ofNullable(customerRepo.findByMobileNumber(mobileData.getMobileNumber()));
        if (optionalMobileData.isPresent()) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public ResponseEntity generateOtp(Long mobileNumber) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Optional<CustomerMobiles>customerMobiles= Optional.ofNullable(customerRepo.findByMobileNumber(mobileNumber));
        if(customerMobiles.isPresent()){
            Optional<OtpEntity>otpEntity= Optional.ofNullable(otpRepo.findByMobileNumber(mobileNumber));
            if(otpEntity.isPresent()){
                OtpEntity otpEntity1=otpEntity.get();
                if(otpEntity1.getCount()>=4){
                    if(otpEntity1.getExpiryDate().toLocalDate().equals(LocalDateTime.now().toLocalDate())){
                        return ResponseEntity.status(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED).body("Otp limit has exceeded for the day please try next day");
                    }
                    else{
                        String otp=String.valueOf(new Random().nextInt(900000)+100000);
                        String encryptedOtp= advancedEncryptDecrypt.encryptOtp(otp,secretKey,cipher);
                        System.out.println("encryptedCode"+encryptedOtp);
                        OtpEntity otpEntity3=new OtpEntity();
                        otpEntity3.setId(otpEntity1.getId());
                        otpEntity3.setCount(1);
                        otpEntity3.setMobileNumber(mobileNumber);
                        otpEntity3.setExpiryDate(LocalDateTime.now());
                        otpEntity3.setOtp(encryptedOtp);
                        otpRepo.save(otpEntity3);
                        return ResponseEntity.status(HttpStatus.OK).body("generated Otp");
                    }
                }
                else{
                  //otp Encryption
                    String otp= String.valueOf(new Random().nextInt(900000)+100000);
                    System.out.println("beforeEncryption"+otp);
                    String encryptedOtp=advancedEncryptDecrypt.encryptOtp(otp,secretKey,cipher);
                    System.out.println("encryptedCode"+encryptedOtp);
                    OtpEntity otpEntity3=new OtpEntity();
                    otpEntity3.setId(otpEntity1.getId());
                    otpEntity3.setCount(otpEntity1.getCount()+1);
                    otpEntity3.setMobileNumber(mobileNumber);
                    otpEntity3.setExpiryDate(LocalDateTime.now());
                    otpEntity3.setOtp(encryptedOtp);
                    otpRepo.save(otpEntity3);
                    return ResponseEntity.status(HttpStatus.OK).body("otp was generated");
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mobile number"+mobileNumber+" is registered but otp has not generated once");
        }
        else{
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("mobile number "+mobileNumber+" was not registered");
        }
    }
    @Override
    public ResponseEntity registerMobile(CustomerMobiles customerMobiles) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        customerRepo.save(customerMobiles);
        OtpEntity otpEntity=new OtpEntity();
        otpEntity.setMobileNumber(customerMobiles.getMobileNumber());
        String otp=String.valueOf(new Random().nextInt(900000)+100000);
        System.out.println("otp:"+otp);
        String encryptedOtp= advancedEncryptDecrypt.encryptOtp(otp,secretKey,cipher);
        otpEntity.setOtp(String.valueOf(encryptedOtp));
        otpEntity.setCount(1);
        otpEntity.setExpiryDate(LocalDateTime.now());
        otpRepo.save(otpEntity);
        return ResponseEntity.status(HttpStatus.OK).body("mobile number registered");
    }
    @Override
    public ResponseEntity verifyOtp(OtpVerifyEntity otpVerifyEntity) throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String receivedOtp=otpVerifyEntity.getOtp();
        long mobileNumber=otpVerifyEntity.getMobileNumber();
        OtpEntity otpEntity=otpRepo.findByMobileNumber(mobileNumber);
       String savedOtp =otpEntity.getOtp();
       String decriptedOtp=advancedEncryptDecrypt.decriptedotp(savedOtp,secretKey,cipher);
       if(receivedOtp.equals(decriptedOtp)){
           if(otpEntity.getExpiryDate().toLocalDate().equals(LocalDateTime.now().toLocalDate())){
               Duration duration=Duration.between(otpEntity.getExpiryDate(),LocalDateTime.now());
               long seconds=duration.getSeconds();
               if(seconds>=300){
                   return  ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("otp limit time exceeded");
               }
               else {
                   otpEntity.setCount(0);
                   otpRepo.save(otpEntity);
                   return ResponseEntity.status(HttpStatus.OK).body("otp verified");
               }
           }
           else{
               return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body("otp limit time exceeded");
           }
       }
       else{
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("wrong otp was entered");
       }
    }
}