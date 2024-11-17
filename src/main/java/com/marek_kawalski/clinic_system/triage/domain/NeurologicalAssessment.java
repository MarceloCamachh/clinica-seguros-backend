package com.marek_kawalski.clinic_system.triage.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NeurologicalAssessment {
    @Min(1) @Max(4)
    private Integer verbalResponse;    // 1-4
    
    @Min(1) @Max(4)
    private Integer eyeOpening;        // 1-4
    
    @Min(1) @Max(6)
    private Integer motorResponse;     // 1-6
    
    private String basicNeurologicalFunctions;
}
