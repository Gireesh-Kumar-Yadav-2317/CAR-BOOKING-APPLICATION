package com.cabbooking.dto;

import com.cabbooking.entity.UserStatus;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long userId;
    private String displayName;
    private String mobileNumber;
    private String username;
    private CityDTO city;       // Replace cityId + cityName with CityDTO
    private UserStatus status;

    // Optional constructor for minimal info
    public UserDTO(Long userId, String displayName, String username) {
        this.userId = userId;
        this.displayName = displayName;
        this.username = username;
    }
}


