package com.exampleOtp.OtpExample.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class OtpEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private int id;

   private long mobileNumber;

   private String otp;

   private int count;

   private LocalDateTime expiryDate;


}
