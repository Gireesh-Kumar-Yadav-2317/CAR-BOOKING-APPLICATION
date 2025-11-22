/*
package com.cabbooking.service;

import com.cabbooking.dto.FareEstRequest;
import com.cabbooking.dto.FareEstResponse;

import com.cabbooking.entity.CityEntity;

import com.cabbooking.entity.CityRouteEntity;
import com.cabbooking.entity.FareEntity;
import com.cabbooking.mapper.FareEstMapper;
import com.cabbooking.repo.FareEstRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FareEstServiceImpl implements  IFareEstService {

    private final FareEstRepository fareEstRepository;
    private final FareEstMapper fareEstMapper;
    private final CityServiceImpl1 cityService; // Reuse CityService for fetching city details

    */
/**
     * Add a new fare estimate for a city
     *//*

    public FareEstResponse addFareEst(FareEstRequest request) {
        log.info("Adding fare estimate for cityId: {}", request.getCity().getCityId());

        // Fetch city entity using CityService
        CityEntity city = cityService.getCityById(request.getCity().getCityId());
        log.info("City found: {} (ID: {})", city.getCityName(), city.getCityId());

        // Build FareEntity
        FareEntity fare = FareEntity.builder()
                .city(city)
                .baseFare(request.getBaseFare())
                .perKmRate(request.getPerKmRate())
                .build();

        // Save to repository
        FareEntity saved = fareEstRepository.save(fare);
        log.info("Fare estimate saved with ID: {}", saved.getFareId());

        // Map to DTO using FareEstMapper
        return fareEstMapper.toResponse(saved);
    }

    */
/**
     * Fetch fare estimate for a specific city by cityId
     *//*

    public FareEstResponse getFareEstByCity(Long cityId) {
        log.info("Fetching fare estimate for cityId: {}", cityId);

        FareEntity fare = fareEstRepository.findByCity_CityId(cityId)
                .orElseThrow(() -> {
                    log.error("Fare estimate not found for cityId: {}", cityId);
                    return new RuntimeException("Fare not found for city");
                });

        log.info("Fare found: ID {}, BaseFare {}, PerKmRate {}",
                fare.getFareId(), fare.getBaseFare(), fare.getPerKmRate());

        return fareEstMapper.toResponse(fare);
    }

    */
/**
     * Fetch all fare estimates
     *//*

    public List<FareEstResponse> getAllFareEst() {
        log.info("Fetching all fare estimates");
        List<FareEstResponse> fares = fareEstRepository.findAll().stream()
                .map(fareEstMapper::toResponse)
                .toList();

        log.info("Total fare estimates found: {}", fares.size());
        return fares;
    }

    */
/**
     * Calculate total fare for a given route using FareEst rates
     * @param route the city route
     * @param fareEst the fare estimate for the city
     * @return calculated fare
     *//*

    public double calculateFare(CityRouteEntity route, FareEstResponse fareEst) {
        if (route == null) {
            throw new RuntimeException("Route cannot be null");
        }
        if (fareEst == null) {
            throw new RuntimeException("FareEst cannot be null");
        }

        double fare = fareEst.getBaseFare() + (route.getDistanceKm() * fareEst.getPerKmRate());
        log.info("Fare calculated: BaseFare={} + Distance({} km) * PerKmRate({}) = {}",
                fareEst.getBaseFare(), route.getDistanceKm(), fareEst.getPerKmRate(), fare);

        return fare;
    }



}
*/
