package com.cabbooking.dto;

import com.cabbooking.service.CityServiceImpl1;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor@NoArgsConstructor
@Builder
public class DriverSignupRequest {
    private String username;
    private String password;
    private String displayName;
    private String mobileNumber;
    private String status;
    private CityDTO city;
    private String cabNumber;
    private String cabType;
    private String licenseNumber;
}
