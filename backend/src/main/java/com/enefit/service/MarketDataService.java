package com.enefit.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MarketDataService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String eleringApiUrl;

    public MarketDataService(
            @Value("${elering.api.url:https://estfeed.elering.ee/api/public/v1/energy-price/electricity}") String eleringApiUrl) {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        this.eleringApiUrl = eleringApiUrl;
    }

    @Cacheable(value = "marketPrices", key = "#date")
    public BigDecimal getPriceForDate(LocalDate date) {
        try {
            String response = restTemplate.getForObject(eleringApiUrl, String.class);
            JsonNode root = objectMapper.readTree(response);
            
            // Find the price for the given date
            for (JsonNode data : root.get("data")) {
                String timestamp = data.get("timestamp").asText();
                LocalDate priceDate = LocalDate.parse(timestamp.split("T")[0]);
                
                if (priceDate.equals(date)) {
                    return BigDecimal.valueOf(data.get("price").asDouble());
                }
            }
            
            // If no price found for the date, return a default value
            return BigDecimal.ZERO;
        } catch (Exception e) {
            // Log the error and return a default value
            return BigDecimal.ZERO;
        }
    }
} 