/*
package com.cabbooking.service;

import com.cabbooking.dto.*;
import com.cabbooking.entity.*;
import com.cabbooking.mapper.AdminMapper;
import com.cabbooking.mapper.BookingMapper;
import com.cabbooking.repo.AdminRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements IAdminService {

    private final UserServiceImpl userService;
    private final DriverServiceImpl driverService;
    private final BookingServiceImpl bookingService;
    private final CityServiceImpl cityService;
    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AdminMapper adminMapper;
    private final BookingMapper bookingMapper;

    // ADMIN SIGNUP
    public AdminDTO signup(AdminSignupRequest request) {
        log.info("Admin signup attempt: {}", request.getUsername());
        if (adminRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        AdminEntity admin = adminMapper.toEntity(request);
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setStatus(Optional.ofNullable(admin.getStatus()).orElse(AdminStatus.ACTIVE));
        adminRepository.save(admin);
        log.info("Admin created successfully: {}", admin.getAdminId());
        return adminMapper.mapToDTO(admin);
    }

    // ADMIN SIGNIN
    public boolean signin(SigninRequest request) {
        log.info("Admin signin attempt: {}", request.getUsername());
        return adminRepository.findByUsername(request.getUsername())
                .map(admin -> passwordEncoder.matches(request.getPassword(), admin.getPassword()))
                .orElse(false);
    }

    @Override
    public Optional<AdminEntity> findByUsername(String username) {
        return  adminRepository.findByUsername(username);
    }

    // USERS
    public List<UserEntity> getAllUsers() {
        log.info("AdminService: Fetching all users via UserService");
        return userService.getAllUsers(); // reuse method from UserService
    }

    public List<UserDTO> getUsersByCity(Long cityId) {
        log.info("Fetching users for cityId: {}", cityId);

        // Fetch user entities
        List<UserEntity> users = (cityId != null)
                ? userService.getAllUsersByCityId(cityId)
                : userService.getAllUsers(); // raw entities, not DTO

        // Convert to DTOs
        return users.stream()
                .map(userService::mapToDTO)
                .collect(Collectors.toList());
    }



    public void deleteUser(Long userId) {
        log.info("Deleting user with ID: {}", userId);

        //  Validate user existence
        UserEntity user = userService.getUserById(userId);
        if (user == null) {
            log.warn("User with ID {} not found, nothing to delete.", userId);
            return; // or throw a custom exception if you prefer
        }

        // Handle related bookings if needed
        bookingService.findBookingsByUserId(userId)
                .forEach(b -> {
                    bookingService.deleteBooking(b.getBookingId());
                    log.info("Deleted booking {} for user {}", b.getBookingId(), userId);
                });
        //  Delete user
        userService.deleteUserById(userId);

        log.info("User {} deleted successfully", userId);
    }

    @Transactional
    public UserDTO updateUserPartial(Long userId, Map<String, Object> updates) {
        log.info("Updating user partially: {}", userId);

        UserEntity user = userService.getUserById(userId);

        if (updates.containsKey("displayName")) {
            user.setDisplayName((String) updates.get("displayName"));
        }

        if (updates.containsKey("mobileNumber")) {
            user.setMobileNumber((String) updates.get("mobileNumber"));
        }

        if (updates.containsKey("status")) {
            Object rawStatus = updates.get("status");

            if (rawStatus instanceof UserStatus) {
                // value is already an enum
                user.setStatus((UserStatus) rawStatus);

            } else if (rawStatus instanceof String) {
                // value is a String – normalise and convert
                String statusStr = ((String) rawStatus).trim().toUpperCase();
                try {
                    user.setStatus(UserStatus.valueOf(statusStr));
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid status value: " + statusStr);
                }

            } else if (rawStatus != null) {
                // some unexpected type
                throw new IllegalArgumentException(
                        "Unsupported type for status: " + rawStatus.getClass().getName()
                );
            }
        }

        // persist changes
        UserEntity saved = userService.saveUser(user);  // or userRepository.save(user)

        log.info("User {} updated", userId);
        return userService.mapToDTO(saved);
    }


    // DRIVERS
    */
/**
     * Fetch all drivers as DTOs via DriverService
     *//*

    public List<DriverDTO> getAllDrivers() {
        log.info("AdminService: Fetching all drivers via DriverService");

        return driverService.getAllDrivers() // fetch entities
                .stream()
                .map(driverService::mapToDTO) // convert to DTOs
                .collect(Collectors.toList());
    }

    public List<DriverDTO> getDriversByCity(Long cityId) {
        log.info("AdminService: Fetching drivers for cityId: {}", cityId);
        return driverService.getDriversByCityId(cityId);
    }



    public void deleteDriver(Long driverId) {
        log.info("Deleting driver with ID: {}", driverId);

        // 1️⃣ Fetch the driver
        DriverEntity driver = driverService.getDriverById(driverId);

        // 2️⃣ Fetch all bookings associated with this driver
        List<BookingEntity> bookings = bookingService.findBookingByDriverId(driverId);

        // 3️⃣ Unlink driver from each booking
        for (BookingEntity booking : bookings) {
            booking.setDriver(null);
        }
        // Save all bookings in batch to reduce DB calls
        bookingService.updateBooking(bookings); // implement updateBookings(List<BookingEntity>) in service

        // 4️⃣ Delete the driver
        driverService.deleteDriver(driver.getDriverId());

        log.info("Driver {} deleted successfully", driverId);
    }



    // Partial update
    public void updateDriverPartial(Long driverId, Map<String, Object> updates) {
        log.info("Updating driver partially: {}", driverId);

        DriverEntity driver = driverService.getDriverById(driverId);

        // Update basic fields
        if (updates.containsKey("displayName")) {
            driver.setDisplayName((String) updates.get("displayName"));
        }
        if (updates.containsKey("mobileNumber")) {
            driver.setMobileNumber((String) updates.get("mobileNumber"));
        }
        if (updates.containsKey("licenseNumber")) {
            driver.setLicenseNumber((String) updates.get("licenseNumber"));
        }
        if (updates.containsKey("cabType")) {
            driver.setCabType((String) updates.get("cabType"));
        }
        if (updates.containsKey("status") && updates.get("status") != null) {
            driver.setStatus((DriverStatus) updates.get("status"));
        }

        // Update city safely using DriverService reusable method
        if (updates.containsKey("cityId") && updates.get("cityId") != null) {
            Object cityObj = updates.get("cityId");
            Long cityId;

            if (cityObj instanceof String) {
                cityId = Long.parseLong((String) cityObj);
            } else if (cityObj instanceof Long) {
                cityId = (Long) cityObj;
            } else {
                throw new IllegalArgumentException("Invalid cityId type");
            }

            driver.setCity(driverService.getDriverCity(cityId));
        }

        // Persist changes
        driverService.updateDriver(driver);

        log.info("Driver {} updated successfully", driverId);
    }


    */
/**
     * Fetch bookings filtered by city name, status, and date range
     *//*

    @Override
    public List<BookingResponse> getBookings(Long cityId,
                                             String statusStr,
                                             LocalDate startDate,
                                             LocalDate endDate) {

        BookingStatus status = parseBookingStatus(statusStr);

        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = (endDate != null)
                ? endDate.plusDays(1).atStartOfDay().minusNanos(1)
                : null;

        List<BookingEntity> bookings =
                bookingService.findBookings(cityId, status, startDateTime, endDateTime);
        return bookingMapper.toResponseList(bookings);
    }


    */
/**
     * Utility: safely parse status string to BookingStatus
     *//*

    private BookingStatus parseBookingStatus(String statusStr) {
        if (statusStr == null || statusStr.isBlank()) return null;
        try {
            return BookingStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("Invalid booking status: {}", statusStr);
            return null;
        }
    }



    //  CITIES
    public List<CityEntity> getAllCities() {
        log.info("Fetching all cities via CityService");
        return cityService.getAllCities();
    }

    public CityEntity getCityByName(Long cityId) {
        log.info("Fetching city by ID: {}", cityId);
        return cityService.getCityById(cityId);
    }


    @Override
    public Long countUser(Long cityId) {
        if (cityId != null) {
            return userService.countUsersByCityId(cityId); // counts users for specific city
        }
        return userService.countAllUsers(); // counts all users
    }

    @Override
    public Long countDriver(Long cityId) {
        if (cityId != null) {
            return driverService.countDriversByCityId(cityId); // counts drivers for specific city
        }
        return driverService.countAllDrivers(); // counts all drivers
    }

    @Override
    public Long countRides(Long cityId) {
        if (cityId != null) {
            return bookingService.countRidesByCityId(cityId); // counts rides for specific city
        }
        return bookingService.countAllBookings(); // counts all rides
    }




}









































































*/
