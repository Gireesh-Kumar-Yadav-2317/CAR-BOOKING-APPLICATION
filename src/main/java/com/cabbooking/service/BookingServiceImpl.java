package com.cabbooking.service;

import com.cabbooking.dto.*;
import com.cabbooking.entity.*;
import com.cabbooking.mapper.BookingMapper;
import com.cabbooking.repo.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service layer for booking operations:
 * - Create, update and query ride bookings
 * - Handle driver assignments & ride life-cycle
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements IBookingService {

    private final BookingRepository bookingRepository;
    private final UserServiceImpl userService;
    private final DriverServiceImpl driverService;
    private final CityRouteServiceImpl1 cityRouteService;
    private final FareEstServiceImpl1 fareEstService;
    private final BookingMapper bookingMapper;

    /* ====================== BOOKING LIFE-CYCLE ====================== */

    /** Book a new ride for a user */
    @Override
    public BookingResponse bookRide(BookingRequest request) {
        log.info("Booking ride: userId={}, pickup={}, drop={}",
                request.getUserId(), request.getPickupLocation(), request.getDropLocation());

        CityRouteEntity route = findRoute(request);
        UserEntity user = findUser(request.getUserId());
        double calculatedFare = calculateFare(request.getCityId(), route);
        BookingEntity savedBooking = saveBooking(request, user, route, calculatedFare);

        log.info("Booking created successfully with ID: {}", savedBooking.getBookingId());
        return bookingMapper.toResponse(savedBooking);
    }

    /** Driver accepts a pending ride */
    @Override
    public BookingResponse acceptRide(Long bookingId, Long driverId) {
        BookingEntity booking = fetchBooking(bookingId);
        DriverEntity driver = driverService.getDriverById(driverId);

        booking.setDriver(driver);
        booking.setStatus(BookingStatus.ACCEPTED);
        log.info("Booking {} accepted by driver {}", bookingId, driverId);

        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    /** Driver rejects an assigned ride */
    @Override
    public BookingResponse rejectRide(Long bookingId, Long driverId) {
        BookingEntity booking = fetchBooking(bookingId);

        booking.setDriver(null);
        booking.setStatus(BookingStatus.REJECTED);
        log.info("Booking {} rejected by driver {}", bookingId, driverId);

        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    /** Start a ride when driver begins the trip */
    @Override
    public BookingResponse startRide(Long bookingId) {
        BookingEntity booking = fetchBooking(bookingId);

        booking.setStatus(BookingStatus.ONGOING);
        booking.setStartTime(LocalDateTime.now());
        log.info("Ride started for booking {}", bookingId);

        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    /** Complete ride and reset driver status */
    @Override
    public BookingResponse completeRide(Long bookingId) {
        BookingEntity booking = fetchBooking(bookingId);

        booking.setStatus(BookingStatus.COMPLETED);
        booking.setEndTime(LocalDateTime.now());
        resetDriverStatus(booking.getDriver());

        log.info("Ride completed for booking {}", bookingId);
        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    /** Fetch booking details by ID */
    @Override
    public BookingResponse getBookingById(Long bookingId) {
        return bookingMapper.toResponse(fetchBooking(bookingId));
    }

    /* ====================== DRIVER-RELATED QUERIES ====================== */

    /** Current rides (ACCEPTED/ONGOING) for a driver */
    @Override
    public List<BookingResponse> getCurrentRide(Long driverId) {
        return bookingRepository
                .findByDriverIdAndStatusIn(driverId,
                        Arrays.asList(BookingStatus.ACCEPTED, BookingStatus.ONGOING))
                .stream()
                .map(bookingMapper::toResponse)
                .collect(Collectors.toList());
    }

    /** Completed rides for a driver */
    @Override
    public List<BookingResponse> getCompletedRides(Long driverId) {
        return bookingRepository.findCompletedRides(driverId, BookingStatus.COMPLETED)
                .stream()
                .map(bookingMapper::toResponse)
                .collect(Collectors.toList());
    }

    /** Pending bookings in driver's city */
    @Override
    public List<BookingResponse> getPendingBookingsForDriver(Long driverId) {
        DriverEntity driver = driverService.getDriverById(driverId);
        return bookingRepository
                .findPendingRidesByCity(BookingStatus.PENDING, driver.getCity().getCityId())
                .stream()
                .map(bookingMapper::toResponse)
                .collect(Collectors.toList());
    }

    /* ====================== REPORTS & STATS ====================== */

    /** Bookings filtered by city, status, and date range */
    public List<BookingEntity> findBookings(Long cityId,
                                            BookingStatus status,
                                            LocalDateTime startDateTime,
                                            LocalDateTime endDateTime) {
        log.info("Fetching bookings. CityId: {}, Status: {}, Start: {}, End: {}",
                (cityId != null ? cityId : "ALL"),
                (status != null ? status : "ALL"),
                startDateTime,
                endDateTime);
        return bookingRepository.findBookings(cityId, status, startDateTime, endDateTime);
    }

    public Long countRidesByCityId(Long cityId) {
        return bookingRepository.countRidesByCityId(cityId);
    }

    public Long countAllBookings() {
        return bookingRepository.count();
    }

    /* ====================== SIMPLE CRUD HELPERS ====================== */

    public Optional<BookingEntity> findBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId);
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

    /** Batch update of bookings when driver is unlinked */
    public void updateBooking(List<BookingEntity> bookings) {
        if (bookings == null || bookings.isEmpty()) return;
        bookingRepository.saveAll(bookings);
        log.info("Updated {} bookings in batch", bookings.size());
    }

    /* ====================== PRIVATE UTILITIES ====================== */

    /** Get booking or throw if not found */
    private BookingEntity fetchBooking(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + bookingId));
    }

    /** Find route entity from request */
    private CityRouteEntity findRoute(BookingRequest request) {
        return Optional.ofNullable(
                        cityRouteService.getRouteEntityByPickupAndDrop(
                                request.getCityId(),
                                request.getPickupLocation(),
                                request.getDropLocation()))
                .orElseThrow(() -> new RuntimeException(
                        "Route not found for selected pickup & drop locations"));
    }

    /** Find user entity or throw if missing */
    private UserEntity findUser(Long userId) {
        return Optional.ofNullable(userService.getUserById(userId))
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    /** Calculate fare using route & city fare settings */
    private double calculateFare(Long cityId, CityRouteEntity route) {
        FareEstResponse fare = Optional.ofNullable(fareEstService.getFareEstByCity(cityId))
                .orElseThrow(() -> new RuntimeException("Fare not found for city with ID: " + cityId));
        return fareEstService.calculateFare(route, fare);
    }

    /** Map request to booking entity and save */
    private BookingEntity saveBooking(BookingRequest request, UserEntity user,
                                      CityRouteEntity route, double calculatedFare) {
        return bookingRepository.save(
                bookingMapper.toEntity(request, user, route, calculatedFare));
    }

    /** Reset driver to ACTIVE after ride completion */
    private void resetDriverStatus(DriverEntity driver) {
        if (driver != null) {
            driver.setStatus(DriverStatus.ACTIVE);
            driverService.updateDriver(driver);
        }
    }
}
