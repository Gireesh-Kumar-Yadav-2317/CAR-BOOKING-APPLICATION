package com.cabbooking.service;

import com.cabbooking.dto.DriverDTO;
import com.cabbooking.dto.DriverSignupRequest;
import com.cabbooking.dto.SigninRequest;
import com.cabbooking.entity.DriverEntity;


public interface IDriverService {

    DriverDTO signup(DriverSignupRequest request);
    boolean signin(SigninRequest request);

    void updateDriver(DriverEntity driver);
}
