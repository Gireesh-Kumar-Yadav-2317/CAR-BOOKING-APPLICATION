package com.cabbooking.mapper;

import com.cabbooking.dto.DriverDTO;
import com.cabbooking.dto.DriverSignupRequest;
import com.cabbooking.entity.CityEntity;
import com.cabbooking.entity.DriverEntity;
import com.cabbooking.entity.DriverStatus;
import org.springframework.stereotype.Component;

@Component
public class DriverMapper {
    private final CityMapper cityMapper;

    public DriverMapper(CityMapper cityMapper) {
        this.cityMapper = cityMapper;
    }

    /**
     * Convert Driver entity → DriverDTO
     * Uses CityMapper to map City → CityDTO
     */
    public  DriverDTO toDTO(DriverEntity driver) {
        if (driver == null) return null;

        return DriverDTO.builder()
                .driverId(driver.getDriverId())
                .displayName(driver.getDisplayName())
                .mobileNumber(driver.getMobileNumber())
                .username(driver.getUsername())
                .city(driver.getCity() != null ? cityMapper.toDTO(driver.getCity()) : null)
                .cabType(driver.getCabType())
                .cabNumber(driver.getCabNumber())
                .licenseNumber(driver.getLicenseNumber())
                .status(driver.getStatus())
                .build();
    }

    /**
     * Convert SignupRequest + City → Driver entity
     * Password encoding should be done in Service before calling this method
     */
    public DriverEntity toEntity(DriverSignupRequest request, CityEntity city) {
        return DriverEntity.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .displayName(request.getDisplayName())
                .mobileNumber(request.getMobileNumber())
                .cabType(request.getCabType())
                .cabNumber(request.getCabNumber())
                .licenseNumber(request.getLicenseNumber())
                .status(DriverStatus.valueOf(request.getStatus()))
                .city(city)
                .build();
    }

    /**
     * Update an existing Driver entity with a DTO
     */
    public void updateEntity(DriverEntity driver, DriverDTO dto, CityEntity city) {
        if (dto.getDisplayName() != null) driver.setDisplayName(dto.getDisplayName());
        if (dto.getMobileNumber() != null) driver.setMobileNumber(dto.getMobileNumber());
        if (dto.getCabType() != null) driver.setCabType(dto.getCabType());
        if (dto.getCabNumber() != null) driver.setCabNumber(dto.getCabNumber());
        if (dto.getLicenseNumber() != null) driver.setLicenseNumber(dto.getLicenseNumber());
        if (dto.getStatus() != null) driver.setStatus(dto.getStatus());
        if (city != null) driver.setCity(city);
    }
}
