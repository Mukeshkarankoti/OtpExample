package com.exampleOtp.OtpExample.OtpController;
import com.exampleOtp.OtpExample.entity.CustomerMobiles;
import com.exampleOtp.OtpExample.entity.MobileData;
import com.exampleOtp.OtpExample.entity.OtpVerifyEntity;
import com.exampleOtp.OtpExample.service.OtpI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
public class OtpC {
    @Autowired
    OtpI otpI;
    @PostMapping("/generateOtp")
    public ResponseEntity generateOpt(@RequestBody MobileData mobileData) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return otpI.generateOtp(mobileData.getMobileNumber());
    }
    @PostMapping("/registerMobile")
    public ResponseEntity registerMobile(@RequestBody CustomerMobiles mobileData) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        if(otpI.checkMobileNumber(mobileData)){
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body("mobile number "+mobileData.getMobileNumber()+" is already registered");
        }
        else{
            return otpI.registerMobile(mobileData);
        }
    }
    @PostMapping("/verifyOtp")
    public ResponseEntity verifyOtp(@RequestBody OtpVerifyEntity otpVerifyEntity) throws IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

      return otpI.verifyOtp(otpVerifyEntity);

    }
}