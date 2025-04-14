package com.enefit.repository;

import com.enefit.model.Consumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsumptionRepository extends JpaRepository<Consumption, Long> {
    @Query("SELECT c FROM Consumption c WHERE c.meteringPoint.meteringPointId = :meteringPointId")
    List<Consumption> findByMeteringPointId(@Param("meteringPointId") Long meteringPointId);
    
    @Query("SELECT c FROM Consumption c WHERE c.meteringPoint.meteringPointId = :meteringPointId " +
           "AND c.consumptionTime BETWEEN :startDate AND :endDate")
    List<Consumption> findByMeteringPointIdAndDateBetween(
            @Param("meteringPointId") Long meteringPointId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
} 