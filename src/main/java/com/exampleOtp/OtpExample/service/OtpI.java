package com.exampleOtp.OtpExample.service;

import com.exampleOtp.OtpExample.entity.CustomerMobiles;
import com.exampleOtp.OtpExample.entity.MobileData;
import com.exampleOtp.OtpExample.entity.OtpEntity;
import com.exampleOtp.OtpExample.entity.OtpVerifyEntity;
import org.springframework.http.ResponseEntity;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface OtpI {

   boolean checkMobileNumber(CustomerMobiles mobileData);

    ResponseEntity  generateOtp(Long mobileNumber) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException;

    ResponseEntity registerMobile(CustomerMobiles customerMobiles) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException;

    ResponseEntity verifyOtp(OtpVerifyEntity otpVerifyEntity) throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException;

}
