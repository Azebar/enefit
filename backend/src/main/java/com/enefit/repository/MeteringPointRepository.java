package com.enefit.repository;

import com.enefit.model.MeteringPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeteringPointRepository extends JpaRepository<MeteringPoint, Long> {
    @Query("SELECT m FROM MeteringPoint m WHERE m.customer.customerId = :customerId")
    List<MeteringPoint> findByCustomerId(@Param("customerId") Long customerId);
    
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM MeteringPoint m " +
           "WHERE m.meteringPointId = :meteringPointId AND m.customer.customerId = :customerId")
    boolean existsByIdAndCustomerId(@Param("meteringPointId") Long meteringPointId, @Param("customerId") Long customerId);

    List<MeteringPoint> findByCustomer_Username(String username);
} 