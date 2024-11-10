package com.marek_kawalski.clinic_system.user;

import org.springframework.data.domain.Page;

public interface UserRepositoryCustom {
    Page<User> getPagedUsers(UserRequestParams userRequestParams);
}
