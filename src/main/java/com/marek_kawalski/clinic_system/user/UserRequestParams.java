package com.marek_kawalski.clinic_system.user;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Sort;

import java.util.List;

@Builder
@Data
public class UserRequestParams {
    private String sortField;
    private Sort.Direction sortDirection;
    private Integer pageSize;
    private Integer pageNumber;
    private String search;
    private boolean showDisabled;
    private List<UserRole> roles;
}
