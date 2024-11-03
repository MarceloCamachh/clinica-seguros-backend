package com.marek_kawalski.clinic_system.user;

import com.marek_kawalski.clinic_system.user.doctor.DoctorDetails;
import com.marek_kawalski.clinic_system.utils.Constants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "users")
public class User implements UserDetails {
    @Id
    private String id;

    @Size(min = Constants.MIN_NAME_LENGTH, max = Constants.MAX_NAME_LENGTH)
    @NotBlank(message = "Name is mandatory")
    private String name;

    @Size(min = Constants.MIN_SURNAME_LENGTH, max = Constants.MAX_SURNAME_LENGTH)
    @NotBlank(message = "Surname is mandatory")
    private String surname;

    @Indexed(unique = true)
    @Email
    private String email;

    private String password;

    private UserRole userRole;

    private boolean isEnabled = true;

    private String phoneNumber;

    private String pesel;

    private Address address;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime lastLogin;

    // Role specific fields
    private DoctorDetails doctorDetails;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(userRole.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
