package com.marek_kawalski.clinic_system.user;

import com.marek_kawalski.clinic_system.user.dto.CreateUpdateUserDTO;
import com.marek_kawalski.clinic_system.user.exception.UnauthorizedAccessException;
import com.marek_kawalski.clinic_system.user.exception.UserExistsException;
import com.marek_kawalski.clinic_system.user.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> createUpdateUser(final String userId, final CreateUpdateUserDTO createUpdateUserDTO) throws UserNotFoundException, UserExistsException, UnauthorizedAccessException {
        User user = new User();
        if (userId != null) {
            final Optional<User> oUser = userRepository.findById(userId);
            if (oUser.isEmpty()) throw new UserNotFoundException(String.format("User with id %s not found", userId));
            user = oUser.get();
            user.setUpdatedAt(LocalDateTime.now());
        } else {
            user.setCreatedAt(LocalDateTime.now());
        }

        user.setName(createUpdateUserDTO.name());
        user.setSurname(createUpdateUserDTO.surname());
        user.setEmail(validateEmail(user, createUpdateUserDTO.email()));
        user.setPhoneNumber(createUpdateUserDTO.phoneNumber());
        user.setPesel(createUpdateUserDTO.pesel());
        user.setAddress(createUpdateUserDTO.address());
        user.setDoctorDetails(createUpdateUserDTO.doctorDetails());

        if (createUpdateUserDTO.password() != null) {
            user.setPassword(bCryptPasswordEncoder.encode(createUpdateUserDTO.password()));
        }

        if (createUpdateUserDTO.role() == null || createUpdateUserDTO.role().equals(UserRole.ROLE_PATIENT)) {
            user.setUserRole(UserRole.ROLE_PATIENT);
        } else {
            checkAccess(List.of(UserRole.ROLE_ADMIN), "You are not authorized to change user role");
            user.setUserRole(createUpdateUserDTO.role());
        }
        if (createUpdateUserDTO.isEnabled() != null) {
            if (userId != null)
                checkAccess(List.of(UserRole.ROLE_ADMIN), "You are not authorized to change user isEnabled status");
            user.setEnabled(createUpdateUserDTO.isEnabled());
        }

        return Optional.of(userRepository.save(user));
    }

    @Override
    public Page<User> getPagedUsers(final UserRequestParams userRequestParams) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return Page.empty();

        if (authentication.getAuthorities()
                .stream()
                .anyMatch(grantedAuthority ->
                        UserRole.ROLE_PATIENT.equals(UserRole.valueOf(grantedAuthority.getAuthority())))) {
            userRequestParams.setRoles(List.of(UserRole.ROLE_DOCTOR));
        } else if (authentication.getAuthorities()
                .stream()
                .anyMatch(grantedAuthority ->
                        List.of(UserRole.ROLE_DOCTOR, UserRole.ROLE_REGISTRAR)
                                .contains(UserRole.valueOf(grantedAuthority.getAuthority())))) {
            userRequestParams.setRoles(List.of(UserRole.ROLE_DOCTOR, UserRole.ROLE_PATIENT, UserRole.ROLE_REGISTRAR));
        }

        return userRepository.getPagedUsers(userRequestParams);
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void save(final User user) {
        userRepository.save(user);
    }

    @Override
    public Optional<User> findById(final String userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> disableUser(final String userId) throws UserNotFoundException {
        final Optional<User> oUser = userRepository.findById(userId);
        if (oUser.isEmpty()) throw new UserNotFoundException(String.format("User with id %s not found", userId));
        final User user = oUser.get();
        user.setEnabled(false);
        return Optional.of(userRepository.save(user));
    }

    private void checkAccess(final List<UserRole> userRoles, final String unauthorizedMessage) throws UnauthorizedAccessException {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication != null && authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> userRoles.contains(UserRole.valueOf(grantedAuthority.getAuthority())))))
            throw new UnauthorizedAccessException(unauthorizedMessage);
    }

    private String validateEmail(final User user, final String email) throws UserExistsException {
        if (user.getEmail() != null && user.getEmail().equals(email)) {
            return email;
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserExistsException(String.format("User with email %s already exists", email));
        }

        return email;
    }
}
