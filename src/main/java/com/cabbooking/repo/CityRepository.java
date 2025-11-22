package com.cabbooking.repo;

import com.cabbooking.entity.CityEntity;
import com.cabbooking.entity.DriverEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<CityEntity, Long > {
    boolean existsByCityName(String cityName);

    Optional<CityEntity> findByCityName(String cityName);
}
