package com.exampleOtp.OtpExample.Repository;

import com.exampleOtp.OtpExample.entity.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepo extends JpaRepository<OtpEntity, Integer> {

OtpEntity findByMobileNumber(Long number);

}
