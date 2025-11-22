/*
package com.cabbooking.controller;

import com.cabbooking.dto.*;
import com.cabbooking.entity.BookingEntity;
import com.cabbooking.entity.BookingStatus;
import com.cabbooking.entity.UserEntity;
import com.cabbooking.service.BookingServiceImpl;
import com.cabbooking.service.CityRouteServiceImpl;
import com.cabbooking.service.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;
   // private final BookingService bookingService;

    */
/**
     * Signup a new user
     *//*

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserSignupRequest request) {
        log.info("Received signup request for username: {}", request.getUsername());
        try {
            UserDTO createdUser = userService.signup(request);
            log.info("User created successfully: {}", createdUser.getUsername());
            return ResponseEntity.ok(createdUser);
        } catch (Exception e) {
            log.error("Signup failed for username: {} | Error: {}", request.getUsername(), e.getMessage(), e);
            return ResponseEntity.badRequest().body("Signup failed: " + e.getMessage());
        }
    }

    */
/**
     * Signin
     *//*

    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestBody SigninRequest request) {
        log.info("Signin attempt for username: {}", request.getUsername());
        boolean isAuthenticated = userService.signin(request);

        if (isAuthenticated) {
            log.info("Signin successful for username: {}", request.getUsername());
            return ResponseEntity.ok("Login successful!");
        } else {
            log.warn("Signin failed for username: {}", request.getUsername());
            return ResponseEntity.badRequest().body("Invalid Credentials");
        }
    }

    */
/**
     * Get all users
     *//*

    @GetMapping
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        log.info("Fetching all users");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    */
/**
     * Get user by ID
     *//*

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        log.info("Fetching user with ID: {}", userId);
        try {
            return ResponseEntity.ok(userService.getUserById(userId));
        } catch (Exception e) {
            log.error("User not found with ID: {}", userId);
            return ResponseEntity.badRequest().body("User not found with ID: " + userId);
        }
    }

    */
/**
     * Book ride for user
     *//*

   */
/* @PostMapping("/{userId}/book-ride")
    public ResponseEntity<?> bookRide(@PathVariable Long userId, @RequestBody BookingRequest request) {
        log.info("Booking ride for userId: {}", userId);
        try {
            request.setUserId(userId);
            BookingResponse response = bookingService.bookRide(request);
            log.info("Ride booked successfully for userId: {}", userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Ride booking failed for userId: {} | Error: {}", userId, e.getMessage(), e);
            return ResponseEntity.badRequest().body("Ride booking failed: " + e.getMessage());
        }
    }*//*

}


package com.cabbooking.controller;

import com.cabbooking.dto.*;
        import com.cabbooking.entity.BookingEntity;
import com.cabbooking.entity.BookingStatus;
import com.cabbooking.entity.CityRouteEntity;
import com.cabbooking.entity.UserEntity;
import com.cabbooking.service.BookingServiceImpl;
import com.cabbooking.service.CityRouteServiceImpl;
import com.cabbooking.service.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

        import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j // Enables logger via Lombok
public class UserController1 {


    private final UserServiceImpl userService;
    private final BookingServiceImpl bookingService;
    private final CityRouteServiceImpl cityRouteService;


    */
/**
     * User Home Page
     *//*

    @GetMapping("/{userId}/home")
    public String home(@PathVariable Long userId, HttpSession session, Model model) {
        log.info("Fetching home page for userId={}", userId);

        // Get the user from session
        UserDTO loggedInUser = (UserDTO) session.getAttribute("loggedInUser");

        if (loggedInUser == null || !loggedInUser.getUserId().equals(userId)) {
            // Fetch UserEntity from DB
            var userEntity = userService.getUserById(userId);

            // Convert to DTO for session
            loggedInUser = userService.mapToDTO(userEntity);

            // Save DTO in session
            session.setAttribute("loggedInUser", loggedInUser);
        }

        // Add DTO to model for view
        model.addAttribute("user", loggedInUser);

        return "user-home";
    }


    @GetMapping("/book-ride")
    public String showBookRidePage(@RequestParam Long userId,
                                   @RequestParam(required = false) Long cityId,
                                   Model model) {
        log.info("Request received to show Book Ride page | userId={} | cityId={}", userId, cityId);

        // Determine cityId if not provided
        if (cityId == null) {
            cityId = fetchCityIdFromUser(userId);
        }

        // Prepare the model for the booking page (reusable helper method)
        prepareBookingPageModel(model, userId, cityId, "", "", null);

        log.info("Forwarding to 'book-ride.jsp' for userId={} | cityId={}", userId, cityId);
        return "book-ride";
    }

    @PostMapping("/book-ride")
    public String bookRide(@ModelAttribute BookingRequest bookingRequest, Model model) {
        log.info("Booking ride for userId={} pickup={} drop={}",
                bookingRequest.getUserId(),
                bookingRequest.getPickupLocation(),
                bookingRequest.getDropLocation());

        try {
            BookingResponse response = bookingService.bookRide(bookingRequest);
            log.info("Booking successful. BookingId={}", response.getBookingId());

            // Redirect to searching driver page
            return "redirect:/users/searching-driver?bookingId=" + response.getBookingId()
                    + "&userId=" + bookingRequest.getUserId();
        } catch (Exception e) {
            log.error("Booking failed for userId={}: {}", bookingRequest.getUserId(), e.getMessage());

            // Reload model attributes using the reusable helper
            prepareBookingPageModel(
                    model,
                    bookingRequest.getUserId(),
                    bookingRequest.getCityId(),
                    bookingRequest.getPickupLocation(),
                    bookingRequest.getDropLocation(),
                    e.getMessage()
            );

            return "book-ride";
        }
    }

    */
/**
     * Helper method to prepare the booking page model.
     * This method is reusable for both GET and POST handlers.
     *//*

    private void prepareBookingPageModel(Model model,
                                         Long userId,
                                         Long cityId,
                                         String selectedPickup,
                                         String selectedDrop,
                                         String errorMessage) {

        // Fetch available routes for the city
        List<CityRouteDTO> routes = cityRouteService.getRoutesByCity(cityId);

        // Prepare booking request for form binding
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setUserId(userId);
        bookingRequest.setCityId(cityId);

        // Add attributes to model
        model.addAttribute("bookingRequest", bookingRequest);
        model.addAttribute("routes", routes);
        model.addAttribute("selectedPickup", selectedPickup);
        model.addAttribute("selectedDrop", selectedDrop);
        model.addAttribute("error", errorMessage);
    }

    */
/**
     * Fetch cityId from user's profile.
     * Throws exception if user or city is not found.
     *//*

    private Long fetchCityIdFromUser(Long userId) {
        log.debug("Fetching cityId from user profile for userId={}", userId);

        UserEntity user = userService.getUserById(userId);
        if (user == null) {
            log.error("User not found for userId={}", userId);
            throw new IllegalArgumentException("User not found with id: " + userId);
        }

        if (user.getCity() == null) {
            log.error("City not mapped for userId={}", userId);
            throw new IllegalArgumentException("City not found for user id: " + userId);
        }

        log.debug("Fetched cityId={} for userId={}", user.getCity().getCityId(), userId);
        return user.getCity().getCityId();
    }

    */
/**
     * Show Searching Driver Page
     *//*

    @GetMapping("/searching-driver")
    public String showSearchingDriver(@RequestParam Long bookingId,
                                      @RequestParam Long userId,
                                      Model model) {
        log.info("Showing searching driver page for bookingId={}", bookingId);

        BookingEntity booking = bookingService.findBookingById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        model.addAttribute("booking", booking);
        return "searching-driver";
    }


    @GetMapping("/ride-started")
    public String rideStarted(@RequestParam Long bookingId,
                              @RequestParam(required = false) Long userId, // make optional
                              Model model) {

        BookingResponse booking = bookingService.getBookingById(bookingId);
        BookingStatus status = booking.getBookingStatus();

        switch (status) {
            case ACCEPTED -> {
                booking = bookingService.startRide(bookingId);
                model.addAttribute("message", "Ride started successfully.");
            }
            case ONGOING -> model.addAttribute("message", "Ride is already ongoing.");
            case COMPLETED -> {
                model.addAttribute("booking", booking);
                model.addAttribute("userId", userId);
                return "user-payment";
            }
            default -> model.addAttribute("message", "Invalid booking status.");
        }

        model.addAttribute("booking", booking);
        model.addAttribute("userId", userId);
        return "user-ride-started";
    }



    */
/**
     * Current Ride Page
     * Shows the current ride status or payment page if completed
     *//*

    @GetMapping("/current-ride/{userId}/{bookingId}")
    public String currentRide(@PathVariable Long userId,
                              @PathVariable Long bookingId,
                              Model model) {

        BookingResponse booking = bookingService.getBookingById(bookingId);
        log.info("CurrentRide page requested: bookingId={}, userId={}, status={}", bookingId, userId, booking.getBookingStatus());

        if (booking.getBookingStatus() == BookingStatus.ACCEPTED) {
            // Automatically start the ride
            booking = bookingService.startRide(bookingId);
            log.info("Ride started automatically for bookingId={}", bookingId);
            model.addAttribute("message", "Ride started successfully.");
        } else if (booking.getBookingStatus() == BookingStatus.COMPLETED) {
            model.addAttribute("booking", booking);
            model.addAttribute("userId", userId);
            return "user-payment"; // redirect to payment page
        }

        model.addAttribute("booking", booking);
        model.addAttribute("userId", userId);
        return "user-ride-started"; // ongoing ride
    }

    @GetMapping("/booking-status")
    @ResponseBody
    public Map<String, Object> bookingStatus(@RequestParam Long bookingId) {
        BookingResponse booking = bookingService.getBookingById(bookingId);

        Map<String, Object> response = new HashMap<>();
        response.put("status", booking.getBookingStatus().name());

        if (booking.getBookingStatus() == BookingStatus.ACCEPTED && booking.getDriver() != null) {
            response.put("driverName", booking.getDriver().getDisplayName());
            response.put("driverMobile", booking.getDriver().getMobileNumber());
        }

        // Add userId so JS can redirect correctly
        response.put("userId", booking.getUser().getUserId());

        return response;
    }

    */
/**
     * Payment Success
     * Updates session info and redirects to user home
     *//*

    @PostMapping("/payment-success")
    public String paymentSuccess(@RequestParam Long userId, HttpSession session) {

        log.info("Payment success for userId={}", userId);
        UserDTO loggedInUser = (UserDTO) session.getAttribute("loggedInUser");

        // If session does not have user info, fetch and add
        if (loggedInUser == null || loggedInUser.getUserId() == null) {
            UserEntity user = userService.getUserById(userId);
            UserDTO dto = new UserDTO();
            dto.setUserId(user.getUserId());
            dto.setDisplayName(user.getDisplayName());
            dto.setUsername(user.getUsername());
            session.setAttribute("loggedInUser", dto);
            log.info("User info added to session for userId={}", userId);
        }

        return "redirect:/users/" + userId + "/home";
    }





    */
/**
     * Logout User
     *//*

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        log.info("Logging out user");
        session.invalidate();
        return "redirect:/users/login";
    }
}*/
