package com.enefit.mapper;

import com.enefit.dto.MeteringPointDto;
import com.enefit.entity.MeteringPoint;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MeteringPointMapper {
    
    @Mapping(source = "meteringPointId", target = "meteringPointId")
    @Mapping(source = "customer.customerId", target = "customerId")
    MeteringPointDto toDTO(MeteringPoint meteringPoint);
    
    List<MeteringPointDto> toDTOList(List<MeteringPoint> meteringPoints);
} 