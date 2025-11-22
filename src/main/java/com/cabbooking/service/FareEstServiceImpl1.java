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

/**
 * Service that manages fare-estimation logic.
 * Handles CRUD operations and total fare calculation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FareEstServiceImpl1 implements IFareEstService {

    private final FareEstRepository fareEstRepository;
    private final FareEstMapper fareEstMapper;
    private final CityServiceImpl1 cityService;

    //  Public API //

    /** Add a new fare estimate for a city */
    @Override
    public FareEstResponse addFareEst(FareEstRequest request) {
        log.info("Adding fare estimate for cityId: {}", request.getCity().getCityId());
        CityEntity city = fetchCity(request.getCity().getCityId());
        FareEntity saved = fareEstRepository.save(buildFareEntity(request, city));
        log.info("Fare estimate saved with ID: {}", saved.getFareId());
        return fareEstMapper.toResponse(saved);
    }

    /** Fetch fare estimate by cityId */
    @Override
    public FareEstResponse getFareEstByCity(Long cityId) {
        log.info("Fetching fare estimate for cityId: {}", cityId);
        FareEntity fare = fareEstRepository.findByCity_CityId(cityId)
                .orElseThrow(() -> new RuntimeException("Fare not found for cityId: " + cityId));
        log.info("Fare found: ID {}, BaseFare {}, PerKmRate {}",
                fare.getFareId(), fare.getBaseFare(), fare.getPerKmRate());
        return fareEstMapper.toResponse(fare);
    }

    /** Fetch all fare estimates */
    @Override
    public List<FareEstResponse> getAllFareEst() {
        log.info("Fetching all fare estimates");
        List<FareEstResponse> fares = fareEstRepository.findAll()
                .stream().map(fareEstMapper::toResponse).toList();
        log.info("Total fare estimates found: {}", fares.size());
        return fares;
    }

    /**
     * Calculate total fare using base fare and per-km rate
     * @param route  city route
     * @param fareEst fare estimate of the city
     */
    @Override
    public double calculateFare(CityRouteEntity route, FareEstResponse fareEst) {
        validateInputs(route, fareEst);
        double fare = fareEst.getBaseFare() + route.getDistanceKm() * fareEst.getPerKmRate();
        log.info("Fare calculated: BaseFare={} + Distance({} km)*PerKmRate({}) = {}",
                fareEst.getBaseFare(), route.getDistanceKm(), fareEst.getPerKmRate(), fare);
        return fare;
    }

    //  Private Helpers //

    /** Fetch CityEntity by ID with logging */
    private CityEntity fetchCity(Long cityId) {
        log.info("Fetching city by ID: {}", cityId);
        return cityService.getCityById(cityId);
    }

    /** Build a FareEntity from request and city */
    private FareEntity buildFareEntity(FareEstRequest request, CityEntity city) {
        return FareEntity.builder()
                .city(city)
                .baseFare(request.getBaseFare())
                .perKmRate(request.getPerKmRate())
                .build();
    }

    /** Validate inputs for fare calculation */
    private void validateInputs(CityRouteEntity route, FareEstResponse fareEst) {
        if (route == null) throw new RuntimeException("Route cannot be null");
        if (fareEst == null) throw new RuntimeException("FareEst cannot be null");
    }
}
