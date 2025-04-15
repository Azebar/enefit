package com.enefit.mapper;

import com.enefit.dto.ConsumptionDto;
import com.enefit.entity.Consumption;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class ConsumptionMapper {

    public ConsumptionDto toDTO(Consumption consumption) {
        return ConsumptionDto.builder()
                .meteringPointId(consumption.getMeteringPoint().getMeteringPointId().toString())
                .consumption(consumption.getAmount().doubleValue())
                .timestamp(consumption.getConsumptionTime())
                .cost(BigDecimal.ZERO) // Default cost, will be updated by the service
                .build();
    }
} 