package com.cabbooking.controller;

import com.cabbooking.dto.DriverDTO;
import com.cabbooking.dto.UserDTO;
import com.cabbooking.entity.BookingStatus;
import com.cabbooking.entity.DriverStatus;
import com.cabbooking.entity.UserStatus;
import com.cabbooking.service.AdminServiceImpl1;
import com.cabbooking.service.CityServiceImpl1;
import com.cabbooking.service.DriverServiceImpl;
import com.cabbooking.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AdminController1
 * -----------------
 * Central controller for all Admin-related operations:
 *  - Dashboard / Home statistics
 *  - Booking filters and viewing
 *  - System monitoring (users, drivers, today's bookings)
 *  - User and Driver management (update / delete)
 *
 * All endpoints are under "/admin".
 */
@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController1 {

    private final AdminServiceImpl1 adminService;
    private final UserServiceImpl userService;
    private final DriverServiceImpl driverService;
    private final CityServiceImpl1 cityService;

    /**
     * Display the Admin Home (Dashboard) with statistics and city wise data.
     */
    @GetMapping("/{adminId}/home")
    public String adminHome(@PathVariable Long adminId,
                            @RequestParam(required = false) Long cityId,
                            Model model) {
        log.info("Admin Home requested | adminId: {}, cityId: {}", adminId, cityId);
        addCommonAttributes(model, adminId, cityId);
        addDashboardStats(model, cityId);
        return "admin-home";
    }

    /**
     * View all bookings with optional filters:
     * city, status and date range.
     */
    @GetMapping("/{adminId}/bookings")
    public String viewBookings(@PathVariable Long adminId,
                               @RequestParam(required = false) Long cityId,
                               @RequestParam(required = false) String status,
                               @RequestParam(required = false)
                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                               @RequestParam(required = false)
                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                               Model model) {
        log.info("View Bookings | adminId: {}, cityId: {}, status: {}, start: {}, end: {}",
                adminId, cityId, status, startDate, endDate);
        addCommonAttributes(model, adminId, cityId);
        loadBookings(model, cityId, status, startDate, endDate);
        return "admin-bookings";
    }

    /**
     * Monitor the system for selected city or all cities.
     * Shows users, drivers and today's bookings.
     */
    @GetMapping("/{adminId}/monitor")
    public String monitorSystem(@PathVariable Long adminId,
                                @RequestParam(required = false) Long cityId,
                                Model model) {
        log.info("Monitor System | adminId: {}, cityId: {}", adminId, cityId);
        addCommonAttributes(model, adminId, cityId);
        loadMonitorData(model, cityId);
        return "admin-monitor";
    }

    /**
     * Load the User Update page for a particular user.
     */
    @GetMapping("/user/{userId}/update")
    public String userUpdatePage(@PathVariable Long userId,
                                 @RequestParam Long adminId,
                                 Model model) {
        log.info("Open User Update Page | adminId: {}, userId: {}", adminId, userId);
        addCommonAttributes(model, adminId, null);
        model.addAttribute("user", userService.getUserDTOById(userId));
        model.addAttribute("userStatuses", UserStatus.values());
        return "user-update";
    }

    /**
     * Update an existing user's details (partial update).
     */
    @PutMapping("/user/{userId}")
    public String updateUser(@PathVariable Long userId,
                             @ModelAttribute UserDTO userDTO,
                             @RequestParam Long adminId,
                             RedirectAttributes ra) {
        log.info("Update User Request | adminId: {}, userId: {}", adminId, userId);
        adminService.updateUserPartial(userId, buildUserUpdates(userDTO));
        ra.addFlashAttribute("message", "User updated successfully!");
        return redirectMonitor(adminId);
    }

    /**
     * Delete a user by id.
     */
    @DeleteMapping("/user/{userId}/delete")
    public String deleteUser(@PathVariable Long userId,
                             @RequestParam Long adminId,
                             RedirectAttributes ra) {
        log.info("Delete User | adminId: {}, userId: {}", adminId, userId);
        adminService.deleteUser(userId);
        ra.addFlashAttribute("message", "User deleted successfully!");
        return redirectMonitor(adminId);
    }

    /**
     * Load the Driver Update page.
     */
    @GetMapping("/driver/{driverId}/update")
    public String driverUpdatePage(@PathVariable Long driverId,
                                   @RequestParam Long adminId,
                                   Model model) {
        log.info("Open Driver Update Page | adminId: {}, driverId: {}", adminId, driverId);
        addCommonAttributes(model, adminId, null);
        model.addAttribute("driver", driverService.getDriverDTOById(driverId));
        model.addAttribute("driverStatuses", DriverStatus.values());
        return "driver-update";
    }

    /**
     * Update an existing driver's details (partial update).
     */
    @PutMapping("/driver/{driverId}")
    public String updateDriver(@PathVariable Long driverId,
                               @ModelAttribute DriverDTO driverDTO,
                               @RequestParam Long adminId,
                               RedirectAttributes ra) {
        log.info("Update Driver Request | adminId: {}, driverId: {}", adminId, driverId);
        adminService.updateDriverPartial(driverId, buildDriverUpdates(driverDTO));
        ra.addFlashAttribute("message", "Driver updated successfully!");
        return redirectMonitor(adminId);
    }

    /**
     * Delete a driver by id.
     */
    @DeleteMapping("/driver/{driverId}")
    public String deleteDriver(@PathVariable Long driverId,
                               @RequestParam Long adminId,
                               RedirectAttributes ra) {
        log.info("Delete Driver | adminId: {}, driverId: {}", adminId, driverId);
        adminService.deleteDriver(driverId);
        ra.addFlashAttribute("message", "Driver deleted successfully!");
        return redirectMonitor(adminId);
    }

    /*
       PRIVATE HELPER METHODS
       These methods encapsulate repeated logic for model population
       and request handling, keeping controller methods concise.
      */

    /**
     * Add common attributes to the model
     * used across multiple pages: adminId, cities list, selectedCityId.
     */
    private void addCommonAttributes(Model model, Long adminId, Long cityId) {
        log.debug("Adding common model attributes | adminId: {}, cityId: {}", adminId, cityId);
        model.addAttribute("adminId", adminId);
        model.addAttribute("cities", cityService.getAllCities());
        model.addAttribute("selectedCityId", cityId);
    }

    /**
     * Add dashboard statistics such as total counts and
     * lists of users and drivers for a city (or all cities).
     */
    private void addDashboardStats(Model model, Long cityId) {
        log.debug("Adding dashboard stats | cityId: {}", cityId);
        model.addAttribute("totalUsers", adminService.countUser(cityId));
        model.addAttribute("totalDrivers", adminService.countDriver(cityId));
        model.addAttribute("totalRides", adminService.countRides(cityId));
        model.addAttribute("users", adminService.getUsersByCity(cityId));
        model.addAttribute("drivers", adminService.getDriversByCity(cityId));
    }

    /**
     * Load bookings based on filters and add to model.
     */
    private void loadBookings(Model model, Long cityId, String status,
                              LocalDate startDate, LocalDate endDate) {
        boolean filterApplied = cityId != null || (status != null && !status.isEmpty())
                || startDate != null || endDate != null;
        log.debug("Loading bookings | cityId: {}, status: {}, start: {}, end: {}, filterApplied: {}",
                cityId, status, startDate, endDate, filterApplied);
        model.addAttribute("bookings", filterApplied ?
                adminService.getBookings(cityId, status, startDate, endDate) : List.of());
        model.addAttribute("filterApplied", filterApplied);
        model.addAttribute("bookingStatuses", BookingStatus.values());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("startDate", startDate != null ? startDate.toString() : "");
        model.addAttribute("endDate", endDate != null ? endDate.toString() : "");
    }

    /**
     * Load monitor page data: user/driver statuses,
     * list of users & drivers and today's bookings.
     */
    private void loadMonitorData(Model model, Long cityId) {
        LocalDate today = LocalDate.now();
        log.debug("Loading monitor data | cityId: {}, today: {}", cityId, today);
        model.addAttribute("userStatuses", UserStatus.values());
        model.addAttribute("driverStatuses", DriverStatus.values());
        model.addAttribute("users", cityId == null ?
                userService.getAllUsers() : userService.getAllUsersByCityId(cityId));
        model.addAttribute("drivers", cityId == null ?
                driverService.getAllDrivers() : driverService.getAllDriversByCityId(cityId));
        model.addAttribute("todayBookings",
                adminService.getBookings(cityId, null, today, today));
    }

    /**
     * Build a map of fields to be updated for User.
     */
    private Map<String, Object> buildUserUpdates(UserDTO dto) {
        log.debug("Building user updates map for userDTO: {}", dto);
        Map<String, Object> updates = new HashMap<>();
        updates.put("displayName", dto.getDisplayName());
        updates.put("mobileNumber", dto.getMobileNumber());
        if (dto.getStatus() != null) updates.put("status", dto.getStatus());
        if (dto.getCity() != null) updates.put("cityId", dto.getCity().getCityId());
        return updates;
    }

    /**
     * Build a map of fields to be updated for Driver.
     */
    private Map<String, Object> buildDriverUpdates(DriverDTO dto) {
        log.debug("Building driver updates map for driverDTO: {}", dto);
        Map<String, Object> updates = new HashMap<>();
        updates.put("displayName", dto.getDisplayName());
        updates.put("mobileNumber", dto.getMobileNumber());
        updates.put("licenseNumber", dto.getLicenseNumber());
        updates.put("cabType", dto.getCabType());
        if (dto.getStatus() != null) updates.put("status", dto.getStatus());
        if (dto.getCity() != null) updates.put("cityId", dto.getCity().getCityId());
        return updates;
    }

    /**
     * Redirect URL helper to monitor page of admin.
     */
    private String redirectMonitor(Long adminId) {
        log.debug("Redirecting to monitor page | adminId: {}", adminId);
        return "redirect:/admin/" + adminId + "/monitor";
    }
}
