package com.cabbooking.service;

import com.cabbooking.dto.*;
import com.cabbooking.entity.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IAdminService {

    AdminDTO signup(AdminSignupRequest request);

    boolean signin(SigninRequest request);

    Optional<AdminEntity> findByUsername(String username);

    List<UserEntity> getAllUsers();

    List<DriverDTO> getAllDrivers();

    List<BookingResponse> getBookings(Long cityId,
                                      String statusStr,
                                      LocalDate startDate,
                                      LocalDate endDate);

    Long countUser(Long cityId);

    Long countDriver(Long cityId);

    Long countRides(Long cityId);

    void deleteUser(Long userId);

    UserDTO updateUserPartial(Long userId, Map<String, Object> updates);

    void deleteDriver(Long driverId);

    void updateDriverPartial(Long driverId, Map<String, Object> updates);
}

