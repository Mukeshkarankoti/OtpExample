package com.exampleOtp.OtpExample.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class MobileData {

    private long mobileNumber;

    private String name;

}
