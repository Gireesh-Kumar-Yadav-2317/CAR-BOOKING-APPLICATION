package com.cabbooking.controller;

import com.cabbooking.dto.CityRouteDTO;
import com.cabbooking.dto.CityRouteRequest;
import com.cabbooking.entity.CityRouteEntity;
import com.cabbooking.service.CityRouteServiceImpl1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing City Routes.
 * Provides APIs to add, search and retrieve routes within a city.
 */
@Slf4j
@RestController
@RequestMapping("/api/city-routes")
@RequiredArgsConstructor
public class CityRouteController1 {

    private final CityRouteServiceImpl1 cityRouteService;

    /**
     * Create a new route inside a city.
     *
     * @param request CityRouteRequest containing cityId, pickup point, and drop point.
     * @return CityRouteDTO with newly created route details.
     */
    @PostMapping
    public ResponseEntity<CityRouteDTO> addRoute(@RequestBody CityRouteRequest request) {

        CityRouteDTO response = cityRouteService.addRoute(request);
        log.info("Route created successfully with ID: {}", response.getRouteId());
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieve all routes available in a specific city.
     *
     * @param cityId ID of the city for which routes are required.
     * @return List of CityRouteDTO objects representing available routes.
     */
    @GetMapping("/city/{cityId}")
    public ResponseEntity<List<CityRouteDTO>> getRoutesByCity(@PathVariable Long cityId) {
        log.info("Fetching all routes for CityID: {}", cityId);
        List<CityRouteDTO> responses = cityRouteService.getRoutesByCity(cityId);
        log.info("Total routes found for CityID {}: {}", cityId, responses.size());
        return ResponseEntity.ok(responses);
    }

    /**
     * Retrieve details of a single route by its unique ID.
     *
     * @param routeId Unique route identifier.
     * @return CityRouteDTO with route details.
     */
    @GetMapping("/{routeId}")
    public ResponseEntity<CityRouteDTO> getRouteById(@PathVariable Long routeId) {
        log.info("Fetching route details for RouteID: {}", routeId);
        CityRouteDTO response = cityRouteService.getRouteById(routeId);
        log.info("Route fetched successfully for RouteID: {}", routeId);
        return ResponseEntity.ok(response);
    }

    /**
     * Search for a specific route inside a city using pickup and drop locations.
     *
     * @param cityId ID of the city.
     * @param pickup Pickup location name.
     * @param drop   Drop location name.
     * @return CityRouteEntity containing the matching route details.
     */
    @GetMapping("/search")
    public ResponseEntity<CityRouteEntity> getRouteByPickupAndDrop(
            @RequestParam Long cityId,
            @RequestParam String pickup,
            @RequestParam String drop) {
        log.info("Searching route for CityID: {}, Pickup: '{}', Drop: '{}'", cityId, pickup, drop);
        CityRouteEntity response = cityRouteService.getRouteEntityByPickupAndDrop(cityId, pickup, drop);
        log.info("Route search completed for CityID: {}, Pickup: '{}', Drop: '{}'", cityId, pickup, drop);
        return ResponseEntity.ok(response);
    }

}
