package com.marek_kawalski.clinic_system.user.dto;

import com.marek_kawalski.clinic_system.user.Address;
import com.marek_kawalski.clinic_system.user.UserRole;
import com.marek_kawalski.clinic_system.user.doctor.DoctorDetails;
import com.marek_kawalski.clinic_system.utils.Constants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateUpdateUserDTO(
        @Size(min = Constants.MIN_NAME_LENGTH, max = Constants.MAX_NAME_LENGTH)
        @NotBlank(message = "Name is mandatory")
        String name,
        @Size(min = Constants.MIN_SURNAME_LENGTH, max = Constants.MAX_SURNAME_LENGTH)
        @NotBlank(message = "Surname is mandatory")
        String surname,
        @Email
        String email,
        String password,
        UserRole role,
        String phoneNumber,
        String pesel,
        Address address,
        DoctorDetails doctorDetails,
        Boolean isEnabled
) {
}
