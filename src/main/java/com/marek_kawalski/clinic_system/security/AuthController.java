package com.marek_kawalski.clinic_system.security;

import com.marek_kawalski.clinic_system.user.User;
import com.marek_kawalski.clinic_system.user.UserMapper;
import com.marek_kawalski.clinic_system.user.UserService;
import com.marek_kawalski.clinic_system.user.dto.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final User user = (User) authentication.getPrincipal();
        user.setLastLogin(LocalDateTime.now());
        userService.save(user);

        return ResponseEntity.status(HttpStatus.OK).
                body(userMapper.mapUserToUserDTO(user));
    }

}
