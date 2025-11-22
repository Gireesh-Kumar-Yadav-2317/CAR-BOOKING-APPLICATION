package com.cabbooking.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingRequest {
    private Long userId;
    private Long cityId;
    private Long routeId;
    private String pickupLocation;
    private  String dropLocation;
}

