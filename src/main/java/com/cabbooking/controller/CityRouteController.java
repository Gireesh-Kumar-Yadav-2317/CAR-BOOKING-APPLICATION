/*
package com.cabbooking.controller;

import com.cabbooking.dto.CityRouteDTO;
import com.cabbooking.dto.CityRouteRequest;

import com.cabbooking.entity.CityRouteEntity;
import com.cabbooking.service.CityRouteServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/city-routes")
@RequiredArgsConstructor
public class CityRouteController {

    private final CityRouteServiceImpl cityRouteService;
    @PostMapping
    public ResponseEntity<CityRouteDTO> addRoute(@RequestBody CityRouteRequest request) {
        CityRouteDTO response = cityRouteService.addRoute(request);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/city/{cityId}")
    public ResponseEntity<List<CityRouteDTO>> getRoutesByCity(@PathVariable Long cityId) {
        List<CityRouteDTO> responses = cityRouteService.getRoutesByCity(cityId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{routeId}")
    public ResponseEntity<CityRouteDTO> getRouteById(@PathVariable Long routeId) {
        CityRouteDTO response = cityRouteService.getRouteById(routeId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/search")
    public ResponseEntity<CityRouteEntity> getRouteByPickupAndDrop(
            @RequestParam Long cityId,
            @RequestParam String pickup,
            @RequestParam String drop) {
        CityRouteEntity response = cityRouteService.getRouteEntityByPickupAndDrop(cityId, pickup, drop);
        return ResponseEntity.ok(response);
    }



}

*/
