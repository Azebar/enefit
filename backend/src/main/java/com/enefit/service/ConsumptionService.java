package com.enefit.service;

import com.enefit.dto.ConsumptionDto;
import com.enefit.dto.MeteringPointDto;
import com.enefit.entity.MeteringPoint;
import com.enefit.mapper.ConsumptionMapper;
import com.enefit.mapper.MeteringPointMapper;
import com.enefit.repository.ConsumptionRepository;
import com.enefit.repository.MeteringPointRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumptionService {

    private final ConsumptionRepository consumptionRepository;
    private final MeteringPointRepository meteringPointRepository;
    private final ConsumptionMapper consumptionMapper;
    private final MeteringPointMapper meteringPointMapper;
    private final EleringService eleringService;

    public List<ConsumptionDto> getConsumptionByMeteringPoint(Long meteringPointId) {
        List<ConsumptionDto> consumptions = consumptionRepository.findByMeteringPointId(meteringPointId)
                .stream()
                .map(consumptionMapper::toDTO)
                .toList();
        
        // Get the date range from the consumptions
        LocalDateTime startDate = consumptions.stream()
                .map(ConsumptionDto::getTimestamp)
                .min(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now().minusYears(1));

        LocalDateTime endDate = consumptions.stream()
                .map(ConsumptionDto::getTimestamp)
                .max(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());

        log.info("Fetching prices for date range: {} to {}", startDate, endDate);

        // Fetch price data from Elering for each month
        var consumptionsByMonth = consumptions.stream()
                .collect(Collectors.groupingBy(consumption -> {
                    LocalDateTime time = consumption.getTimestamp();
                    return time.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
                }));

        var allPrices = consumptionsByMonth.keySet().stream()
                .flatMap(monthStart -> {
                    LocalDateTime monthEnd = monthStart.plusMonths(1);
                    log.info("Fetching prices for month: {} to {}", monthStart, monthEnd);
                    return eleringService.getPrices(monthStart, monthEnd).stream();
                })
                .toList();

        log.info("Received total of {} prices from Elering API", allPrices.size());

        // Calculate costs for each consumption
        return consumptions.stream()
                .map(consumption -> {
                    // Convert consumption time to Unix timestamp in UTC
                    long consumptionTimestamp = consumption.getTimestamp()
                            .atZone(ZoneId.of("UTC"))
                            .toInstant()
                            .getEpochSecond();

                    log.info("Processing consumption at {} (timestamp: {})",
                            consumption.getTimestamp(), consumptionTimestamp);

                    // Find the price for this consumption timestamp
                    // Elering API returns hourly prices, so we need to match the hour
                    var price = allPrices.stream()
                            .filter(p -> {
                                // Round down to the nearest hour
                                long consumptionHour = consumptionTimestamp - (consumptionTimestamp % 3600);
                                long timestamp = LocalDateTime.parse(p.getFromDateTime(),
                                    DateTimeFormatter.ISO_DATE_TIME)
                                    .atZone(ZoneId.of("UTC"))
                                    .toInstant()
                                    .getEpochSecond();
                                long priceHour = timestamp - (timestamp % 3600);
                                boolean matches = consumptionHour == priceHour;
                                if (matches) {
                                    log.info("Found matching price: {} EUR/MWh for hour {}",
                                            p.getEurPerMwh(), Instant.ofEpochSecond(priceHour));
                                }
                                return matches;
                            })
                            .findFirst()
                            .orElse(null);

                    if (price != null) {
                        // Calculate cost in EUR (price is in EUR/MWh, amount is in kWh)
                        BigDecimal cost = BigDecimal.valueOf(price.getEurPerMwh())
                                .multiply(BigDecimal.valueOf(consumption.getConsumption()))
                                .divide(BigDecimal.valueOf(1000), RoundingMode.HALF_UP); // Convert from MWh to kWh
                        consumption.setCost(cost);
                        log.info("Calculated cost: {} EUR for consumption of {} kWh",
                                cost, consumption.getConsumption());
                    } else {
                        log.warn("No matching price found for consumption at {}",
                                consumption.getTimestamp());
                        consumption.setCost(BigDecimal.ZERO);
                    }

                    return consumption;
                })
                .sorted(Comparator.comparing(ConsumptionDto::getTimestamp))
                .collect(Collectors.toList());
    }

    public List<MeteringPointDto> getMeteringPointsForCustomer(Long customerId) {
        List<MeteringPoint> meteringPoints = meteringPointRepository.findByCustomer_CustomerId(customerId);
        return meteringPointMapper.toDTOList(meteringPoints);
    }
} 