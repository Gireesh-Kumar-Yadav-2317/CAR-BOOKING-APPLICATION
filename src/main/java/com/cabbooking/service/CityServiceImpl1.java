package com.cabbooking.service;

import com.cabbooking.entity.CityEntity;
import com.cabbooking.repo.CityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for managing City-related operations.
 * Provides CRUD functionality and additional utilities
 * such as fetching city names.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CityServiceImpl1 implements ICityService {

    private final CityRepository cityRepository;

    /**
     * Create a new city after checking for duplicates.
     *
     * @param city the city entity to create
     * @return the saved CityEntity
     */
    @Override
    public CityEntity createCity(CityEntity city) {
        log.info("Creating city: {}", city.getCityName());

        if (cityRepository.existsByCityName(city.getCityName())) {
            log.error("City already exists: {}", city.getCityName());
            throw new RuntimeException("City already exists: " + city.getCityName());
        }

        CityEntity savedCity = cityRepository.save(city);
        log.info("City created with ID: {}", savedCity.getCityId());
        return savedCity;
    }

    /**
     * Fetch all cities from the database.
     *
     * @return list of CityEntity
     */
    @Override
    public List<CityEntity> getAllCities() {
        log.info("Fetching all cities");
        return cityRepository.findAll();
    }

    /**
     * Fetch a city by its ID.
     *
     * @param id city ID
     * @return CityEntity if found
     */
    @Override
    public CityEntity getCityById(Long id) {
        log.info("Fetching city by ID: {}", id);
        return cityRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("City not found with ID: {}", id);
                    return new RuntimeException("City not found with ID: " + id);
                });
    }

    /**
     * Update an existing city’s name.
     *
     * @param id   city ID
     * @param city new city data
     * @return updated CityEntity
     */
    @Override
    public CityEntity updateCity(Long id, CityEntity city) {
        log.info("Updating city with ID: {}", id);
        CityEntity existingCity = cityRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("City not found with ID: {}", id);
                    return new RuntimeException("City not found with ID: " + id);
                });

        if (city.getCityName() != null && !city.getCityName().isBlank()) {
            existingCity.setCityName(city.getCityName());
            log.info("City name updated to: {}", city.getCityName());
        }
        return cityRepository.save(existingCity);
    }

    /**
     * Delete a city by ID after checking existence.
     *
     * @param id city ID
     */
    @Override
    public void deleteCity(Long id) {
        log.info("Deleting city with ID: {}", id);
        if (!cityRepository.existsById(id)) {
            log.error("City not found with ID: {}", id);
            throw new RuntimeException("City not found with ID: " + id);
        }
        cityRepository.deleteById(id);
        log.info("City deleted successfully: {}", id);
    }

    /**
     * Fetch city by name, returning null if name is blank or not found.
     *
     * @param cityName city name
     * @return CityEntity or null
     */
    @Override
    public CityEntity getCityByName(String cityName) {
        log.info("Fetching city by name: {}", cityName);

        if (cityName == null || cityName.isBlank()) {
            log.info("City name is null/blank, returning null (All Cities case)");
            return null;
        }
        return cityRepository.findByCityName(cityName).orElse(null);
    }

    /**
     * Fetch all city names as a sorted list.
     *
     * @return sorted list of city names
     */
    public List<String> getAllCityNames() {
        log.info("Fetching all city names");
        return getAllCities().stream()
                .map(CityEntity::getCityName)
                .sorted()
                .collect(Collectors.toList());
    }
}
