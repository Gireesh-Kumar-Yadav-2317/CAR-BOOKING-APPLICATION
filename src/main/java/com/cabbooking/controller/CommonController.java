/*
package com.cabbooking.controller;

import com.cabbooking.dto.*;

import com.cabbooking.entity.AdminEntity;
import com.cabbooking.entity.DriverEntity;
import com.cabbooking.entity.UserEntity;
import com.cabbooking.exception.CustomNotFoundException;
import com.cabbooking.mapper.AdminMapper;
import com.cabbooking.service.AdminServiceImpl;
import com.cabbooking.service.CityServiceImpl;
import com.cabbooking.service.DriverServiceImpl;
import com.cabbooking.service.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

*/
/**
 * CommonController handles signup, login, and logout for Users, Drivers, and Admins.
 * Refactored for reusability and clean structure.
 *//*

@Controller
@RequestMapping("/common")
@RequiredArgsConstructor
@Slf4j
public class CommonController {

    private final UserServiceImpl userService;
    private final DriverServiceImpl driverService;
    private final AdminServiceImpl adminService;
    private  final CityServiceImpl cityService;
    private final AdminMapper adminMapper;

    // SIGNUP

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("adminRequest", new AdminSignupRequest());
        model.addAttribute("driverRequest", new DriverSignupRequest());
        model.addAttribute("userRequest", new UserSignupRequest());
        model.addAttribute("cities", cityService.getAllCities());

        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String role,
                         @ModelAttribute AdminSignupRequest adminRequest,
                         @ModelAttribute DriverSignupRequest driverRequest,
                         @ModelAttribute UserSignupRequest userRequest,
                         Model model) {
        // Call helper method for signup
        performSignup(role, adminRequest, driverRequest, userRequest);
        model.addAttribute("success", "Signup successful! Please login.");
        return "redirect:/common/login";
    }

    */
/**
     * Reusable helper method for role-based signup.
     *//*

    private void performSignup(String role,
                               AdminSignupRequest adminRequest,
                               DriverSignupRequest driverRequest,
                               UserSignupRequest userRequest) {
        String normalizedRole = role.trim().toUpperCase();

        switch (normalizedRole) {
            case "ADMIN":
                adminService.signup(adminRequest);
                log.info("Admin signed up successfully: {}", adminRequest.getUsername());
                break;
            case "DRIVER":
                driverService.signup(driverRequest);
                log.info("Driver signed up successfully: {}", driverRequest.getUsername());
                break;
            case "USER":
            default:
                userService.signup(userRequest);
                log.info("User signed up successfully: {}", userRequest.getUsername());
                break;
        }
    }

    // ---------------- LOGIN ----------------

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginRequest", new SigninRequest());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute SigninRequest loginRequest, Model model, HttpSession session) {
        String redirectUrl = performLogin(loginRequest, session);
        if (redirectUrl != null) {
            return "redirect:" + redirectUrl;
        }
        model.addAttribute("error", "Invalid username or password");
        return "login";
    }

    */
/**
     * Reusable helper method for login.
     *//*

    private String performLogin(SigninRequest loginRequest, HttpSession session) {
        String username = loginRequest.getUsername().trim();

        //  USER LOGIN
        if (userService.signin(loginRequest)) {
            UserEntity user = userService.findByUsername(username)
                    .orElseThrow(() -> new CustomNotFoundException("User not found"));
            session.setAttribute("loggedInUser", userService.mapToDTO(user));
            log.info("User login successful: {}", username);
            return "/users/" + user.getUserId() + "/home";
        }

        //  DRIVER LOGIN
        if (driverService.signin(loginRequest)) {
            DriverEntity driver = driverService.findByUsername(username)
                    .orElseThrow(() -> new CustomNotFoundException("Driver not found"));
            session.setAttribute("loggedInDriver", driverService.mapToDTO(driver));
            log.info("Driver login successful: {}", username);
            return "/driver/" + driver.getDriverId() + "/home";
        }

        //  ADMIN LOGIN
        if (adminService.signin(loginRequest)) {
            AdminEntity admin = adminService.findByUsername(username)
                    .orElseThrow(() -> new CustomNotFoundException("Admin not found"));
            session.setAttribute("loggedInAdmin", adminMapper.mapToDTO(admin));
            log.info("Admin login successful: {}", username);
            return "/admin/" + admin.getAdminId() + "/home";
        }

        // Login failed
        return null;
    }

    // ---------------- LOGOUT ----------------

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        log.info("Logout request received");
        session.invalidate();
        return "redirect:/common/login";
    }

    @GetMapping("/")
    public String defaultPage() {
        // Redirect to login page by default
        return "redirect:/common/login";
    }

}
*/
