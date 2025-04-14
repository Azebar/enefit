package com.enefit.mapper;

import com.enefit.dto.ConsumptionDTO;
import com.enefit.dto.ConsumptionDetailsDTO;
import com.enefit.dto.ConsumptionResponse;
import com.enefit.dto.MeteringPointResponse;
import com.enefit.model.Consumption;
import com.enefit.model.MeteringPoint;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ConsumptionMapper {
    public List<MeteringPointResponse> toMeteringPointResponseList(List<MeteringPoint> meteringPoints) {
        return meteringPoints.stream()
                .map(this::toMeteringPointResponse)
                .collect(Collectors.toList());
    }

    public MeteringPointResponse toMeteringPointResponse(MeteringPoint meteringPoint) {
        return new MeteringPointResponse(
                meteringPoint.getMeteringPointId().toString(),
                meteringPoint.getAddress(),
                meteringPoint.getAddress()
        );
    }

    public List<ConsumptionResponse> toConsumptionResponseList(List<Consumption> consumptions) {
        return consumptions.stream()
                .map(this::toConsumptionResponse)
                .collect(Collectors.toList());
    }

    public ConsumptionResponse toConsumptionResponse(Consumption consumption) {
        return new ConsumptionResponse(
                consumption.getConsumptionId(),
                consumption.getMeteringPoint().getMeteringPointId().toString(),
                consumption.getConsumptionTime().toLocalDate(),
                consumption.getAmount()
        );
    }

    public Map<LocalDate, ConsumptionResponse> toConsumptionResponseMap(Map<LocalDate, BigDecimal> costs) {
        return costs.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> new ConsumptionResponse(
                                null,
                                null,
                                entry.getKey(),
                                entry.getValue()
                        )
                ));
    }

    public Map<String, List<ConsumptionDTO>> groupByMeteringPoint(List<Consumption> consumptions) {
        return consumptions.stream()
                .collect(Collectors.groupingBy(
                        consumption -> consumption.getMeteringPoint().getMeteringPointId().toString(),
                        Collectors.mapping(this::toDTO, Collectors.toList())
                ));
    }

    public ConsumptionDetailsDTO toDetailsDTO(Consumption consumption) {
        MeteringPoint meteringPoint = consumption.getMeteringPoint();
        return ConsumptionDetailsDTO.builder()
                .meteringPointId(meteringPoint.getMeteringPointId().toString())
                .address(meteringPoint.getAddress())
                .amount(consumption.getAmount())
                .amountUnit(consumption.getAmountUnit())
                .cost(consumption.getCost())
                .consumptionTime(consumption.getConsumptionTime())
                .build();
    }

    public ConsumptionDTO toDTO(Consumption consumption) {
        return ConsumptionDTO.builder()
                .meteringPointId(consumption.getMeteringPoint().getMeteringPointId().toString())
                .amount(consumption.getAmount())
                .amountUnit(consumption.getAmountUnit())
                .cost(consumption.getCost())
                .consumptionTime(consumption.getConsumptionTime())
                .build();
    }

    public List<ConsumptionDTO> toDTOList(List<Consumption> consumptions) {
        return consumptions.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
} 