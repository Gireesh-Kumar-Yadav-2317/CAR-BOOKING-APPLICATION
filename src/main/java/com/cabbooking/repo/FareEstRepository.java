package com.cabbooking.repo;

import com.cabbooking.entity.FareEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FareEstRepository extends JpaRepository<FareEntity, Long> {
     Optional<FareEntity> findByCity_CityId(Long cityId);
}
