package com.cabbooking.service;

import com.cabbooking.dto.DriverDTO;
import com.cabbooking.dto.DriverSignupRequest;
import com.cabbooking.dto.SigninRequest;
import com.cabbooking.entity.CityEntity;
import com.cabbooking.entity.DriverEntity;
import com.cabbooking.mapper.DriverMapper;
import com.cabbooking.repo.DriverRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service layer for handling Driver-related operations.
 * All methods have detailed logs for easy debugging.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements IDriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;
    private final CityServiceImpl1 cityService;
    private final BCryptPasswordEncoder passwordEncoder;

    // ================== Public Service Methods ==================

    /**
     * Registers a new driver after validating username and encoding password.
     */
    @Override
    public DriverDTO signup(DriverSignupRequest request) {
        log.info("Signup request for username: {}", request.getUsername());
        validateUniqueUsername(request.getUsername());

        CityEntity city = fetchCityById(request.getCity().getCityId());
        DriverEntity driver = createDriverEntity(request, city);

        DriverEntity savedDriver = driverRepository.save(driver);
        log.info("Driver created successfully: {} (ID: {})", savedDriver.getUsername(), savedDriver.getDriverId());
        return driverMapper.toDTO(savedDriver);
    }

    /**
     * Authenticates a driver by verifying username and password.
     */
    @Override
    public boolean signin(SigninRequest request) {
        log.info("Signin attempt for username: {}", request.getUsername());
        boolean success = driverRepository.findByUsername(request.getUsername())
                .map(driver -> passwordEncoder.matches(request.getPassword(), driver.getPassword()))
                .orElse(false);

        if (success) log.info("Signin successful for username: {}", request.getUsername());
        else log.warn("Signin failed for username: {}", request.getUsername());
        return success;
    }

    /** Fetch driver by username */
    public Optional<DriverEntity> findByUsername(String username) {
        log.info("Fetching driver by username: {}", username);
        return driverRepository.findByUsername(username);
    }

    /** Fetch driver by ID or throw exception */
    public DriverEntity getDriverById(Long driverId) {
        log.info("Fetching driver by ID: {}", driverId);
        return driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found with ID: " + driverId));
    }

    /** Map Driver entity to DTO */
    public DriverDTO mapToDTO(DriverEntity driver) {
        log.debug("Mapping Driver entity to DTO: ID {}", driver.getDriverId());
        return driverMapper.toDTO(driver);
    }

    /**
     * Update existing driver details like status, cab info etc.
     */
    @Override
    public void updateDriver(DriverEntity driver) {
        log.info("Updating driver with ID: {}", driver.getDriverId());
        if (driver == null || driver.getDriverId() == null) {
            throw new RuntimeException("Driver entity or ID cannot be null");
        }

        DriverEntity existing = getDriverById(driver.getDriverId());
        updateDriverFields(existing, driver);
        driverRepository.save(existing);
        log.info("Driver updated successfully: ID {}", driver.getDriverId());
    }

    /** Fetch all drivers (entities) */
    public List<DriverEntity> getAllDrivers() {
        log.info("Fetching all drivers");
        return driverRepository.findAll();
    }

    /** Fetch all drivers as DTOs by cityId (if cityId is null, fetch all) */
    public List<DriverDTO> getAllDriversByCityId(Long cityId) {
        log.info("Fetching all drivers for cityId: {}", cityId);
        List<DriverEntity> drivers = (cityId == null)
                ? driverRepository.findAll()
                : driverRepository.findDriversByCity_CityId(cityId);

        return drivers.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    /** Fetch drivers by city name */
    public List<DriverEntity> getAllDriversByCityName(String cityName) {
        log.info("Fetching drivers for city: {}", cityName);
        if (cityName == null || cityName.isBlank()) {
            throw new RuntimeException("City name cannot be null or empty");
        }
        CityEntity city = cityService.getCityByName(cityName);
        return driverRepository.findDriversByCity_CityId(city.getCityId());
    }

    /** Count drivers for a specific city */
    public Long countDriversByCityId(Long cityId) {
        return driverRepository.countDriversByCityId(cityId);
    }

    /** Count all drivers */
    public Long countAllDrivers() {
        return driverRepository.count();
    }

    /** Get Driver DTO by ID */
    public DriverDTO getDriverDTOById(Long driverId) {
        return mapToDTO(getDriverById(driverId));
    }

    /** Delete driver by ID */
    public void deleteDriver(Long driverId) {
        log.info("Deleting driver with ID: {}", driverId);
        driverRepository.deleteById(driverId);
    }

    /** Assign a city to a driver safely */
    public void setDriverCity(DriverEntity driver, Object cityObj) {
        if (cityObj != null) {
            Long cityId = (cityObj instanceof String)
                    ? Long.parseLong((String) cityObj)
                    : (Long) cityObj;
            driver.setCity(getDriverCity(cityId));
        }
    }

    /** Get CityEntity for given cityId */
    public CityEntity getDriverCity(Long cityId) {
        if (cityId == null) {
            throw new RuntimeException("City ID is required");
        }
        return fetchCityById(cityId);
    }

    // ================== Private Helper Methods ==================

    /** Ensure username is unique before signup */
    private void validateUniqueUsername(String username) {
        if (driverRepository.findByUsername(username).isPresent()) {
            log.error("Signup failed: username {} already exists", username);
            throw new RuntimeException("Username already exists");
        }
    }

    /** Fetch city entity by ID */
    private CityEntity fetchCityById(Long cityId) {
        log.info("Fetching city by ID: {}", cityId);
        return cityService.getCityById(cityId);
    }

    /** Create a DriverEntity from request and city */
    private DriverEntity createDriverEntity(DriverSignupRequest request, CityEntity city) {
        DriverEntity driver = driverMapper.toEntity(request, city);
        driver.setPassword(passwordEncoder.encode(driver.getPassword()));
        return driver;
    }

    /** Update modifiable fields of driver */
    private void updateDriverFields(DriverEntity existing, DriverEntity updated) {
        existing.setStatus(updated.getStatus());
        existing.setDisplayName(updated.getDisplayName());
        existing.setMobileNumber(updated.getMobileNumber());
        existing.setCabNumber(updated.getCabNumber());
        existing.setCabType(updated.getCabType());
        existing.setLicenseNumber(updated.getLicenseNumber());
    }

    public List<DriverDTO> getDriversByCityId(Long cityId) {
        log.info("Fetching drivers for cityId: {}", cityId);

        List<DriverEntity> drivers;

        if (cityId != null) {
            // Fetch drivers for the specific city
            drivers = driverRepository.findDriversByCity_CityId(cityId);
        } else {
            // If cityId is null, return all drivers
            drivers = driverRepository.findAll();
        }

        // Convert DriverEntity list → DriverDTO list
        return drivers.stream()
                .map(driverMapper::toDTO)
                .toList();
    }

}
