package com.cabbooking.repo;

import com.cabbooking.entity.BookingEntity;
import com.cabbooking.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    @Query("SELECT b FROM BookingEntity b WHERE b.driver.driverId = :driverId AND b.status = :status")
    List<BookingEntity> findPendingBookingsForDriver(@Param("driverId") Long driverId,
                                                     @Param("status") BookingStatus status);

    @Query("SELECT b FROM BookingEntity b WHERE b.driver.driverId = :driverId AND b.status IN :statusList")
    List<BookingEntity> findByDriverIdAndStatusIn(@Param("driverId") Long driverId,
                                                  @Param("statusList") List<BookingStatus> statusList);

    @Query("SELECT b FROM BookingEntity b WHERE b.driver.driverId = :driverId AND b.status = :status")
    List<BookingEntity> findCompletedRides(@Param("driverId") Long driverId,
                                           @Param("status") BookingStatus status);

    @Query("SELECT b FROM BookingEntity b WHERE b.driver.driverId = :driverId")
    List<BookingEntity> findAllRidesByDriver(@Param("driverId") Long driverId);

    @Query("SELECT b FROM BookingEntity b WHERE b.driver.driverId = :driverId AND b.status = :status")
    List<BookingEntity> findAcceptedRides(@Param("driverId") Long driverId,
                                          @Param("status") BookingStatus status);

    @Query("SELECT b FROM BookingEntity b WHERE b.driver.driverId = :driverId AND b.status = :status")
    List<BookingEntity> findRejectedRides(@Param("driverId") Long driverId,
                                          @Param("status") BookingStatus status);

    @Query("SELECT b FROM BookingEntity b WHERE b.driver.driverId = :driverId AND b.status = :status")
    List<BookingEntity> findUpcomingRides(@Param("driverId") Long driverId,
                                          @Param("status") BookingStatus status);

    @Query("SELECT COUNT(b) FROM BookingEntity b WHERE b.driver.driverId = :driverId AND b.status = :status")
    Long countRidesByStatus(@Param("driverId") Long driverId,
                            @Param("status") BookingStatus status);

    @Query("SELECT b FROM BookingEntity b " +
            "WHERE (b.driver.driverId = :driverId OR b.driver IS NULL) " +
            "AND b.status = :status " +
            "AND b.city.cityId = :cityId")
    List<BookingEntity> findPendingBookingsForDriver(@Param("driverId") Long driverId,
                                                     @Param("status") BookingStatus status,
                                                     @Param("cityId") Long cityId);

    @Query("SELECT b FROM BookingEntity b WHERE b.status = :status AND b.city.cityId = :cityId AND b.driver IS NULL")
    List<BookingEntity> findPendingRidesByCity(@Param("status") BookingStatus status,
                                               @Param("cityId") Long cityId);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
            "FROM BookingEntity b " +
            "WHERE b.driver.driverId = :driverId " +
            "AND b.status IN :statuses")
    boolean existsByDriverAndStatusIn(@Param("driverId") Long driverId,
                                      @Param("statuses") List<BookingStatus> statuses);

    @Query("SELECT COUNT(b) FROM BookingEntity b WHERE (:cityId IS NULL OR b.city.id = :cityId)")
    long countRidesByCityId(@Param("cityId") Long cityId);

    @Query("SELECT b FROM BookingEntity b WHERE " +
            "(:cityId IS NULL OR b.city.id = :cityId) AND " +
            "(:status IS NULL OR b.status = :status) AND " +
            "(:startDateTime IS NULL OR b.startTime >= :startDateTime) AND " +
            "(:endDateTime IS NULL OR b.endTime <= :endDateTime)")
    List<BookingEntity> findBookings(@Param("cityId") Long cityId,
                                     @Param("status") BookingStatus status,
                                     @Param("startDateTime") LocalDateTime startDateTime,
                                     @Param("endDateTime") LocalDateTime endDateTime);


    List<BookingEntity> findByUser_UserId(Long userId);

    List<BookingEntity> findByDriver_DriverId(Long driverId);

    @Query("select b.bookingId from BookingEntity b where b.user.id = :userId")
    List<BookingEntity> findIdsByUserId(Long userId);

    @Query("SELECT b FROM BookingEntity b WHERE b.driver.driverId = :driverId")
    List<BookingEntity> findBookingsByDriverId(@Param("driverId") Long driverId);

}
