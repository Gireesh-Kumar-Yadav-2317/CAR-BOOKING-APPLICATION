package com.cabbooking.mapper;

import com.cabbooking.dto.FareEstResponse;
import com.cabbooking.dto.CityDTO;
import com.cabbooking.entity.FareEntity;
import org.springframework.stereotype.Component;

@Component
public class FareEstMapper {
    private final CityMapper cityMapper;

    public FareEstMapper(CityMapper cityMapper) {
        this.cityMapper = cityMapper;
    }

    /**
     * Convert FareEntity → FareEstResponse DTO
     */
    public  FareEstResponse toResponse(FareEntity fare) {
        if (fare == null) return null;

        CityDTO cityDTO = fare.getCity() != null ? cityMapper.toDTO(fare.getCity()) : null;

        return FareEstResponse.builder()
                .fareId(fare.getFareId())
                .city(cityDTO)          // Use CityDTO
                .baseFare(fare.getBaseFare())
                .perKmRate(fare.getPerKmRate())
                .build();
    }
}
