package com.enefit.controller;

import com.enefit.dto.ConsumptionDTO;
import com.enefit.dto.MeteringPointResponse;
import com.enefit.service.ConsumptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/consumption")
@RequiredArgsConstructor
@Tag(name = "Consumption", description = "Consumption management APIs")
public class ConsumptionController {
    private final ConsumptionService consumptionService;

    @GetMapping("/metering-points")
    @Operation(summary = "Get all metering points for the authenticated user")
    public ResponseEntity<List<MeteringPointResponse>> getMeteringPoints(@RequestParam(required = false) String username) {
        return ResponseEntity.ok(consumptionService.getMeteringPointsForCustomer(username)
                .stream()
                .map(dto -> new MeteringPointResponse(
                        dto.getMeteringPointId(),
                        dto.getAddress(),
                        dto.getAddress()
                ))
                .collect(Collectors.toList()));
    }

    @GetMapping("/metering-point/{meteringPointId}")
    @Operation(summary = "Get consumption data for a metering point")
    public ResponseEntity<List<ConsumptionDTO>> getConsumptionByMeteringPoint(
            @PathVariable Long meteringPointId) {
        return ResponseEntity.ok(consumptionService.getConsumptionByMeteringPoint(meteringPointId));
    }

    @GetMapping("/metering-point/{meteringPointId}/date-range")
    @Operation(summary = "Get consumption data for a metering point within a date range")
    public ResponseEntity<List<ConsumptionDTO>> getConsumptionByMeteringPointAndDateRange(
            @PathVariable Long meteringPointId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(consumptionService.getConsumptionByMeteringPointAndDateRange(
                meteringPointId, startDate, endDate));
    }
} 