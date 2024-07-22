package com.exampleOtp.OtpExample.entity;

import lombok.Data;

@Data
public class OtpVerifyEntity {
    private long mobileNumber;
    private String otp;
}
