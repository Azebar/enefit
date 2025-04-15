package com.enefit.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsumptionDto {
    private String meteringPointId;
    private LocalDateTime timestamp;
    private double consumption;
    private BigDecimal cost;
} 