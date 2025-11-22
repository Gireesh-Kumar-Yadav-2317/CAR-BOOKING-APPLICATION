package com.cabbooking.service;

import com.cabbooking.dto.SigninRequest;

import com.cabbooking.dto.UserDTO;
import com.cabbooking.dto.UserSignupRequest;


public interface IUserService {

    UserDTO signup(UserSignupRequest request);
    boolean signin(SigninRequest request);
}
