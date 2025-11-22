/*
package com.cabbooking.controller;

import com.cabbooking.entity.CityEntity;

import com.cabbooking.service.CityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {
    private final CityServiceImpl cityService;
    @PostMapping
    public ResponseEntity<CityEntity> createCity(@RequestBody CityEntity city){
        CityEntity savedCity = cityService.createCity(city);
        return  new ResponseEntity<>(savedCity , HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<CityEntity>> getAllCities(){
        return ResponseEntity.ok(cityService.getAllCities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityEntity> getCityByID(@PathVariable Long id){
        CityEntity city =  cityService.getCityById(id);
        return  new ResponseEntity<>(city , HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityEntity> updateCity(@PathVariable Long id, @RequestBody CityEntity city) {
        CityEntity updatedCity = cityService.updateCity(id , city);
        return new ResponseEntity<>(updatedCity, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCity(@PathVariable Long id) {
        cityService.deleteCity(id);
        return new ResponseEntity<>("City deleted successfully", HttpStatus.OK);
    }



}
*/
