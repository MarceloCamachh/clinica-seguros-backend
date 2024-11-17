package com.marek_kawalski.clinic_system.triage.domain;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VitalSigns {
    @NotNull(message = "La temperatura es obligatoria")
    @DecimalMin(value = "30.0", message = "La temperatura mínima es 30°C")
    @DecimalMax(value = "45.0", message = "La temperatura máxima es 45°C")
    private Double temperature;
    
    @NotNull(message = "La presión arterial sistólica es obligatoria")
    @Min(value = 60, message = "La presión sistólica mínima es 60")
    @Max(value = 250, message = "La presión sistólica máxima es 250")
    private Integer systolicBP;
    
    @NotNull(message = "La presión arterial diastólica es obligatoria")
    @Min(value = 30, message = "La presión diastólica mínima es 30")
    @Max(value = 150, message = "La presión diastólica máxima es 150")
    private Integer diastolicBP;
    
    @NotNull(message = "La frecuencia cardíaca es obligatoria")
    @Min(value = 30, message = "La frecuencia cardíaca mínima es 30")
    @Max(value = 250, message = "La frecuencia cardíaca máxima es 250")
    private Integer heartRate;
    
    @NotNull(message = "La frecuencia respiratoria es obligatoria")
    @Min(value = 8, message = "La frecuencia respiratoria mínima es 8")
    @Max(value = 60, message = "La frecuencia respiratoria máxima es 60")
    private Integer respiratoryRate;
    
    @NotNull(message = "La saturación de oxígeno es obligatoria")
    @Min(value = 0, message = "La saturación mínima es 0")
    @Max(value = 100, message = "La saturación máxima es 100")
    private Integer oxygenSaturation;
    
    @Min(value = 20, message = "La glucemia mínima es 20")
    @Max(value = 600, message = "La glucemia máxima es 600")
    private Integer bloodGlucose;
}
