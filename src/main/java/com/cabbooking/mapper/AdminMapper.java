package com.cabbooking.mapper;

import com.cabbooking.dto.AdminDTO;
import com.cabbooking.dto.AdminSignupRequest;

import com.cabbooking.entity.AdminEntity;
import com.cabbooking.entity.AdminStatus;
import org.springframework.stereotype.Component;

@Component
public class AdminMapper {

    /**
     * Convert SignupRequest → Admin entity
     */
    public AdminEntity toEntity(AdminSignupRequest request) {
        if (request == null) return null;

        return AdminEntity.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .displayName(request.getDisplayName())
                .mobileNumber(request.getMobileNumber())
                .status(parseStatus(request.getStatus()))
                .build();
    }

    /**
     * Convert Admin entity → AdminResponse DTO
     */
    public AdminDTO mapToDTO(AdminEntity admin) {
        if (admin == null) return null;

        return AdminDTO.builder()
                .adminId(admin.getAdminId())
                .username(admin.getUsername())
                .displayName(admin.getDisplayName())
                .mobileNumber(admin.getMobileNumber())
                .status(AdminStatus.valueOf(admin.getStatus().name()))
                .build();
    }

    /**
     * Update an existing Admin entity from an AdminResponse or similar DTO
     */
    public void updateEntity(AdminEntity admin, AdminDTO dto) {
        if (admin == null || dto == null) return;

        if (dto.getDisplayName() != null) {
            admin.setDisplayName(dto.getDisplayName());
        }
        if (dto.getMobileNumber() != null) {
            admin.setMobileNumber(dto.getMobileNumber());
        }
        if (dto.getStatus() != null) {
            admin.setStatus(parseStatus(String.valueOf(dto.getStatus())));
        }
    }

    /**
     * Safe enum conversion: defaults to ACTIVE if status is null or invalid.
     */
    private AdminStatus parseStatus(String status) {
        if (status == null) return AdminStatus.ACTIVE;
        try {
            return AdminStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            // log a warning if desired
            return AdminStatus.ACTIVE;
        }
    }
}
