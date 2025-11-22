package com.cabbooking.repo;

import com.cabbooking.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    // Corrected JPQL entity name
    @Query("SELECT COUNT(u) FROM UserEntity u WHERE (:cityId IS NULL OR u.city.id = :cityId)")
    long countUsersByCityId(@Param("cityId") Long cityId);

    @Query("SELECT u FROM UserEntity u WHERE u.city.cityName = :cityName")
    List<UserEntity> findUserByCityName(@Param("cityName") String cityName);


    @Query("SELECT u FROM UserEntity u WHERE u.city.cityId = :cityId")
    List<UserEntity> findUserByCityId(@Param("cityId") Long cityId);

}
