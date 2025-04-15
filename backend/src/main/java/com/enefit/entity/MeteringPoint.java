package com.enefit.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "metering_points")
public class MeteringPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metering_point_id")
    private Long meteringPointId;

    @Column(nullable = false)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "meteringPoint", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Consumption> consumptions = new ArrayList<>();
} 