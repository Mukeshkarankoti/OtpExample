package com.exampleOtp.OtpExample.Repository;

import com.exampleOtp.OtpExample.entity.CustomerMobiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends JpaRepository<CustomerMobiles,Integer> {
    CustomerMobiles findByMobileNumber(Long mobileNumber);
}
