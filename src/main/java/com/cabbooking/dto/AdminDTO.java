package com.cabbooking.dto;

import com.cabbooking.entity.AdminStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminDTO {
    private Long adminId;
    private String username;
    private String displayName;
    private String mobileNumber;
    private AdminStatus status;
}
