/*
package com.cabbooking.controller;

import com.cabbooking.dto.*;
import com.cabbooking.entity.BookingStatus;
import com.cabbooking.entity.DriverStatus;
import com.cabbooking.entity.UserStatus;
import com.cabbooking.service.AdminServiceImpl;
import com.cabbooking.service.CityServiceImpl;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminServiceImpl adminService;
    private final UserServiceImpl userService;
    private final DriverServiceImpl driverService;
    private final CityServiceImpl cityService;

    // ---------------- ADMIN HOME ----------------
    @GetMapping("/{adminId}/home")
    public String adminHome(@PathVariable Long adminId,
                            @RequestParam(value = "cityId", required = false) Long cityId,
                            Model model) {

        log.info("Loading admin home for adminId: {}, cityId: {}", adminId, cityId);
        model.addAttribute("adminId", adminId);
        model.addAttribute("cities", cityService.getAllCities());
        model.addAttribute("selectedCityId", cityId);

        model.addAttribute("totalUsers", adminService.countUser(cityId));
        model.addAttribute("totalDrivers", adminService.countDriver(cityId));
        model.addAttribute("totalRides", adminService.countRides(cityId));

        model.addAttribute("users", adminService.getUsersByCity(cityId));
        model.addAttribute("drivers", adminService.getDriversByCity(cityId));

        return "admin-home";
    }

    // ---------------- VIEW BOOKINGS ----------------
    @GetMapping("/{adminId}/bookings")
    public String viewBookings(@PathVariable Long adminId,
                               @RequestParam(value = "cityId", required = false) Long cityId,
                               @RequestParam(value = "status", required = false) String status,
                               @RequestParam(value = "startDate", required = false)
                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                               @RequestParam(value = "endDate", required = false)
                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                               Model model) {

        log.info("Fetching bookings for adminId: {}, cityId: {}, status: {}, startDate: {}, endDate: {}",
                adminId, cityId, status, startDate, endDate);

        // Only apply filter if at least one filter is selected
        boolean filterApplied = cityId != null || (status != null && !status.isEmpty())
                || startDate != null || endDate != null;

        List<BookingResponse> bookings = new ArrayList<>();
        if (filterApplied) {
            bookings = adminService.getBookings(cityId, status, startDate, endDate);
        }

        model.addAttribute("adminId", adminId);
        model.addAttribute("bookings", bookings);
        model.addAttribute("filterApplied", filterApplied);
        model.addAttribute("cities", cityService.getAllCities());
        model.addAttribute("selectedCityId", cityId);
        model.addAttribute("bookingStatuses", BookingStatus.values());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("startDate", startDate != null ? startDate.toString() : "");
        model.addAttribute("endDate", endDate != null ? endDate.toString() : "");

        return "admin-bookings";
    }


    //  MONITOR SYSTEM
    @GetMapping("/{adminId}/monitor")
    public String monitorSystem(@PathVariable Long adminId,
                                @RequestParam(value = "cityId", required = false) Long cityId,
                                Model model) {

        log.info("Monitoring system for adminId: {}, cityId: {}", adminId, cityId);

        LocalDate today = LocalDate.now();

        model.addAttribute("adminId", adminId);
        model.addAttribute("cities", cityService.getAllCities());
        model.addAttribute("selectedCityId", cityId);
        model.addAttribute("userStatuses", UserStatus.values());
        model.addAttribute("driverStatuses", DriverStatus.values());

        if (cityId == null) {
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("drivers", driverService.getAllDrivers());
            model.addAttribute("todayBookings", adminService.getBookings(null, null, today, today));
        } else {
            model.addAttribute("users", userService.getAllUsersByCityId(cityId));
            model.addAttribute("drivers", driverService.getAllDriversByCityId(cityId));
            model.addAttribute("todayBookings", adminService.getBookings(cityId, null, today, today));
        }

        return "admin-monitor";
    }

    //  USER MANAGEMENT
    @GetMapping("/user/{userId}/update")
    public String userUpdatePage(@PathVariable Long userId,
                                 @RequestParam Long adminId,
                                 Model model) {

        UserDTO user = userService.getUserDTOById(userId);
        model.addAttribute("adminId", adminId);
        model.addAttribute("user", user);
        model.addAttribute("userStatuses", UserStatus.values());
        model.addAttribute("cities", cityService.getAllCities());

        return "user-update";
    }

    // ---------- Update User (PUT) ----------
    @PutMapping("/user/{userId}")
    public String updateUser(@PathVariable Long userId,
                             @ModelAttribute UserDTO userDTO,
                             @RequestParam Long adminId,
                             RedirectAttributes redirectAttributes) {

        Map<String, Object> updates = new HashMap<>();
        updates.put("displayName", userDTO.getDisplayName());
        updates.put("mobileNumber", userDTO.getMobileNumber());
        if (userDTO.getStatus() != null) updates.put("status", userDTO.getStatus());
        if (userDTO.getCity() != null) updates.put("cityId", userDTO.getCity().getCityId());

        adminService.updateUserPartial(userId, updates);

        redirectAttributes.addFlashAttribute("message", "User updated successfully!");
        return "redirect:/admin/" + adminId + "/monitor";
    }

    // ---------- Delete User (DELETE) ----------
    @DeleteMapping("/user/{userId}/delete")
    public String deleteUser(@PathVariable Long userId,
                             @RequestParam Long adminId,
                             RedirectAttributes redirectAttributes) {

        adminService.deleteUser(userId);
        redirectAttributes.addFlashAttribute("message", "User deleted successfully!");
        return "redirect:/admin/" + adminId + "/monitor";
    }

    @GetMapping("/driver/{driverId}/update")
    public String driverUpdatePage(@PathVariable Long driverId,
                                   @RequestParam Long adminId,
                                   Model model) {
        log.info("Loading update page for driverId: {}", driverId);
        DriverDTO driver = driverService.getDriverDTOById(driverId);

        model.addAttribute("adminId", adminId);
        model.addAttribute("driver", driver);
        model.addAttribute("cities", cityService.getAllCities());
        model.addAttribute("driverStatuses", DriverStatus.values());
        return "driver-update";
    }

    // Update driver (PUT)
    @PutMapping("/driver/{driverId}")
    public String updateDriver(@PathVariable Long driverId,
                               @ModelAttribute DriverDTO driverDTO,
                               @RequestParam Long adminId,
                               RedirectAttributes redirectAttributes) {

        log.info("Updating driverId: {}", driverId);
        Map<String, Object> updates = new HashMap<>();
        updates.put("displayName", driverDTO.getDisplayName());
        updates.put("mobileNumber", driverDTO.getMobileNumber());
        updates.put("licenseNumber", driverDTO.getLicenseNumber());
        updates.put("cabType", driverDTO.getCabType());

        if (driverDTO.getStatus() != null) updates.put("status", driverDTO.getStatus());
        if (driverDTO.getCity() != null) updates.put("cityId", driverDTO.getCity().getCityId());

        adminService.updateDriverPartial(driverId, updates);
        redirectAttributes.addFlashAttribute("message", "Driver updated successfully!");
        return "redirect:/admin/" + adminId + "/monitor";
    }

    // Delete driver (DELETE)
    @DeleteMapping("/driver/{driverId}")
    public String deleteDriver(@PathVariable Long driverId,
                               @RequestParam Long adminId,
                               RedirectAttributes redirectAttributes) {

        log.info("Deleting driverId: {}", driverId);
        adminService.deleteDriver(driverId);
        redirectAttributes.addFlashAttribute("message", "Driver deleted successfully!");
        return "redirect:/admin/" + adminId + "/monitor";
    }

}
*/
