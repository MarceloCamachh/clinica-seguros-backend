package com.marek_kawalski.clinic_system.specialization.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CreateUpdateSpecializationDTO(
    @NotBlank(message = "El nombre es obligatorio")
    String name,
    
    String description,
    
    boolean active
) {}
