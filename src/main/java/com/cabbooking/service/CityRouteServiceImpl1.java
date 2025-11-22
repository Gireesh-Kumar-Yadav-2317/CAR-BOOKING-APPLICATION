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

/**
 * Service layer for managing City Routes.
 * Handles CRUD operations and mapping between DTOs & entities.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CityRouteServiceImpl1 implements ICityRouteService {

    private final CityRouteRepository cityRouteRepository;
    private final CityServiceImpl1 cityService;
    private final CityRouteMapper cityRouteMapper;

    /**
     * Add a new route for a specific city.
     */
    @Override
    public CityRouteDTO addRoute(CityRouteRequest request) {
        log.info("Adding route for cityId={}, pickup={}, drop={}",
                request.getCity().getCityId(), request.getPickupLocation(), request.getDropLocation());

        var city = cityService.getCityById(request.getCity().getCityId());
        var route = cityRouteMapper.toEntity(request);
        route.setCity(city);

        var saved = cityRouteRepository.save(route);
        log.info("Route created successfully with ID: {}", saved.getRouteId());
        return cityRouteMapper.toDTO(saved);
    }

    /**
     * Fetch a route by its unique ID.
     */
    @Override
    public CityRouteDTO getRouteById(Long routeId) {
        log.info("Fetching route by ID: {}", routeId);
        var route = cityRouteRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found with ID: " + routeId));
        return cityRouteMapper.toDTO(route);
    }

    /**
     * Fetch a specific route by city, pickup and drop locations.
     */
    @Override
    public CityRouteEntity getRouteEntityByPickupAndDrop(Long cityId, String pickup, String drop) {
        log.info("Fetching route for cityId={}, pickup={}, drop={}", cityId, pickup, drop);
        var routes = cityRouteRepository.findUniqueRoute(pickup, drop, cityId);

        if (routes.isEmpty()) {
            log.warn("No route found for cityId={}, pickup={}, drop={}", cityId, pickup, drop);
            throw new RuntimeException("Route not found for given pickup & drop");
        }
        return routes.get(0);
    }

    /**
     * Update an existing route.
     */
    @Override
    public CityRouteDTO updateRoute(Long routeId, CityRouteRequest request) {
        log.info("Updating route with ID: {}", routeId);
        var route = cityRouteRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found with ID: " + routeId));

        cityRouteMapper.updateEntity(route, request);
        var updated = cityRouteRepository.save(route);
        log.info("Route updated successfully: {}", routeId);
        return cityRouteMapper.toDTO(updated);
    }

    /**
     * Delete a route by its ID.
     */
    @Override
    public void deleteRoute(Long routeId) {
        log.info("Deleting route with ID: {}", routeId);
        if (!cityRouteRepository.existsById(routeId)) {
            log.warn("Attempted to delete non-existing route: {}", routeId);
            throw new RuntimeException("Route not found with ID: " + routeId);
        }
        cityRouteRepository.deleteById(routeId);
        log.info("Route deleted successfully: {}", routeId);
    }

    /**
     * Get all routes for a specific city.
     */
    @Override
    public List<CityRouteDTO> getRoutesByCity(Long cityId) {
        log.info("Fetching all routes for cityId: {}", cityId);
        var routes = cityRouteRepository.findRoutesByCityId(cityId);

        if (routes.isEmpty()) {
            log.warn("No routes found for cityId: {}", cityId);
        }

        return routes.stream()
                .map(cityRouteMapper::toDTO)
                .collect(Collectors.toList());
    }
}
