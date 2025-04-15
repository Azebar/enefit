package com.enefit.controller;

import com.enefit.dto.ConsumptionDto;
import com.enefit.dto.MeteringPointDto;
import com.enefit.service.ConsumptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consumption")
@RequiredArgsConstructor
@Tag(name = "Consumption", description = "Consumption management APIs")
public class ConsumptionController {
    private final ConsumptionService consumptionService;

    @GetMapping("/metering-points")
    @Operation(summary = "Get all metering points for the authenticated user")
    public ResponseEntity<List<MeteringPointDto>> getMeteringPoints(@RequestParam(required = false) Long customerId) {
        return ResponseEntity.ok(consumptionService.getMeteringPointsForCustomer(customerId));
    }

    @GetMapping("/metering-point/{meteringPointId}")
    @Operation(summary = "Get consumption data for a metering point")
    public ResponseEntity<List<ConsumptionDto>> getConsumptionByMeteringPoint(
            @PathVariable Long meteringPointId) {
        return ResponseEntity.ok(consumptionService.getConsumptionByMeteringPoint(meteringPointId));
    }
} 