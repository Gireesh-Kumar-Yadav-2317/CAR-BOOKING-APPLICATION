package com.cabbooking.service;

import com.cabbooking.entity.CityEntity;

import java.util.List;

public interface ICityService {


    CityEntity createCity(CityEntity city);

    List<CityEntity> getAllCities();
    CityEntity getCityById(Long id);

    CityEntity updateCity(Long id , CityEntity city);
    void deleteCity(Long id);


    CityEntity getCityByName(String cityName);
}
