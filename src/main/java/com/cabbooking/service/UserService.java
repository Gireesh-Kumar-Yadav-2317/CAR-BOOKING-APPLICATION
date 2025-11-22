/*
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final CityServiceImpl1 cityService;
    private final BCryptPasswordEncoder passwordEncoder;
    private  final UserMapper userMapper;

    */
/**
     * Signup a new user
     *//*

    public UserDTO signup(UserSignupRequest request) {
        log.info("Signing up user: {}", request.getUsername());

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            log.error("User already exists with username: {}", request.getUsername());
            throw new RuntimeException("User already exists with username: " + request.getUsername());
        }

        CityEntity city = cityService.getCityById(request.getCity().getCityId());


        UserEntity user = userMapper.fromSignupRequest(request, city);
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Encode password

        UserEntity savedUser = userRepository.save(user);
        log.info("User created with ID: {}", savedUser.getUserId());

        return userMapper.toDTO(savedUser); // Now includes CityDTO
    }

    */
/**
     * Signin user
     *//*

    public boolean signin(SigninRequest request) {
        log.info("Signin attempt for username: {}", request.getUsername());

        return userRepository.findByUsername(request.getUsername())
                .map(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .orElse(false);
    }

    */
/**
     * Find user by username
     *//*

    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    */
/**
     * Fetch all users as DTO list
     *//*

    public List<UserEntity> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll(); // returns raw entities
    }
    */
/**
     * Fetch user by ID
     *//*

    public UserEntity getUserById(Long userId) {
        log.info("Fetching user by ID: {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new RuntimeException("User not found with ID: " + userId);
                });
    }


    public UserDTO mapToDTO(UserEntity user) {
        return userMapper.toDTO(user);
    }

    public List<UserEntity> getAllUsersByCityId(Long cityId) {
        log.info("Fetching all users for cityId: {}", cityId);

        if (cityId == null) {
            throw new RuntimeException("City ID cannot be null");
        }

        return userRepository.findUserByCityId(cityId);// return empty list if no users found
    }

    public Long countUsersByCityId(Long cityId) {
        return userRepository.countUsersByCityId(cityId);
    }



    public Long countAllUsers() {
        return userRepository.count();
    }

    // Fetch a user by ID and return DTO
    public UserDTO getUserDTOById(Long userId) {
        UserEntity user = getUserById(userId); // existing method returning entity
        return mapToDTO(user);                // convert entity → DTO
    }

    public UserEntity saveUser(UserEntity user) {
        // Optional: add validations or extra logic here
        return userRepository.save(user);
    }


    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }
}
*/
