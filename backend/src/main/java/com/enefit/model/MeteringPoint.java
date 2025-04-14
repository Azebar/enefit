package com.enefit.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @JsonManagedReference
    @OneToMany(mappedBy = "meteringPoint", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Consumption> consumptions = new ArrayList<>();
} 