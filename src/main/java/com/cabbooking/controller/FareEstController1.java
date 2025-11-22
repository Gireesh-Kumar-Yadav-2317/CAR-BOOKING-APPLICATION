package com.cabbooking.controller;

import com.cabbooking.dto.FareEstRequest;
import com.cabbooking.dto.FareEstResponse;
import com.cabbooking.service.FareEstServiceImpl1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;          // For logging
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/fare-rules")
@RequiredArgsConstructor
public class FareEstController1 {

    // Service layer dependency injected via constructor
    private final FareEstServiceImpl1 fareEstService;

    /**
     * Add a new fare estimation rule.
     * @param request fare estimation details
     * @return saved FareEstResponse
     */
    @PostMapping
    public ResponseEntity<FareEstResponse> create(@RequestBody FareEstRequest request) {
        log.info("Received request to add fare estimation: {}", request);
        return ResponseEntity.ok(fareEstService.addFareEst(request));
    }

    /**
     * Get fare estimation rule by city ID.
     * @param cityId city identifier
     * @return FareEstResponse for the given city
     */
    @GetMapping("/{cityId}")
    public ResponseEntity<FareEstResponse> findByCity(@PathVariable Long cityId) {
        log.info("Fetching fare estimation for cityId: {}", cityId);
        return ResponseEntity.ok(fareEstService.getFareEstByCity(cityId));
    }

    /**
     * Fetch all fare estimation rules across all cities.
     * @return list of FareEstResponse
     */
    @GetMapping
    public ResponseEntity<List<FareEstResponse>> findAll() {
        log.info("Fetching all fare estimations");
        return ResponseEntity.ok(fareEstService.getAllFareEst());
    }
}
