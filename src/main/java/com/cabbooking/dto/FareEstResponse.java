package com.cabbooking.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FareEstResponse {
    private Long fareId;
    private CityDTO city;
    private Double baseFare;
    private Double perKmRate;
}
