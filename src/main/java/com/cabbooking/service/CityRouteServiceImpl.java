/*
package com.cabbooking.service;

import com.cabbooking.dto.CityRouteDTO;
import com.cabbooking.dto.CityRouteRequest;
import com.cabbooking.entity.CityRouteEntity;
import com.cabbooking.mapper.CityRouteMapper;
import com.cabbooking.repo.CityRouteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class CityRouteServiceImpl implements ICityRouteService {

    private final CityRouteRepository cityRouteRepository;
    private final CityServiceImpl cityService; // for fetching CityEntity
    private final CityRouteMapper cityRouteMapper; // now injected

    // ADD ROUTE
    @Override
    public CityRouteDTO addRoute(CityRouteRequest request) {
        log.info("Adding route: cityId={}, pickup={}, drop={}",
                request.getCity().getCityId(), request.getPickupLocation(), request.getDropLocation());

        var city = cityService.getCityById(request.getCity().getCityId());
        var route = cityRouteMapper.toEntity(request);
        route.setCity(city);

        var saved = cityRouteRepository.save(route);
        log.info("Route created with ID: {}", saved.getRouteId());

        return cityRouteMapper.toDTO(saved);
    }


    //  GET ROUTE BY ID
    @Override
    public CityRouteDTO getRouteById(Long routeId) {
        log.info("Fetching route by ID={}", routeId);

        var route = cityRouteRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found with ID: " + routeId));

        return cityRouteMapper.toDTO(route);
    }

    //  GET ROUTE BY PICKUP & DROP
    @Override
    public CityRouteEntity getRouteEntityByPickupAndDrop(Long cityId, String pickup, String drop) {
        log.info("Fetching route for cityId={}, pickup={}, drop={}", cityId, pickup, drop);

        var routes = cityRouteRepository.findUniqueRoute(pickup, drop, cityId);
        if (routes.isEmpty()) {
            throw new RuntimeException("Route not found for given pickup & drop");
        }

        return routes.get(0); // return the actual entity
    }


    //  UPDATE ROUTE
    @Override
    public CityRouteDTO updateRoute(Long routeId, CityRouteRequest request) {
        log.info("Updating route with ID={}", routeId);

        var route = cityRouteRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found with ID: " + routeId));

        cityRouteMapper.updateEntity(route, request);
        var updated = cityRouteRepository.save(route);

        return cityRouteMapper.toDTO(updated);
    }

    //  DELETE ROUTE
    @Override
    public void deleteRoute(Long routeId) {
        log.info("Deleting route with ID={}", routeId);

        if (!cityRouteRepository.existsById(routeId)) {
            throw new RuntimeException("Route not found with ID: " + routeId);
        }

        cityRouteRepository.deleteById(routeId);
        log.info("Route deleted successfully: {}", routeId);
    }

    @Override
    public List<CityRouteDTO> getRoutesByCity(Long cityId) {
        log.info("Fetching routes for cityId={}", cityId);

        // Fetch routes from repository
        List<CityRouteEntity> routes = cityRouteRepository.findRoutesByCityId(cityId);

        if (routes.isEmpty()) {
            log.warn("No routes found for cityId={}", cityId);
        }

        // Convert entities → DTOs using the mapper
        return routes.stream()
                .map(cityRouteMapper::toDTO)
                .collect(Collectors.toList());
    }

}
*/
