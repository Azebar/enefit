package com.enefit.repository;

import com.enefit.entity.MeteringPoint;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeteringPointRepository extends JpaRepository<MeteringPoint, Long> {

    List<MeteringPoint> findByCustomer_CustomerId(Long customerId);
} 