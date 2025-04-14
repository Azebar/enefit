package com.enefit.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ConsumptionDetailsDTO {
    private String meteringPointId;
    private String address;
    private BigDecimal amount;
    private String amountUnit;
    private BigDecimal cost;
    private LocalDateTime consumptionTime;
} 