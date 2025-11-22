package com.cabbooking.repo;

import com.cabbooking.entity.DriverEntity;
import com.cabbooking.entity.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<DriverEntity, Long> {

    Optional<DriverEntity> findByUsername(String username);

    // Corrected JPQL entity name to DriverEntity
    @Query("SELECT d FROM DriverEntity d WHERE d.city.cityId = :cityId AND d.status = :status")
    List<DriverEntity> findActiveDriversByCity(@Param("cityId") Long cityId, @Param("status") DriverStatus status);

    @Query("SELECT COUNT(d) FROM DriverEntity d WHERE (:cityId IS NULL OR d.city.id = :cityId)")
    long countDriversByCityId(@Param("cityId") Long cityId);


    @Query("SELECT d FROM DriverEntity d WHERE d.city.cityName = :cityName")
    List<DriverEntity> findDriversByCityName(@Param("cityName") String cityName);
    List<DriverEntity> findDriversByCity_CityId(Long cityId);
}
