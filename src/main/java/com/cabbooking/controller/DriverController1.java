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
public class DriverController1 {

    private final BookingServiceImpl bookingService;
    private final DriverServiceImpl driverService;

    /*  PAGES  */

    @GetMapping("/{driverId}/home")
    public String driverHome(@PathVariable Long driverId, Model model) {
        model.addAttribute("driver", driverService.getDriverById(driverId));
        populateRideLists(driverId, model);
        return "driver-home";
    }

    @GetMapping("/{driverId}/pending")
    public String pendingRides(@PathVariable Long driverId, Model model) {
        model.addAttribute("rides", bookingService.getPendingBookingsForDriver(driverId));
        model.addAttribute("driverId", driverId);
        return "pendingRides";
    }

    @GetMapping("/{driverId}/current")
    public String currentRides(@PathVariable Long driverId, Model model) {
        model.addAttribute("currentRide", firstCurrentRide(driverId));
        model.addAttribute("driverId", driverId);
        return "current-rides";
    }

    @PostMapping("/{driverId}/complete/{bookingId}")
    public String completeRide(@PathVariable Long driverId, @PathVariable Long bookingId, Model model) {
        try { addRideAndQr(model, bookingService.completeRide(bookingId)); }
        catch (Exception e) { model.addAttribute("errorMessage", "Failed to complete ride."); }
        return "driver-ride-completed";
    }

    /*  JSON APIs  */

    @GetMapping("/{driverId}/pending/json")
    @ResponseBody
    public List<BookingResponse> getPendingRidesJson(@PathVariable Long driverId) {
        return bookingService.getPendingBookingsForDriver(driverId);
    }

    @GetMapping("/{driverId}/pending/latest")
    @ResponseBody
    public Map<String, Object> getLatestPendingRide(@PathVariable Long driverId) {
        return latestRideResponse(bookingService.getPendingBookingsForDriver(driverId));
    }

    @GetMapping("/{driverId}/ride-status")
    @ResponseBody
    public Map<String, String> getRideStatus(@RequestParam Long bookingId) {
        return Map.of("status",
                bookingService.getBookingById(bookingId) != null ?
                        bookingService.getBookingById(bookingId).getBookingStatus().name() : "NOT_FOUND");
    }

    /*  ACTIONS  */

    @PostMapping("/{driverId}/accept/{bookingId}")
    public String acceptRide(@PathVariable Long driverId, @PathVariable Long bookingId,
                             RedirectAttributes ra) {
        return performRideAction(() -> bookingService.acceptRide(bookingId, driverId),
                ra, "/driver/" + driverId + "/current");
    }

    @PostMapping("/{driverId}/start/{bookingId}")
    public String startRide(@PathVariable Long driverId, @PathVariable Long bookingId,
                            RedirectAttributes ra) {
        return performRideAction(() -> bookingService.startRide(bookingId),
                ra, "/driver/" + driverId + "/current");
    }

    @PostMapping("/{driverId}/reject/{bookingId}")
    public String rejectRide(@PathVariable Long driverId, @PathVariable Long bookingId,
                             RedirectAttributes ra) {
        return performRideAction(() -> bookingService.rejectRide(bookingId, driverId),
                ra, "/driver/" + driverId + "/pending");
    }

    /*  PRIVATE HELPERS  */

    private void populateRideLists(Long driverId, Model model) {
        model.addAttribute("pendingRides", bookingService.getPendingBookingsForDriver(driverId));
        model.addAttribute("currentRides", bookingService.getCurrentRide(driverId));
        model.addAttribute("completedRides", bookingService.getCompletedRides(driverId));
    }

    private BookingResponse firstCurrentRide(Long driverId) {
        return bookingService.getCurrentRide(driverId).stream()
                .filter(r -> r.getBookingStatus() == BookingStatus.ACCEPTED
                        || r.getBookingStatus() == BookingStatus.ONGOING)
                .findFirst().orElse(null);
    }

    private void addRideAndQr(Model model, BookingResponse ride) throws Exception {
        String content = "Pay ₹" + ride.getFareAmount() + " to Driver: " + ride.getDriver();
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, 250, 250);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", out);
        model.addAttribute("ride", ride);
        model.addAttribute("qrCode", Base64.getEncoder().encodeToString(out.toByteArray()));
    }

    private Map<String, Object> latestRideResponse(List<BookingResponse> rides) {
        if (rides.isEmpty()) return Map.of("hasRide", false);
        BookingResponse r = rides.get(rides.size() - 1);
        return Map.of("hasRide", true,
                "bookingId", r.getBookingId(),
                "pickup", r.getRoute().getPickupLocation(),
                "drop", r.getRoute().getDropLocation(),
                "fare", r.getFareAmount(),
                "userName", r.getUser().getUsername(),
                "mobileNumber", r.getUser().getMobileNumber());
    }

    private String performRideAction(Runnable action, RedirectAttributes ra, String redirectUrl) {
        try { action.run(); }
        catch (Exception e) {
            log.error("Ride action failed: {}", e.getMessage(), e);
            ra.addFlashAttribute("message", "Action failed. Try again.");
        }
        return "redirect:" + redirectUrl;
    }
}
