package com.enefit.service;

import com.enefit.config.ExternalApiConfig;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnergyPriceService {
    private final RestTemplate restTemplate;
    private final ExternalApiConfig externalApiConfig;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    @Cacheable(value = "energyPrices", key = "#date")
    public BigDecimal getEnergyPriceForDate(LocalDate date) {
        // If the date is in the future, return a default price
        if (date.isAfter(LocalDate.now())) {
            log.warn("Requested energy price for future date: {}. Returning default price.", date);
            return BigDecimal.valueOf(0.15); // Default price in EUR/kWh
        }

        String startDateTime = date.atStartOfDay().atOffset(ZoneOffset.UTC).format(DATE_TIME_FORMATTER);
        String endDateTime = date.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC).format(DATE_TIME_FORMATTER);
        
        String url = externalApiConfig.getElering().getBaseUrl() + 
                    externalApiConfig.getElering().getEnergyPriceEndpoint() +
                    "?startDateTime=" + startDateTime + "&endDateTime=" + endDateTime;

        try {
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);
            if (response != null && response.has("data")) {
                JsonNode data = response.get("data");
                if (data.isArray() && data.size() > 0) {
                    // Get the average price for the day
                    double totalPrice = 0;
                    int count = 0;
                    for (JsonNode priceNode : data) {
                        if (priceNode.has("price")) {
                            totalPrice += priceNode.get("price").asDouble();
                            count++;
                        }
                    }
                    return count > 0 ? BigDecimal.valueOf(totalPrice / count) : BigDecimal.ZERO;
                }
            }
        } catch (Exception e) {
            log.error("Error fetching energy price for date {}: {}", date, e.getMessage());
        }
        return BigDecimal.ZERO;
    }

    public Map<LocalDate, BigDecimal> getEnergyPricesForMonth(LocalDate month) {
        Map<LocalDate, BigDecimal> prices = new HashMap<>();
        LocalDate startDate = month.withDayOfMonth(1);
        LocalDate endDate = month.withDayOfMonth(month.lengthOfMonth());

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            prices.put(date, getEnergyPriceForDate(date));
        }

        return prices;
    }
} 