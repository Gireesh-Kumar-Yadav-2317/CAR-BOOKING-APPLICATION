package com.cabbooking.controller;

import com.cabbooking.dto.*;
import com.cabbooking.entity.*;
import com.cabbooking.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * Handles all user-related web requests:
 * home page, ride booking, ride tracking,
 * payment, and logout functionality.
 */
@Slf4j
@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController1 {

    private final UserServiceImpl userService;
    private final BookingServiceImpl bookingService;
    private final CityRouteServiceImpl1 cityRouteService;

    /** User home page – ensures user info in session */
    @GetMapping("/{userId}/home")
    public String home(@PathVariable Long userId, HttpSession session, Model model) {
        log.info("Loading home for userId={}", userId);
        model.addAttribute("user", getOrLoadUser(userId, session));
        return "user-home";
    }

    /** GET: Show booking page for a user */
    @GetMapping("/book-ride")
    public String showBookRidePage(@RequestParam Long userId,
                                   @RequestParam(required = false) Long cityId,
                                   Model model) {
        log.info("Opening Book Ride page | userId={} | cityId={}", userId, cityId);
        prepareBookingModel(model, userId, cityId != null ? cityId : cityIdFromUser(userId), "", "", null);
        return "book-ride";
    }

    /** POST: Create a booking for the user */
    @PostMapping("/book-ride")
    public String bookRide(@ModelAttribute BookingRequest req, Model model) {
        log.info("Booking ride | userId={} | pickup={} | drop={}",
                req.getUserId(), req.getPickupLocation(), req.getDropLocation());
        try { return redirectToSearching(bookingService.bookRide(req), req.getUserId()); }
        catch (Exception e) { return handleBookingError(model, req, e.getMessage()); }
    }

    /** Searching driver screen */
    @GetMapping("/searching-driver")
    public String searchingDriver(@RequestParam Long bookingId, Model model) {
        log.info("Searching driver | bookingId={}", bookingId);
        model.addAttribute("booking",
                bookingService.findBookingById(bookingId)
                        .orElseThrow(() -> new RuntimeException("Booking not found")));
        return "searching-driver";
    }

    /** Start or continue a ride */
    @GetMapping("/ride-started")
    public String rideStarted(@RequestParam Long bookingId,
                              @RequestParam(required = false) Long userId,
                              Model model) {
        BookingResponse booking = bookingService.getBookingById(bookingId);
        log.info("Ride start requested | bookingId={} | status={}", bookingId, booking.getBookingStatus());
        return processRideStatus(bookingId, userId, booking, model);
    }

    /** Current ride page – shows status or payment */
    @GetMapping("/current-ride/{userId}/{bookingId}")
    public String currentRide(@PathVariable Long userId,
                              @PathVariable Long bookingId,
                              Model model) {
        BookingResponse booking = bookingService.getBookingById(bookingId);
        log.info("Current ride | bookingId={} | userId={} | status={}",
                bookingId, userId, booking.getBookingStatus());
        return processRideStatus(bookingId, userId, booking, model);
    }

    /** AJAX: Get booking status with driver info */
    @GetMapping("/booking-status")
    @ResponseBody
    public Map<String,Object> bookingStatus(@RequestParam Long bookingId) {
        BookingResponse booking = bookingService.getBookingById(bookingId);
        log.debug("Fetching booking status | bookingId={} | status={}",
                bookingId, booking.getBookingStatus());
        return buildStatusResponse(booking);
    }

    /** Update session after successful payment */
    @PostMapping("/payment-success")
    public String paymentSuccess(@RequestParam Long userId, HttpSession session) {
        log.info("Payment success | userId={}", userId);
        ensureUserInSession(userId, session);
        return "redirect:/users/" + userId + "/home";
    }

    /** Logout user and invalidate session */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        log.info("Logging out current user");
        session.invalidate();
        return "redirect:/users/login";
    }

    /*  Private Helper Methods  */

    private UserDTO getOrLoadUser(Long userId, HttpSession session) {
        UserDTO u = (UserDTO) session.getAttribute("loggedInUser");
        if (u != null && userId.equals(u.getUserId())) return u;
        UserDTO dto = userService.mapToDTO(userService.getUserById(userId));
        session.setAttribute("loggedInUser", dto);
        return dto;
    }

    private void prepareBookingModel(Model m, Long userId, Long cityId,
                                     String pickup, String drop, String error) {
        BookingRequest br = new BookingRequest();
        br.setUserId(userId); br.setCityId(cityId);
        m.addAttribute("bookingRequest", br);
        m.addAttribute("routes", cityRouteService.getRoutesByCity(cityId));
        m.addAttribute("selectedPickup", pickup);
        m.addAttribute("selectedDrop", drop);
        m.addAttribute("error", error);
    }

    private Long cityIdFromUser(Long userId) {
        UserEntity u = userService.getUserById(userId);
        if (u == null || u.getCity() == null)
            throw new IllegalArgumentException("City not found for user " + userId);
        return u.getCity().getCityId();
    }

    private String redirectToSearching(BookingResponse res, Long userId) {
        log.info("Booking successful | bookingId={}", res.getBookingId());
        return "redirect:/users/searching-driver?bookingId=" + res.getBookingId()
                + "&userId=" + userId;
    }

    private String handleBookingError(Model m, BookingRequest r, String err) {
        log.error("Booking failed | userId={} | error={}", r.getUserId(), err);
        prepareBookingModel(m, r.getUserId(), r.getCityId(),
                r.getPickupLocation(), r.getDropLocation(), err);
        return "book-ride";
    }

    private String processRideStatus(Long bookingId, Long userId,
                                     BookingResponse booking, Model model) {
        BookingStatus s = booking.getBookingStatus();
        if (s == BookingStatus.ACCEPTED) {
            booking = bookingService.startRide(bookingId);
            model.addAttribute("message", "Ride started successfully.");
        } else if (s == BookingStatus.COMPLETED) {
            model.addAttribute("booking", booking);
            model.addAttribute("userId", userId);
            return "user-payment";
        }
        model.addAttribute("booking", booking);
        model.addAttribute("userId", userId);
        return "user-ride-started";
    }

    private Map<String,Object> buildStatusResponse(BookingResponse b) {
        Map<String,Object> map = new HashMap<>();
        map.put("status", b.getBookingStatus().name());
        if (b.getBookingStatus() == BookingStatus.ACCEPTED && b.getDriver() != null) {
            map.put("driverName", b.getDriver().getDisplayName());
            map.put("driverMobile", b.getDriver().getMobileNumber());
        }
        map.put("userId", b.getUser().getUserId());
        return map;
    }

    private void ensureUserInSession(Long userId, HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) {
            UserEntity u = userService.getUserById(userId);
            UserDTO dto = new UserDTO(u.getUserId(), u.getUsername(), u.getDisplayName());
            session.setAttribute("loggedInUser", dto);
            log.debug("User info stored in session | userId={}", userId);
        }
    }
}
