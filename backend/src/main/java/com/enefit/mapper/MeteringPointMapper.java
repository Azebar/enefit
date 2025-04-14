package com.enefit.mapper;

import com.enefit.dto.MeteringPointDTO;
import com.enefit.model.MeteringPoint;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MeteringPointMapper {
    
    public MeteringPointDTO toDTO(MeteringPoint meteringPoint) {
        if (meteringPoint == null) {
            return null;
        }

        MeteringPointDTO dto = new MeteringPointDTO();
        dto.setMeteringPointId(meteringPoint.getMeteringPointId().toString());
        dto.setAddress(meteringPoint.getAddress());
        dto.setCustomerId(meteringPoint.getCustomer().getCustomerId());
        return dto;
    }
    
    public List<MeteringPointDTO> toDTOList(List<MeteringPoint> meteringPoints) {
        return meteringPoints.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
} 