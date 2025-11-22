package com.cabbooking.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CityRouteRequest{
    private CityDTO city;
    private String pickupLocation;
    private String dropLocation;
    private Double distanceKm;

}
