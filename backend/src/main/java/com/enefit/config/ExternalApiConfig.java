package com.enefit.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ConfigurationProperties(prefix = "external-api")
@Data
public class ExternalApiConfig {
    private Elering elering;

    @Data
    public static class Elering {
        private String baseUrl;
        private String energyPriceEndpoint;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
} 