/*
package com.cabbooking.controller;

import com.cabbooking.dto.*;

import com.cabbooking.entity.BookingStatus;
import com.cabbooking.entity.DriverEntity;
import com.cabbooking.service.BookingServiceImpl;
import com.cabbooking.service.DriverServiceImpl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {
    private final DriverServiceImpl driverService;

    private  final BookingServiceImpl bookingService;
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody DriverSignupRequest request){
        try{
            driverService.signup(request);
            return  ResponseEntity.ok("Driver registered successfully");
        } catch (Exception e) {
            return  ResponseEntity.badRequest().body("Signup failed : " + e.getMessage());
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestBody SigninRequest request) {
        boolean isAuthenticated = driverService.signin(request);

        if (isAuthenticated) {
            return ResponseEntity.ok("Login successful!");
        }

        return ResponseEntity.badRequest().body("Invalid credentials");
    }


 @GetMapping("/{driverId}/pending-bookings")
    public ResponseEntity<List<BookingResponse>> getPendingBookings(@PathVariable Long driverId) {
        return ResponseEntity.ok(bookingService.getPendingBookingsForDriver(driverId));
    }

    @PutMapping("/{driverId}/bookings/{bookingId}/accept")
    public ResponseEntity<BookingResponse> acceptRide(@PathVariable Long driverId, @PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.acceptRide(bookingId, driverId));
    }

    @PutMapping("/{driverId}/bookings/{bookingId}/reject")
    public ResponseEntity<BookingResponse> rejectRide(@PathVariable Long driverId, @PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.rejectRide(bookingId, driverId));
    }

    @PutMapping("/{driverId}/bookings/{bookingId}/start")
    public ResponseEntity<BookingResponse> startRide(@PathVariable Long driverId, @PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.startRide(bookingId));
    }

    @PutMapping("/{driverId}/bookings/{bookingId}/complete")
    public ResponseEntity<BookingResponse> completeRide(@PathVariable Long driverId, @PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.completeRide(bookingId));
    }




}







package com.cabbooking.controller;

import com.cabbooking.dto.BookingResponse;
import com.cabbooking.entity.BookingStatus;
import com.cabbooking.entity.DriverEntity;
import com.cabbooking.service.BookingServiceImpl;
import com.cabbooking.service.DriverServiceImpl;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
        import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/driver")
@RequiredArgsConstructor
public class DriverController {

    private final BookingServiceImpl bookingService;
    private final DriverServiceImpl driverService;

    */
/** DRIVER HOME PAGE **//*

    @GetMapping("/{driverId}/home")
    public String driverHome(@PathVariable Long driverId, Model model) {
        log.info("Loading driver home page for driverId={}", driverId);

        DriverEntity driver = driverService.getDriverById(driverId);
        if (driver == null) {
            log.warn("Driver not found: driverId={}", driverId);
        }
        model.addAttribute("driver", driver);

        // Fetch rides by status
        List<BookingResponse> pending = bookingService.getPendingBookingsForDriver(driverId);
        List<BookingResponse> current = bookingService.getCurrentRide(driverId);
        List<BookingResponse> completed = bookingService.getCompletedRides(driverId);

        model.addAttribute("pendingRides", pending);
        model.addAttribute("currentRides", current);
        model.addAttribute("completedRides", completed);

        log.info("Driver home loaded: pending={}, current={}, completed={}",
                pending.size(), current.size(), completed.size());
        return "driver-home";
    }

    */
/** PENDING RIDES PAGE **//*

    @GetMapping("/{driverId}/pending")
    public String pendingRides(@PathVariable Long driverId, Model model) {
        List<BookingResponse> rides = bookingService.getPendingBookingsForDriver(driverId);
        log.info("Fetching pending rides for driverId={} count={}", driverId, rides.size());
        model.addAttribute("rides", rides);
        model.addAttribute("driverId", driverId);
        return "pendingRides";
    }

    */
/** PENDING RIDES JSON (AJAX) **//*

    @GetMapping("/{driverId}/pending/json")
    @ResponseBody
    public List<BookingResponse> getPendingRidesJson(@PathVariable Long driverId) {
        List<BookingResponse> rides = bookingService.getPendingBookingsForDriver(driverId);
        log.info("Returning pending rides JSON for driverId={} count={}", driverId, rides.size());
        return rides;
    }

    */
/** LATEST PENDING RIDE API **//*

    @GetMapping("/{driverId}/pending/latest")
    @ResponseBody
    public Map<String, Object> getLatestPendingRide(@PathVariable Long driverId) {
        Map<String, Object> response = new HashMap<>();
        List<BookingResponse> rides = bookingService.getPendingBookingsForDriver(driverId);

        if (!rides.isEmpty()) {
            BookingResponse latest = rides.get(rides.size() - 1);
            response.put("hasRide", true);
            response.put("bookingId", latest.getBookingId());
            response.put("pickup", latest.getRoute().getPickupLocation());
            response.put("drop", latest.getRoute().getDropLocation());
            response.put("fare", latest.getFareAmount());
            response.put("userName", latest.getUser().getUsername());
            response.put("mobileNumber", latest.getUser().getMobileNumber());
            log.info("Latest pending ride found: bookingId={}", latest.getBookingId());
        } else {
            response.put("hasRide", false);
            log.info("No pending rides for driverId={}", driverId);
        }
        return response;
    }

    */
/** ACCEPT RIDE **//*

    @PostMapping("/{driverId}/accept/{bookingId}")
    public String acceptRide(@PathVariable Long driverId,
                             @PathVariable Long bookingId,
                             RedirectAttributes redirectAttributes) {
        try {
            log.info("Driver {} accepting booking {}", driverId, bookingId);
            bookingService.acceptRide(bookingId, driverId);
            log.info("Booking {} ACCEPTED by driver {}", bookingId, driverId);
        } catch (Exception e) {
            log.error("Error accepting booking {}: {}", bookingId, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("message", "Failed to accept ride.");
        }
        return "redirect:/driver/" + driverId + "/current";
    }

    */
/** CURRENT RIDE PAGE **//*

    @GetMapping("/{driverId}/current")
    public String currentRides(@PathVariable Long driverId, Model model) {
        List<BookingResponse> rides = bookingService.getCurrentRide(driverId)
                .stream()
                .filter(r -> r.getBookingStatus() == BookingStatus.ACCEPTED
                        || r.getBookingStatus() == BookingStatus.ONGOING)
                .toList();

        if (!rides.isEmpty()) {
            model.addAttribute("currentRide", rides.get(0));
            log.info("Current ride loaded: bookingId={} status={}",
                    rides.get(0).getBookingId(), rides.get(0).getBookingStatus());
        } else {
            model.addAttribute("currentRide", null);
            log.info("No current ride assigned for driverId={}", driverId);
        }

        model.addAttribute("driverId", driverId);
        return "current-rides";
    }

    */
/** START RIDE **//*

    @PostMapping("/{driverId}/start/{bookingId}")
    public String startRide(@PathVariable Long driverId,
                            @PathVariable Long bookingId,
                            RedirectAttributes redirectAttributes) {
        try {
            log.info("Driver {} starting ride {}", driverId, bookingId);
            BookingResponse ride = bookingService.startRide(bookingId); // Service should update status to ONGOING
            log.info("Ride {} started. Status: {}", bookingId, ride.getBookingStatus());
        } catch (Exception e) {
            log.error("Error starting ride {}: {}", bookingId, e.getMessage());
            redirectAttributes.addFlashAttribute("message", "Failed to start ride.");
        }
        return "redirect:/driver/" + driverId + "/current";
    }


    @PostMapping("/{driverId}/complete/{bookingId}")
    public String completeRide(@PathVariable Long driverId,
                               @PathVariable Long bookingId,
                               Model model) {
        try {
            BookingResponse ride = bookingService.completeRide(bookingId);

            // Generate QR code
            String qrContent = "Pay ₹" + ride.getFareAmount() + " to Driver: " + ride.getDriver();
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 250, 250);

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            String qrBase64 = Base64.getEncoder().encodeToString(pngOutputStream.toByteArray());

            model.addAttribute("ride", ride);
            model.addAttribute("qrCode", qrBase64);

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to complete ride. Try again.");
        }
        return "driver-ride-completed"; // JSP page
    }

    */
/** REJECT RIDE **//*

    @PostMapping("/{driverId}/reject/{bookingId}")
    public String rejectRide(@PathVariable Long driverId,
                             @PathVariable Long bookingId,
                             RedirectAttributes redirectAttributes) {
        try {
            log.info("Driver {} rejecting ride {}", driverId, bookingId);
            bookingService.rejectRide(bookingId, driverId);
            redirectAttributes.addFlashAttribute("message", "You have rejected this ride.");
            log.info("Ride {} REJECTED by driver {}", bookingId, driverId);
        } catch (Exception e) {
            log.error("Error rejecting ride {}: {}", bookingId, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("message", "Failed to reject ride. Try again.");
        }
        return "redirect:/driver/" + driverId + "/pending";
    }
    @GetMapping("/{driverId}/ride-status")
    @ResponseBody
    public Map<String, String> getRideStatus(@PathVariable Long driverId,
                                             @RequestParam Long bookingId) {
        BookingResponse ride = bookingService.getBookingById(bookingId);
        Map<String, String> response = new HashMap<>();
        if (ride != null) {
            response.put("status", ride.getBookingStatus().name());
        } else {
            response.put("status", "NOT_FOUND");
        }
        return response;
    }

}

*/
