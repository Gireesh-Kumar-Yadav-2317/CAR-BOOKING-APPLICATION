package com.cabbooking.dto;

import com.cabbooking.entity.UserStatus;
import lombok.*;

@Data

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignupRequest {
    private String username;
    private String password;
    private String displayName;
    private String mobileNumber;
    private UserStatus status;
    private CityDTO city;
}

