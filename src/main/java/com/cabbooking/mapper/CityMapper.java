package com.cabbooking.mapper;

import com.cabbooking.dto.CityDTO;

import com.cabbooking.entity.CityEntity;
import org.springframework.stereotype.Component;


@Component
public class CityMapper {

    /**
     * Convert City entity → CityDTO
     */
    public  CityDTO toDTO(CityEntity city) {
        if (city == null) return null;

        return CityDTO.builder()
                .cityId(city.getCityId())
                .cityName(city.getCityName())
                .build();
    }

    /**
     * Convert CityDTO → City entity
     */
    public  CityEntity toEntity(CityDTO dto) {  // <--- fix parameter type
        if (dto == null) return null;

        CityEntity city = new CityEntity();
        city.setCityId(dto.getCityId());
        city.setCityName(dto.getCityName());
        return city;
    }
}
