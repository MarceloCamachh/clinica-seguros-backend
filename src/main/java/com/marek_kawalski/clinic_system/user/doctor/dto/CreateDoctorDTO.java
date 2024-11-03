package com.marek_kawalski.clinic_system.user.doctor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record CreateDoctorDTO(
    @NotBlank(message = "El nombre es obligatorio")
    String name,
    
    @NotBlank(message = "El apellido es obligatorio")
    String surname,
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    String email,
    
    @NotBlank(message = "El número de teléfono es obligatorio")
    @Pattern(regexp = "\\d{9}", message = "El número de teléfono debe tener 9 dígitos")
    String phoneNumber,
    
    @NotBlank(message = "La especialización es obligatoria")
    String specialization,
    
    String education,
    String description
) {}
