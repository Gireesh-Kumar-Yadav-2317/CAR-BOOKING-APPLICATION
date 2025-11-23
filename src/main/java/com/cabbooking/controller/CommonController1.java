package com.cabbooking.controller;

import com.cabbooking.dto.*;
import com.cabbooking.entity.AdminEntity;
import com.cabbooking.entity.DriverEntity;
import com.cabbooking.entity.UserEntity;
import com.cabbooking.exception.CustomNotFoundException;
import com.cabbooking.mapper.AdminMapper;
import com.cabbooking.service.AdminServiceImpl1;
import com.cabbooking.service.CityServiceImpl1;
import com.cabbooking.service.DriverServiceImpl;
import com.cabbooking.service.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Handles common authentication & registration flows for:
 * <ul>
 *     <li>Users</li>
 *     <li>Drivers</li>
 *     <li>Admins</li>
 * </ul>
 * Provides Signup, Login and Logout endpoints.
 */
@Slf4j
@Controller
@RequestMapping("/common")
@RequiredArgsConstructor
public class CommonController1 {

    private final UserServiceImpl userService;
    private final DriverServiceImpl driverService;
    private final AdminServiceImpl1 adminService;
    private final CityServiceImpl1 cityService;
    private final AdminMapper adminMapper;

    // ====================== SIGNUP ======================

    /**
     * Display signup page with forms for Admin, Driver and User.
     */
    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        log.debug("Loading signup form...");
        model.addAttribute("adminRequest", new AdminSignupRequest());
        model.addAttribute("driverRequest", new DriverSignupRequest());
        model.addAttribute("userRequest", new UserSignupRequest());
        model.addAttribute("cities", cityService.getAllCities());
        return "signup";
    }

    /**
     * Handle signup submission based on selected role.
     * After signup, redirects user to login page.
     */
    @PostMapping("/signup")
    public String signup(@RequestParam String role,
                         @ModelAttribute AdminSignupRequest adminRequest,
                         @ModelAttribute DriverSignupRequest driverRequest,
                         @ModelAttribute UserSignupRequest userRequest,
                         RedirectAttributes redirectAttributes) {
        performSignup(role, adminRequest, driverRequest, userRequest);
        log.info("Signup successful for role: {}", role);
        redirectAttributes.addFlashAttribute("success", "Signup successful! Please login.");
        return "redirect:/common/login";
    }

    /**
     * Internal helper: Performs signup for the given role.
     */
    private void performSignup(String role,
                               AdminSignupRequest adminRequest,
                               DriverSignupRequest driverRequest,
                               UserSignupRequest userRequest) {
        String normalizedRole = role.trim().toUpperCase();

        switch (normalizedRole) {
            case "ADMIN":
                adminService.signup(adminRequest);
                log.info("New Admin registered: {}", adminRequest.getUsername());
                break;
            case "DRIVER":
                driverService.signup(driverRequest);
                log.info("New Driver registered: {}", driverRequest.getUsername());
                break;
            case "USER":
            default:
                userService.signup(userRequest);
                log.info("New User registered: {}", userRequest.getUsername());
                break;
        }
    }

    // ====================== LOGIN ======================

    /**
     * Display login page.
     */
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        log.debug("Loading login form...");
        model.addAttribute("loginRequest", new SigninRequest());
        return "login";
    }

    /**
     * Handle login for Admin, Driver, or User based on credentials.
     * Stores logged-in entity DTO in HTTP session and redirects to their dashboard.
     */
    @PostMapping("/login")
    public String login(@ModelAttribute SigninRequest loginRequest,
                        Model model,
                        HttpSession session) {
        String redirectUrl = performLogin(loginRequest, session);
        if (redirectUrl != null) {
            log.info("Login successful for username: {}", loginRequest.getUsername());
            return "redirect:" + redirectUrl;
        }
        log.warn("Invalid login attempt for username: {}", loginRequest.getUsername());
        model.addAttribute("error", "Invalid username or password");
        return "login";
    }

    /**
     * Internal helper: Validates credentials and sets session attributes
     * depending on the role.
     *
     * @return redirect path if successful, otherwise null.
     */
    private String performLogin(SigninRequest loginRequest, HttpSession session) {
        String username = loginRequest.getUsername().trim();

        // User Login
        if (userService.signin(loginRequest)) {
            UserEntity user = userService.findByUsername(username)
                    .orElseThrow(() -> new CustomNotFoundException("User not found"));
            session.setAttribute("loggedInUser", userService.mapToDTO(user));
            log.info("User '{}' logged in successfully", username);
            return "/users/" + user.getUserId() + "/home";
        }

        // Driver Login
        if (driverService.signin(loginRequest)) {
            DriverEntity driver = driverService.findByUsername(username)
                    .orElseThrow(() -> new CustomNotFoundException("Driver not found"));
            session.setAttribute("loggedInDriver", driverService.mapToDTO(driver));
            log.info("Driver '{}' logged in successfully", username);
            return "/driver/" + driver.getDriverId() + "/home";
        }

        // Admin Login
        if (adminService.signin(loginRequest)) {
            AdminEntity admin = adminService.findByUsername(username)
                    .orElseThrow(() -> new CustomNotFoundException("Admin not found"));
            session.setAttribute("loggedInAdmin", adminMapper.mapToDTO(admin));
            log.info("Admin '{}' logged in successfully", username);
            return "/admin/" + admin.getAdminId() + "/home";
        }

        // Login failed for all roles
        return null;
    }



    /**
     * Invalidate current session and redirect to login page.
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        log.info("Logging out current session...");
        session.invalidate();
        return "redirect:/common/login";
    }

    /**
     * Default entry point: redirect to login.
     */
    @GetMapping("/")
    public String defaultPage() {
        log.debug("Default request received - redirecting to login");
        return "redirect:/common/login";
    }



}
