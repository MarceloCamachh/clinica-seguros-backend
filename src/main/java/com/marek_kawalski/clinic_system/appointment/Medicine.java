package com.marek_kawalski.clinic_system.appointment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Medicine {
    private String name;
    private String quantity;
    private String numberOfDays;
    private String dosage;
}
