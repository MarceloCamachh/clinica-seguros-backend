package com.marek_kawalski.clinic_system.user;

import com.marek_kawalski.clinic_system.user.doctor.DoctorDetailsMapper;
import com.marek_kawalski.clinic_system.user.dto.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserMapper {
    private final DoctorDetailsMapper doctorDetailsMapper;

    public UserDTO mapUserToUserDTO(final User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .role(user.getUserRole())
                .phoneNumber(user.getPhoneNumber())
                .pesel(user.getPesel())
                .address(user.getAddress())
                .createdAt(user.getCreatedAt())
                .lastLogin(user.getLastLogin())
                .updatedAt(user.getUpdatedAt())
                .isEnabled(user.isEnabled())
                .doctorDetails(
                        user.getDoctorDetails() == null ? null :
                                doctorDetailsMapper.mapDoctorDetailsToDoctorDetailsDTO(user.getDoctorDetails())
                )
                .build();
    }
}
