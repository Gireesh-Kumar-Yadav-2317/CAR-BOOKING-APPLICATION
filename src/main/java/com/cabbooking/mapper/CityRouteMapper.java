package com.cabbooking.mapper;

import com.cabbooking.dto.CityRouteDTO;
import com.cabbooking.dto.CityRouteRequest;

import com.cabbooking.entity.CityRouteEntity;
import org.springframework.stereotype.Component;

@Component
public class CityRouteMapper {
    private  final CityMapper cityMapper;

    public CityRouteMapper(CityMapper cityMapper) {
        this.cityMapper = cityMapper;
    }

    /**
     * Convert CityRouteEntity → CityRouteDTO
     */
    public  CityRouteDTO toDTO(CityRouteEntity entity) {
        if (entity == null) return null;

        return CityRouteDTO.builder()
                .routeId(entity.getRouteId())
                .city(cityMapper.toDTO(entity.getCity()))
                .pickupLocation(entity.getPickupLocation())
                .dropLocation(entity.getDropLocation())
                .distanceKm(entity.getDistanceKm())
                .build();
    }

    /**
     * Convert CityRouteRequest → CityRouteEntity
     */
    public  CityRouteEntity toEntity(CityRouteRequest request) {
        if (request == null) return null;

        return CityRouteEntity.builder()
                .city(cityMapper.toEntity(request.getCity()))
                .pickupLocation(request.getPickupLocation())
                .dropLocation(request.getDropLocation())
                .distanceKm(request.getDistanceKm())
                .build();
    }

    /**
     * Convert CityRouteDTO → CityRouteEntity
     */
    public  CityRouteEntity toEntity(CityRouteDTO dto) {
        if (dto == null) return null;

        return CityRouteEntity.builder()
                .routeId(dto.getRouteId())
                .city(cityMapper.toEntity(dto.getCity()))
                .pickupLocation(dto.getPickupLocation())
                .dropLocation(dto.getDropLocation())
                .distanceKm(dto.getDistanceKm())
                .build();
    }

    /**
     * Convert CityRouteEntity → CityRouteRequest
     */
    public  CityRouteRequest toRequest(CityRouteEntity entity) {
        if (entity == null) return null;

        return CityRouteRequest.builder()
                .city(cityMapper.toDTO(entity.getCity()))
                .pickupLocation(entity.getPickupLocation())
                .dropLocation(entity.getDropLocation())
                .distanceKm(entity.getDistanceKm())
                .build();
    }

    /**
     * Update an existing CityRouteEntity using CityRouteRequest
     */
    public  void updateEntity(CityRouteEntity entity, CityRouteRequest request) {
        if (entity == null || request == null) return;

        if (request.getCity() != null) {
            entity.setCity(cityMapper.toEntity(request.getCity()));
        }
        if (request.getPickupLocation() != null && !request.getPickupLocation().isBlank()) {
            entity.setPickupLocation(request.getPickupLocation());
        }
        if (request.getDropLocation() != null && !request.getDropLocation().isBlank()) {
            entity.setDropLocation(request.getDropLocation());
        }
        if (request.getDistanceKm() != null) {
            entity.setDistanceKm(request.getDistanceKm());
        }
    }
}
