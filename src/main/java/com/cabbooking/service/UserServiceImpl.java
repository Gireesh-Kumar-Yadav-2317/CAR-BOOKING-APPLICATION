package com.cabbooking.service;

import com.cabbooking.dto.SigninRequest;
import com.cabbooking.dto.UserDTO;
import com.cabbooking.dto.UserSignupRequest;
import com.cabbooking.entity.CityEntity;
import com.cabbooking.entity.UserEntity;
import com.cabbooking.mapper.UserMapper;
import com.cabbooking.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing User operations like signup, signin, and fetching users.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final CityServiceImpl1 cityService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    //  Public API  //

    /**
     * Signup a new user
     */
    public UserDTO signup( UserSignupRequest request) {
        log.info("Signing up user: {}", request.getUsername());

        validateUsername(request.getUsername());

        CityEntity city = fetchCity(request.getCity().getCityId());

        UserEntity user = userMapper.fromSignupRequest(request, city);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        UserEntity savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getUserId());

        return userMapper.toDTO(savedUser);
    }

    /**
     * Signin user
     */
    public boolean signin(SigninRequest request) {
        log.info("Signin attempt for username: {}", request.getUsername());

        return userRepository.findByUsername(request.getUsername())
                .map(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .orElse(false);
    }

    /**
     * Fetch user by ID
     */
    public UserEntity getUserById(Long userId) {
        log.info("Fetching user by ID: {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    /**
     * Fetch user by ID and return DTO
     */
    public UserDTO getUserDTOById(Long userId) {
        return mapToDTO(getUserById(userId));
    }

    /**
     * Fetch all users
     */
    public List<UserEntity> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

    /**
     * Fetch all users in a specific city
     */
    public List<UserEntity> getAllUsersByCityId(Long cityId) {
        log.info("Fetching all users for cityId: {}", cityId);
        if (cityId == null) throw new RuntimeException("City ID cannot be null");

        return userRepository.findUserByCityId(cityId);
    }

    /**
     * Count users in a city
     */
    public Long countUsersByCityId(Long cityId) {
        return userRepository.countUsersByCityId(cityId);
    }

    /**
     * Count all users in the system
     */
    public Long countAllUsers() {
        return userRepository.count();
    }

    /**
     * Save or update user entity
     */
    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }

    /**
     * Delete user by ID
     */
    public void deleteUserById(Long userId) {
        log.info("Deleting user with ID: {}", userId);
        userRepository.deleteById(userId);
    }

    /**
     * Find user by username
     */
    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Convert UserEntity to UserDTO
     */
    public UserDTO mapToDTO(UserEntity user) {
        return userMapper.toDTO(user);
    }

    //  Private Helpers ///

    /** Validate that username is not null/blank and unique */
    private void validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new RuntimeException("Username cannot be null or empty");
        }
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("User already exists with username: " + username);
        }
    }

    /** Fetch city safely by ID */
    private CityEntity fetchCity(Long cityId) {
        return cityService.getCityById(cityId);
    }
}
