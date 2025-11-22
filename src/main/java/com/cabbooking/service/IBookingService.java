package com.cabbooking.service;

import com.cabbooking.dto.BookingRequest;
import com.cabbooking.dto.BookingResponse;

import java.util.List;

public interface IBookingService {

    BookingResponse bookRide(BookingRequest request);

    BookingResponse acceptRide(Long bookingId, Long driverId);

    BookingResponse rejectRide(Long bookingId, Long driverId);

    BookingResponse startRide(Long bookingId);

    BookingResponse completeRide(Long bookingId);

    List<BookingResponse> getCompletedRides(Long driverId);

    List<BookingResponse> getPendingBookingsForDriver(Long driverId);

    BookingResponse getBookingById(Long bookingId);

    List<BookingResponse> getCurrentRide(Long driverId);
}
