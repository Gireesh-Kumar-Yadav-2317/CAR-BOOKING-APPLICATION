package com.cabbooking.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CityRouteDTO {
    private Long routeId;
    private CityDTO city;
    private String pickupLocation;
    private String dropLocation;
    private Double distanceKm;


}
