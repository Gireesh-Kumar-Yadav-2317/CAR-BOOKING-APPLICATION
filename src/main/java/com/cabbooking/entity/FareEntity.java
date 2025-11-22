package com.cabbooking.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Fare_calculation")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FareEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fare_id")
    private Long fareId;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private CityEntity city;   // FK mapping with City table

    @Column(name = "base_fare", nullable = false)
    private Double baseFare;

    @Column(name = "per_km_rate", nullable = false)
    private Double perKmRate;
}