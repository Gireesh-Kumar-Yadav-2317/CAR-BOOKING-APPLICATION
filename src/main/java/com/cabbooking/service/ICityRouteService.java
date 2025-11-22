package com.cabbooking.service;

import com.cabbooking.dto.CityRouteDTO;
import com.cabbooking.dto.CityRouteRequest;
import com.cabbooking.entity.CityRouteEntity;

import java.util.List;

public interface ICityRouteService {

    CityRouteDTO addRoute(CityRouteRequest request);

    List<CityRouteDTO> getRoutesByCity(Long cityId);

    CityRouteDTO getRouteById(Long routeId);

    //  GET ROUTE BY PICKUP & DROP
    CityRouteEntity getRouteEntityByPickupAndDrop(Long cityId, String pickup, String drop);

    //  UPDATE ROUTE
    CityRouteDTO updateRoute(Long routeId, CityRouteRequest request);

    //  DELETE ROUTE
    void deleteRoute(Long routeId);


}
