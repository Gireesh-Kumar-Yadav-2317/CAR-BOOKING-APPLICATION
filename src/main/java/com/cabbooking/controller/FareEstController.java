/*
package com.cabbooking.controller;

import com.cabbooking.dto.FareEstRequest;
import com.cabbooking.dto.FareEstResponse;
import com.cabbooking.service.FareEstServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fare-rules")
@RequiredArgsConstructor
public class FareEstController {

    private final FareEstServiceImpl fareEstService;

    @PostMapping
    public ResponseEntity<FareEstResponse> addFareEst(@RequestBody FareEstRequest request) {
        return ResponseEntity.ok(fareEstService.addFareEst(request));
    }

    @GetMapping("/{cityId}")
    public ResponseEntity<FareEstResponse> getFareEstByCity(@PathVariable Long cityId) {
        return ResponseEntity.ok(fareEstService.getFareEstByCity(cityId));
    }

    @GetMapping
    public ResponseEntity<List<FareEstResponse>> getAllFareEst() {
        return ResponseEntity.ok(fareEstService.getAllFareEst());
    }
}

*/
