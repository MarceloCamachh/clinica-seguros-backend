package com.marek_kawalski.clinic_system.user;

import com.marek_kawalski.clinic_system.user.dto.CreateUpdateUserDTO;
import com.marek_kawalski.clinic_system.user.exception.UnauthorizedAccessException;
import com.marek_kawalski.clinic_system.user.exception.UserExistsException;
import com.marek_kawalski.clinic_system.user.exception.UserNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();

    Optional<User> createUpdateUser(final String userId, final CreateUpdateUserDTO createUpdateUserDTO) throws UserNotFoundException, UserExistsException, UnauthorizedAccessException;

    Page<User> getPagedUsers(final UserRequestParams userRequestParams);

    Optional<User> findByEmail(final String email);

    void save(final User user);

    Optional<User> findById(final String userId);

    Optional<User> disableUser(final String userId) throws UserNotFoundException;
}
