package com.enefit.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "consumption")
public class Consumption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consumption_id")
    private Long consumptionId;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metering_point_id", nullable = false)
    private MeteringPoint meteringPoint;

    @Column(nullable = false)
    private LocalDateTime consumptionTime;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "amount_unit", nullable = false)
    private String amountUnit;
} 