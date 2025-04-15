package com.enefit.repository;

import com.enefit.entity.Consumption;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumptionRepository extends JpaRepository<Consumption, Long> {
    @Query("SELECT c FROM Consumption c WHERE c.meteringPoint.meteringPointId = :meteringPointId")
    List<Consumption> findByMeteringPointId(@Param("meteringPointId") Long meteringPointId);
} 