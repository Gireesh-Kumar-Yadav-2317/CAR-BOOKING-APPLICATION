
package com.cabbooking.mapper;

import com.cabbooking.dto.*;
import com.cabbooking.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class BookingMapper {
    private  final UserMapper userMapper;
    private final DriverMapper driverMapper;
    private final CityRouteMapper cityRouteMapper;
    private final CityMapper cityMapper;

    public BookingMapper(UserMapper userMapper, DriverMapper driverMapper, CityRouteMapper cityRouteMapper, CityMapper cityMapper) {
        this.userMapper = userMapper;
        this.driverMapper = driverMapper;
        this.cityRouteMapper = cityRouteMapper;
        this.cityMapper = cityMapper;
    }

    // Convert BookingRequest + UserDTO + CityRouteRequest + calculatedFare → BookingEntity
    public BookingEntity toEntity(BookingRequest request, UserEntity user, CityRouteEntity route, double calculatedFare) {
        return BookingEntity.builder()
                .user(user)          // persisted user
                .city(route.getCity()) // persisted city
                .route(route)        // persisted route
                .fareAmount(calculatedFare)
                .status(BookingStatus.PENDING)
                .build();
    }


    public BookingResponse toResponse(BookingEntity booking) {
        if (booking == null) return null;

        BookingStatus status = BookingStatus.valueOf(booking.getStatus() != null ? booking.getStatus().name() : "UNKNOWN");

        return BookingResponse.builder()
                .bookingId(booking.getBookingId())
                .user(userMapper.toDTO(booking.getUser()))
                .driver(driverMapper.toDTO(booking.getDriver()))
                .city(cityMapper.toDTO(booking.getCity()))
                .route(cityRouteMapper.toDTO(booking.getRoute()))
                .fareAmount(booking.getFareAmount())
                .bookingStatus(status)    // you can keep this too
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .message(null)
                .build();
    }

    public List<BookingResponse> toResponseList(List<BookingEntity> bookings) {
        return bookings.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }



}
