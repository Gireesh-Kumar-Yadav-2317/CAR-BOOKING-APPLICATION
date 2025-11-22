package com.cabbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminSignupRequest {
    private String username;
    private String password;
    private String displayName;
    private String mobileNumber;
    private String status;
    private CityDTO city;
}
