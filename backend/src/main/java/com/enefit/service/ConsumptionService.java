package com.enefit.service;

import com.enefit.dto.ConsumptionDTO;
import com.enefit.dto.MeteringPointDTO;
import com.enefit.mapper.ConsumptionMapper;
import com.enefit.mapper.MeteringPointMapper;
import com.enefit.model.Consumption;
import com.enefit.model.MeteringPoint;
import com.enefit.repository.ConsumptionRepository;
import com.enefit.repository.MeteringPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsumptionService {

    private final ConsumptionRepository consumptionRepository;
    private final MeteringPointRepository meteringPointRepository;
    private final MarketDataService marketDataService;
    private final ConsumptionMapper consumptionMapper;
    private final MeteringPointMapper meteringPointMapper;


    public List<ConsumptionDTO> getConsumptionByMeteringPoint(Long meteringPointId) {
        return consumptionRepository.findByMeteringPointId(meteringPointId)
                .stream()
                .map(consumptionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ConsumptionDTO> getConsumptionByMeteringPointAndDateRange(
            Long meteringPointId, LocalDateTime startDate, LocalDateTime endDate) {
        return consumptionRepository.findByMeteringPointIdAndDateBetween(meteringPointId, startDate, endDate)
                .stream()
                .map(consumptionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<MeteringPointDTO> getMeteringPointsForCustomer(String username) {
        List<MeteringPoint> meteringPoints = meteringPointRepository.findByCustomer_Username(username);
        return meteringPointMapper.toDTOList(meteringPoints);
    }
} 