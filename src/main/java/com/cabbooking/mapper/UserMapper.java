package com.cabbooking.mapper;

import com.cabbooking.dto.UserDTO;

import com.cabbooking.dto.UserSignupRequest;
import com.cabbooking.entity.CityEntity;
import com.cabbooking.entity.UserEntity;
import com.cabbooking.entity.UserStatus;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final CityMapper cityMapper;

    public UserMapper(CityMapper cityMapper) {
        this.cityMapper = cityMapper;
    }

    /**
     * Convert UserEntity → UserDTO
     * Maps the associated City entity to CityDTO using CityMapper
     */
    public  UserDTO toDTO(UserEntity user) {
        if (user == null) return null;

        return UserDTO.builder()
                .userId(user.getUserId())
                .displayName(user.getDisplayName())
                .username(user.getUsername())
                .mobileNumber(user.getMobileNumber())
                .status(user.getStatus())
                .city(cityMapper.toDTO(user.getCity())) // map City entity to CityDTO
                .build();
    }

    /**
     * Convert UserDTO → UserEntity
     * Accepts a City entity (already fetched) to set the relationship
     */
    public  UserEntity toEntity(UserDTO dto, CityEntity city) {
        if (dto == null) return null;

        return UserEntity.builder()
                .userId(dto.getUserId())
                .displayName(dto.getDisplayName())
                .username(dto.getUsername())
                .mobileNumber(dto.getMobileNumber())
                .status(dto.getStatus() != null ? dto.getStatus() : UserStatus.ACTIVE)
                .city(city)
                .build();
    }

    /**
     * Update an existing UserEntity with values from UserDTO
     * Only updates non-null fields from DTO
     */
    public  void updateEntity(UserEntity user, UserDTO dto, CityEntity city) {
        if (dto.getDisplayName() != null) user.setDisplayName(dto.getDisplayName());
        if (dto.getMobileNumber() != null) user.setMobileNumber(dto.getMobileNumber());
        if (dto.getStatus() != null) user.setStatus(dto.getStatus());
        if (city != null) user.setCity(city);
    }

    /**
     * Map SignupRequest → UserEntity
     * Used during user signup; password can be encoded in service
     */
    public  UserEntity fromSignupRequest(UserSignupRequest req, CityEntity city) {
        return UserEntity.builder()
                .username(req.getUsername())
                .password(req.getPassword()) // encode outside mapper
                .displayName(req.getDisplayName())
                .mobileNumber(req.getMobileNumber())
                .status(req.getStatus() != null ? UserStatus.valueOf(String.valueOf(req.getStatus())) : UserStatus.ACTIVE)
                .city(city)
                .build();
    }

}
