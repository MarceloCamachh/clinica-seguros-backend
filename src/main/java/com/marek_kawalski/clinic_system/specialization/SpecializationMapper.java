package com.marek_kawalski.clinic_system.specialization;

import com.marek_kawalski.clinic_system.specialization.dto.SpecializationDTO;
import org.springframework.stereotype.Component;

@Component
public class SpecializationMapper {
    
    public SpecializationDTO toDTO(Specialization specialization) {
        return SpecializationDTO.builder()
                .id(specialization.getId())
                .name(specialization.getName())
                .description(specialization.getDescription())
                .active(specialization.isActive())
                .createdAt(specialization.getCreatedAt().toString())
                .updatedAt(specialization.getUpdatedAt().toString())
                .build();
    }
} 