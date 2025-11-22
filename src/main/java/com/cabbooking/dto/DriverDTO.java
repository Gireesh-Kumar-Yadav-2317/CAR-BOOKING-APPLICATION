package com.cabbooking.dto;

import com.cabbooking.entity.DriverStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO used for creating or updating a driver.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverDTO {
    private Long driverId;
    private String displayName;
    private String mobileNumber;
    private String username;
    private DriverStatus status;
    private String cabType;
    private String cabNumber;
    private String licenseNumber;
    private CityDTO city; // Use CityDTO instead of City object or separate fields
}

