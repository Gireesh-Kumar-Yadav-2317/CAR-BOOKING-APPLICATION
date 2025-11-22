package com.cabbooking.controller;

import com.cabbooking.entity.CityEntity;
import com.cabbooking.service.CityServiceImpl1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing City entities.
 * Provides CRUD operations to create, retrieve, update and delete cities.
 */
@Slf4j
@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController1 {

    private final CityServiceImpl1 cityService;

    /**
     * Create a new city.
     *
     * @param city CityEntity payload from request body.
     * @return ResponseEntity containing saved CityEntity and HTTP status CREATED.
     */
    @PostMapping
    public ResponseEntity<CityEntity> createCity(@RequestBody CityEntity city) {
        log.info("Request received to create city: {}", city.getCityName());
        CityEntity savedCity = cityService.createCity(city);
        log.info("City created successfully with ID: {}", savedCity.getCityId());
        return new ResponseEntity<>(savedCity, HttpStatus.CREATED);
    }

    /**
     * Fetch all available cities.
     *
     * @return List of CityEntity objects and HTTP status OK.
     */
    @GetMapping
    public ResponseEntity<List<CityEntity>> getAllCities() {
        log.info("Fetching all cities");
        List<CityEntity> cities = cityService.getAllCities();
        log.info("Total cities fetched: {}", cities.size());
        return ResponseEntity.ok(cities);
    }

    /**
     * Fetch a city by its unique ID.
     *
     * @param id City ID to fetch.
     * @return CityEntity object and HTTP status OK.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CityEntity> getCityByID(@PathVariable Long id) {
        log.info("Fetching city with ID: {}", id);
        CityEntity city = cityService.getCityById(id);
        log.info("City fetched successfully: {}", city.getCityName());
        return new ResponseEntity<>(city, HttpStatus.OK);
    }

    /**
     * Update an existing city.
     *
     * @param id   City ID to update.
     * @param city CityEntity payload with updated details.
     * @return Updated CityEntity and HTTP status OK.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CityEntity> updateCity(@PathVariable Long id, @RequestBody CityEntity city) {
        log.info("Request received to update city with ID: {}", id);
        CityEntity updatedCity = cityService.updateCity(id, city);
        log.info("City updated successfully with ID: {}", updatedCity.getCityId());
        return new ResponseEntity<>(updatedCity, HttpStatus.OK);
    }

    /**
     * Delete a city by its unique ID.
     *
     * @param id City ID to delete.
     * @return Confirmation message and HTTP status OK.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCity(@PathVariable Long id) {
        log.info("Request received to delete city with ID: {}", id);
        cityService.deleteCity(id);
        log.info("City deleted successfully with ID: {}", id);
        return new ResponseEntity<>("City deleted successfully", HttpStatus.OK);
    }

}
