/*
package com.cabbooking.service;

import com.cabbooking.dto.*;
import com.cabbooking.entity.*;
import com.cabbooking.mapper.BookingMapper;
import com.cabbooking.repo.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements IBookingService {

    private final BookingRepository bookingRepository;
    private final UserServiceImpl userService;
    private final DriverServiceImpl driverService;
    private final CityRouteServiceImpl cityRouteService;
    private final FareEstServiceImpl fareEstService;
    private final BookingMapper bookingMapper;

    //  BOOK A RIDE
    @Override
    public BookingResponse bookRide(BookingRequest request) {
        log.info("Booking ride: userId={}, pickup={}, drop={}",
                request.getUserId(), request.getPickupLocation(), request.getDropLocation());

        // Validate and fetch required entities
        CityRouteEntity route = findRoute(request);
        UserEntity user = findUser(request.getUserId());
        FareEstResponse fare = findFare(request.getCityId());

        //  Calculate fare
        double calculatedFare = fareEstService.calculateFare(route, fare);

        // Map & save booking
        BookingEntity savedBooking = saveBooking(request, user, route, calculatedFare);
        log.info("Booking created successfully with ID: {}", savedBooking.getBookingId());

        //  Convert to response DTO
        return bookingMapper.toResponse(savedBooking);
    }

    // ACCEPT RIDE
    @Override
    public BookingResponse acceptRide(Long bookingId, Long driverId) {
        BookingEntity booking = fetchBooking(bookingId);
        DriverEntity driver = driverService.getDriverById(driverId);

        booking.setDriver(driver);
        booking.setStatus(BookingStatus.ACCEPTED);

        BookingEntity savedBooking = bookingRepository.save(booking);
        log.info("Booking {} accepted by driver {}", bookingId, driverId);
        return bookingMapper.toResponse(savedBooking);
    }

    //  REJECT RIDE
    @Override
    public BookingResponse rejectRide(Long bookingId, Long driverId) {
        BookingEntity booking = fetchBooking(bookingId);

        booking.setDriver(null);
        booking.setStatus(BookingStatus.REJECTED);

        BookingEntity savedBooking = bookingRepository.save(booking);
        log.info("Booking {} rejected by driver {}", bookingId, driverId);
        return bookingMapper.toResponse(savedBooking);
    }

    //  START RIDE
    @Override
    public BookingResponse startRide(Long bookingId) {
        BookingEntity booking = fetchBooking(bookingId);

        booking.setStatus(BookingStatus.ONGOING);
        booking.setStartTime(LocalDateTime.now());

        BookingEntity savedBooking = bookingRepository.save(booking);
        log.info("Ride started for booking {}", bookingId);
        return bookingMapper.toResponse(savedBooking);
    }


    //COMPLETE RIDE
    @Override
    public BookingResponse completeRide(Long bookingId) {
        BookingEntity booking = fetchBooking(bookingId);

        booking.setStatus(BookingStatus.COMPLETED);
        booking.setEndTime(LocalDateTime.now());

        DriverEntity driver = booking.getDriver();
        if (driver != null) {
            driver.setStatus(DriverStatus.ACTIVE);
            driverService.updateDriver(driver);
        }

        BookingEntity savedBooking = bookingRepository.save(booking);
        log.info("Ride completed for booking {}", bookingId);
        return bookingMapper.toResponse(savedBooking);
    }

    //GET BOOKING BY ID
    @Override
    public BookingResponse getBookingById(Long bookingId) {
        BookingEntity booking = fetchBooking(bookingId);
        return bookingMapper.toResponse(booking);
    }

    //  CURRENT RIDES
    @Override
    public List<BookingResponse> getCurrentRide(Long driverId) {
        return bookingRepository.findByDriverIdAndStatusIn(driverId,
                        Arrays.asList(BookingStatus.ACCEPTED, BookingStatus.ONGOING))
                .stream()
                .map(bookingMapper::toResponse)
                .collect(Collectors.toList());
    }

    // COMPLETED RIDES
    @Override
    public List<BookingResponse> getCompletedRides(Long driverId) {
        return bookingRepository.findCompletedRides(driverId, BookingStatus.COMPLETED)
                .stream()
                .map(bookingMapper::toResponse)
                .collect(Collectors.toList());
    }

    // PENDING BOOKINGS FOR DRIVER
    @Override
    public List<BookingResponse> getPendingBookingsForDriver(Long driverId) {
        DriverEntity driver = driverService.getDriverById(driverId);

        return bookingRepository.findPendingRidesByCity(BookingStatus.PENDING, driver.getCity().getCityId())
                .stream()
                .map(bookingMapper::toResponse)
                .collect(Collectors.toList());
    }

    // HELPER METHOD
    private BookingEntity fetchBooking(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + bookingId));
    }


    public Optional<BookingEntity> findBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId);
    }





    //Fetch bookings filtered by city, status, and date range
    public List<BookingEntity> findBookings(Long cityId,
                                            BookingStatus status,
                                            LocalDateTime startDateTime,
                                            LocalDateTime endDateTime) {
        log.info("BookingService: Fetching bookings. CityId: {}, Status: {}, Start: {}, End: {}",
                (cityId != null ? cityId : "ALL"),
                (status != null ? status : "ALL"),
                startDateTime,
                endDateTime);

        // Call repository method
        return bookingRepository.findBookings(cityId, status, startDateTime, endDateTime);
    }

    public Long countRidesByCityId(Long cityId) {
        return bookingRepository.countRidesByCityId(cityId);
    }

    public Long countAllBookings() {
        return bookingRepository.count();
    }

    private CityRouteEntity findRoute(BookingRequest request) {
        return Optional.ofNullable(
                        cityRouteService.getRouteEntityByPickupAndDrop(
                                request.getCityId(),
                                request.getPickupLocation(),
                                request.getDropLocation()))
                .orElseThrow(() -> new RuntimeException(
                        "Route not found for the selected pickup & drop locations"));
    }

    private UserEntity findUser(Long userId) {
        return Optional.ofNullable(userService.getUserById(userId))
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    private FareEstResponse findFare(Long cityId) {
        return Optional.ofNullable(fareEstService.getFareEstByCity(cityId))
                .orElseThrow(() -> new RuntimeException("Fare not found for city with ID: " + cityId));
    }

    private BookingEntity saveBooking(
            BookingRequest request,
            UserEntity user,
            CityRouteEntity route,
            double calculatedFare) {

        BookingEntity booking = bookingMapper.toEntity(request, user, route, calculatedFare);
        return bookingRepository.save(booking);
    }


    public List<BookingEntity> findBookingsByUserId(Long userId) {
        return bookingRepository.findIdsByUserId(userId);
    }


    public void deleteBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    public List<BookingEntity> findBookingByDriverId(Long driverId) {
        return bookingRepository.findBookingsByDriverId(driverId);
    }


    public void updateBooking(List<BookingEntity> booking) {

    }
}
*/
