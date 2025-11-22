package com.cabbooking.repo;

import com.cabbooking.entity.CityRouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CityRouteRepository extends JpaRepository<CityRouteEntity, Long> {

    // Fetch a unique route by pickup, drop, and cityId
    @Query("SELECT cr FROM CityRouteEntity cr " +
            "WHERE cr.pickupLocation = :pickup " +
            "AND cr.dropLocation = :drop " +
            "AND cr.city.cityId = :cityId")
    List<CityRouteEntity> findUniqueRoute(@Param("pickup") String pickup,
                                          @Param("drop") String drop,
                                          @Param("cityId") Long cityId);

    // Fetch all routes for a city using cityId
    @Query("SELECT cr FROM CityRouteEntity cr WHERE cr.city.cityId = :cityId")
    List<CityRouteEntity> findRoutesByCityId(@Param("cityId") Long cityId);

    // Alternative: Spring Data derived query
    List<CityRouteEntity> findByCity_CityId(Long cityId);
}
