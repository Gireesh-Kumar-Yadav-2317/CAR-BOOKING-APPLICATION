/*
package com.cabbooking.service;


import com.cabbooking.entity.CityEntity;
import com.cabbooking.repo.CityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CityServiceImpl implements ICityService {

    private final CityRepository cityRepository;

    */
/** Create a new city *//*

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

    */
/** Fetch all cities *//*

    @Override
    public List<CityEntity> getAllCities() {
        log.info("Fetching all cities");
        return cityRepository.findAll();
    }

    */
/** Fetch a city by ID *//*

    @Override
    public CityEntity getCityById(Long id) {
        log.info("Fetching city by ID: {}", id);
        return cityRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("City not found with ID: {}", id);
                    return new RuntimeException("City not found with ID: " + id);
                });
    }

    */
/** Update an existing city *//*

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

    */
/** Delete a city by ID *//*

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
    @Override
    public CityEntity getCityByName(String cityName) {
        log.info("Fetching city by name: {}", cityName);

        // Allow "All Cities"
        if (cityName == null || cityName.isBlank()) {
            log.info("City name is null or blank, returning null (All Cities)");
            return null;
        }

        // Fetch city from DB safely
        return cityRepository.findByCityName(cityName)
                .orElse(null); // return null if not found
    }


    */
/**
     * Fetch all city names as sorted list
     *//*

    public List<String> getAllCityNames() {
        log.info("Fetching all city names");
        return getAllCities() // fetch all cities
                .stream()
                .map(CityEntity::getCityName)
                .sorted()
                .collect(Collectors.toList());
    }



}
*/
