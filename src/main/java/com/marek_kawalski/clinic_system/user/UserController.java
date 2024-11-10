package com.marek_kawalski.clinic_system.user;

import com.marek_kawalski.clinic_system.user.dto.CreateUpdateUserDTO;
import com.marek_kawalski.clinic_system.user.dto.UserDTO;
import com.marek_kawalski.clinic_system.user.exception.UnauthorizedAccessException;
import com.marek_kawalski.clinic_system.user.exception.UserExistsException;
import com.marek_kawalski.clinic_system.user.exception.UserNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_REGISTRAR', 'ROLE_DOCTOR')")
    @GetMapping("users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") final String userId) {
        return userService.findById(userId)
                .map(user -> ResponseEntity.status(HttpStatus.OK)
                        .body(userMapper.mapUserToUserDTO(user)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_REGISTRAR', 'ROLE_DOCTOR')")
    @GetMapping("users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        final List<User> users = userService.getAllUsers();
        return users.isEmpty() ?
                ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(null)
                : ResponseEntity.status(HttpStatus.OK)
                .body(users.stream().map(userMapper::mapUserToUserDTO).toList());
    }

    @GetMapping("/users/paged")
    public ResponseEntity<Page<UserDTO>> getPagedUsers(@RequestParam(value = "sort", defaultValue = "createdAt") final String sortField,
                                                       @RequestParam(value = "sort-dir", defaultValue = "DESC") final Sort.Direction sortDirection,
                                                       @RequestParam(value = "page-size", defaultValue = "10") final Integer pageSize,
                                                       @RequestParam(value = "page-num", defaultValue = "0") final Integer pageNum,
                                                       @RequestParam(value = "search", required = false) final String search,
                                                       @RequestParam(value = "show-disabled", defaultValue = "false") final boolean showDisabled,
                                                       @RequestParam(value = "roles", required = false) final List<UserRole> roles) {
        final Page<User> pagedUsers = userService.getPagedUsers(UserRequestParams.builder()
                .sortField(sortField)
                .sortDirection(sortDirection)
                .pageSize(pageSize)
                .pageNumber(pageNum)
                .search(search)
                .roles(roles)
                .showDisabled(showDisabled)
                .build());

        return pagedUsers.isEmpty() ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(null) :
                ResponseEntity.status(HttpStatus.OK).body(pagedUsers.map(userMapper::mapUserToUserDTO));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("id") final String userId, @Valid @RequestBody final CreateUpdateUserDTO createUpdateUserDTO) {
        try {
            return userService.createUpdateUser(userId, createUpdateUserDTO)
                    .map(user -> ResponseEntity.status(HttpStatus.OK)
                            .body(userMapper.mapUserToUserDTO(user)))
                    .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e.getCause());
        } catch (UserExistsException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e.getCause());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, e.getMessage(), e.getCause());
        }
    }

    @PostMapping("registration")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody final CreateUpdateUserDTO createUpdateUserDTO) {
        try {
            return userService.createUpdateUser(null, createUpdateUserDTO)
                    .map(user -> ResponseEntity.status(HttpStatus.CREATED)
                            .body(userMapper.mapUserToUserDTO(user)))
                    .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e.getCause());
        } catch (UserExistsException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e.getCause());
        } catch (UnauthorizedAccessException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, e.getMessage(), e.getCause());
        }
    }

    @PatchMapping("/users/{id}/disable")
    public ResponseEntity<UserDTO> disableUser(@PathVariable("id") final String userId) {
        try {
            return userService.disableUser(userId)
                    .map(user -> ResponseEntity.status(HttpStatus.OK)
                            .body(userMapper.mapUserToUserDTO(user)))
                    .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e.getCause());
        }
    }

}
