package com.enefit.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ConsumptionResponse(
        Long consumptionId,
        String meteringPointId,
        LocalDate consumptionTime,
        BigDecimal amount
) {} 