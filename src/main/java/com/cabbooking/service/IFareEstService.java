package com.cabbooking.service;

import com.cabbooking.dto.FareEstRequest;
import com.cabbooking.dto.FareEstResponse;
import com.cabbooking.entity.CityRouteEntity;

import java.util.List;

public interface IFareEstService {

    FareEstResponse addFareEst(FareEstRequest request);
    FareEstResponse getFareEstByCity(Long cityId);
    List<FareEstResponse> getAllFareEst();

    double calculateFare(CityRouteEntity route, FareEstResponse fareEst);
}
