package com.cabbooking.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor@NoArgsConstructor
@Builder
public class FareEstRequest {
    private CityDTO city;
    private Double baseFare;
    private Double perKmRate;

}

