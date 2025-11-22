package com.cabbooking.repo;


import com.cabbooking.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<AdminEntity, Long> {

    boolean existsByUsername(String username);

   Optional<AdminEntity> findByUsername(String username);

    boolean existsByMobileNumber(String mobileNumber);
}
