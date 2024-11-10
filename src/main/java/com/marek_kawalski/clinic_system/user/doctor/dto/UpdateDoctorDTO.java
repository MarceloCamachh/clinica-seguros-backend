package com.marek_kawalski.clinic_system.user.doctor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record UpdateDoctorDTO(
    String name,
    String surname,
    
    @Email(message = "El formato del email no es válido")
    String email,
    
    @Pattern(regexp = "\\d{9}", message = "El número de teléfono debe tener 9 dígitos")
    String phoneNumber,
    
    String specialization,
    String education,
    String description
) {}
