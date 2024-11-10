package com.marek_kawalski.clinic_system.specialization.dto;

import lombok.Builder;

@Builder
public record SpecializationDTO(
    String id,
    String name,
    String description,
    boolean active,
    String createdAt,
    String updatedAt
) {} 