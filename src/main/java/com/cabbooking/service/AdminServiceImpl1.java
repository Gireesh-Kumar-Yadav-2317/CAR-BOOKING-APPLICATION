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

/**
 * Service layer for all Admin operations:
 * - Admin Authentication (signup / signin)
 * - User / Driver / Booking / City management
 * - Statistics aggregation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl1 implements IAdminService {

    private final UserServiceImpl userService;
    private final DriverServiceImpl driverService;
    private final BookingServiceImpl bookingService;
    private final CityServiceImpl1 cityService;
    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AdminMapper adminMapper;
    private final BookingMapper bookingMapper;

    /* ADMIN  AUTH */

    /** Create new admin account after checking username uniqueness */
    public AdminDTO signup(AdminSignupRequest request) {
        log.info("Admin signup attempt: {}", request.getUsername());
        validateNewAdminUsername(request.getUsername());
        AdminEntity admin = createAndSaveAdmin(request);
        log.info("Admin created successfully: {}", admin.getAdminId());
        return adminMapper.mapToDTO(admin);
    }

    /** Authenticate admin by verifying password */
    public boolean signin(SigninRequest request) {
        log.info("Admin signin attempt: {}", request.getUsername());
        return adminRepository.findByUsername(request.getUsername())
                .map(admin -> passwordEncoder.matches(request.getPassword(), admin.getPassword()))
                .orElse(false);
    }

    public Optional<AdminEntity> findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    /* USER MANAGEMENT */

    /** Get all users */
    public List<UserEntity> getAllUsers() {
        log.info("Fetching all users");
        return userService.getAllUsers();
    }

    /** Get users filtered by city */
    public List<UserDTO> getUsersByCity(Long cityId) {
        log.info("Fetching users for cityId: {}", cityId);
        List<UserEntity> users = (cityId != null)
                ? userService.getAllUsersByCityId(cityId)
                : userService.getAllUsers();
        return users.stream().map(userService::mapToDTO).collect(Collectors.toList());
    }

    /** Delete user and its related bookings */
    public void deleteUser(Long userId) {
        log.info("Deleting user with ID: {}", userId);
        UserEntity user = validateUserExists(userId);
        deleteUserBookings(userId);
        userService.deleteUserById(user.getUserId());
        log.info("User {} deleted successfully", userId);
    }

    /** Partial update of user profile */
    @Transactional
    public UserDTO updateUserPartial(Long userId, Map<String, Object> updates) {
        log.info("Updating user partially: {}", userId);
        UserEntity user = userService.getUserById(userId);
        applyUserUpdates(user, updates);
        UserEntity saved = userService.saveUser(user);
        log.info("User {} updated", userId);
        return userService.mapToDTO(saved);
    }

    /* DRIVER MANAGEMENT */

    public List<DriverDTO> getAllDrivers() {
        log.info("Fetching all drivers");
        return driverService.getAllDrivers().stream()
                .map(driverService::mapToDTO).collect(Collectors.toList());
    }

    public List<DriverDTO> getDriversByCity(Long cityId) {
        log.info("Fetching drivers for cityId: {}", cityId);
        return driverService.getDriversByCityId(cityId);
    }

    /** Delete driver and unlink from bookings */
    public void deleteDriver(Long driverId) {
        log.info("Deleting driver with ID: {}", driverId);
        DriverEntity driver = driverService.getDriverById(driverId);
        unlinkDriverFromBookings(driverId);
        driverService.deleteDriver(driver.getDriverId());
        log.info("Driver {} deleted successfully", driverId);
    }

    /** Partial update of driver profile */
    public void updateDriverPartial(Long driverId, Map<String, Object> updates) {
        log.info("Updating driver partially: {}", driverId);
        DriverEntity driver = driverService.getDriverById(driverId);
        applyDriverUpdates(driver, updates);
        driverService.updateDriver(driver);
        log.info("Driver {} updated successfully", driverId);
    }

    /* BOOKING REPORTS */

    /** Fetch bookings filtered by city, status, and date range */
    public List<BookingResponse> getBookings(Long cityId, String statusStr,
                                             LocalDate startDate, LocalDate endDate) {
        BookingStatus status = parseBookingStatus(statusStr);
        LocalDateTime start = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime end = endDate != null ? endDate.plusDays(1).atStartOfDay().minusNanos(1) : null;
        List<BookingEntity> bookings = bookingService.findBookings(cityId, status, start, end);
        return bookingMapper.toResponseList(bookings);
    }

    /* CITIES & STATS */

    public List<CityEntity> getAllCities() {
        log.info("Fetching all cities");
        return cityService.getAllCities();
    }

    public CityEntity getCityByName(Long cityId) {
        log.info("Fetching city by ID: {}", cityId);
        return cityService.getCityById(cityId);
    }

    public Long countUser(Long cityId) {
        return (cityId != null) ? userService.countUsersByCityId(cityId) : userService.countAllUsers();
    }

    public Long countDriver(Long cityId) {
        return (cityId != null) ? driverService.countDriversByCityId(cityId) : driverService.countAllDrivers();
    }

    public Long countRides(Long cityId) {
        return (cityId != null) ? bookingService.countRidesByCityId(cityId) : bookingService.countAllBookings();
    }

    /* ========================= PRIVATE HELPERS ========================= */

    /** Check for duplicate username before creating admin */
    private void validateNewAdminUsername(String username) {
        if (adminRepository.existsByUsername(username)) {
            log.warn("Username already exists: {}", username);
            throw new RuntimeException("Username already exists");
        }
    }

    /** Build and persist admin entity from signup request */
    private AdminEntity createAndSaveAdmin(AdminSignupRequest request) {
        AdminEntity admin = adminMapper.toEntity(request);
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setStatus(Optional.ofNullable(admin.getStatus()).orElse(AdminStatus.ACTIVE));
        return adminRepository.save(admin);
    }

    /** Ensure user exists before deletion */
    private UserEntity validateUserExists(Long userId) {
        UserEntity user = userService.getUserById(userId);
        if (user == null) {
            log.warn("User with ID {} not found", userId);
            throw new RuntimeException("User not found");
        }
        return user;
    }

    /** Delete all bookings for a specific user */
    private void deleteUserBookings(Long userId) {
        bookingService.findBookingsByUserId(userId).forEach(b -> {
            bookingService.deleteBooking(b.getBookingId());
            log.info("Deleted booking {} for user {}", b.getBookingId(), userId);
        });
    }

    /** Unlink driver from all their bookings and batch update */
    private void unlinkDriverFromBookings(Long driverId) {
        List<BookingEntity> bookings = bookingService.findBookingByDriverId(driverId);
        bookings.forEach(b -> b.setDriver(null));
        bookingService.updateBooking(bookings); // batch save
        log.info("Unlinked driver {} from {} bookings", driverId, bookings.size());
    }

    /** Apply updates from map to a User entity */
    private void applyUserUpdates(UserEntity user, Map<String, Object> updates) {
        if (updates.containsKey("displayName"))
            user.setDisplayName((String) updates.get("displayName"));
        if (updates.containsKey("mobileNumber"))
            user.setMobileNumber((String) updates.get("mobileNumber"));
        if (updates.containsKey("status"))
            user.setStatus(parseUserStatus(updates.get("status")));
    }

    /** Apply updates from map to a Driver entity */
    private void applyDriverUpdates(DriverEntity driver, Map<String, Object> updates) {
        if (updates.containsKey("displayName"))
            driver.setDisplayName((String) updates.get("displayName"));
        if (updates.containsKey("mobileNumber"))
            driver.setMobileNumber((String) updates.get("mobileNumber"));
        if (updates.containsKey("licenseNumber"))
            driver.setLicenseNumber((String) updates.get("licenseNumber"));
        if (updates.containsKey("cabType"))
            driver.setCabType((String) updates.get("cabType"));
        if (updates.containsKey("status") && updates.get("status") != null)
            driver.setStatus((DriverStatus) updates.get("status"));
        if (updates.containsKey("cityId") && updates.get("cityId") != null)
            driver.setCity(driverService.getDriverCity(parseCityId(updates.get("cityId"))));
    }

    /** Parse BookingStatus safely from string */
    private BookingStatus parseBookingStatus(String statusStr) {
        if (statusStr == null || statusStr.isBlank()) return null;
        try {
            return BookingStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("Invalid booking status: {}", statusStr);
            return null;
        }
    }

    /** Convert raw status object to UserStatus */
    private UserStatus parseUserStatus(Object rawStatus) {
        if (rawStatus instanceof UserStatus) return (UserStatus) rawStatus;
        if (rawStatus instanceof String) {
            try {
                return UserStatus.valueOf(((String) rawStatus).trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status value: " + rawStatus);
            }
        }
        if (rawStatus != null)
            throw new IllegalArgumentException("Unsupported type for status: " + rawStatus.getClass().getName());
        return null;
    }

    /** Convert cityId object safely to Long */
    private Long parseCityId(Object cityObj) {
        if (cityObj instanceof Long) return (Long) cityObj;
        if (cityObj instanceof String) return Long.parseLong((String) cityObj);
        throw new IllegalArgumentException("Invalid cityId type");
    }
}
