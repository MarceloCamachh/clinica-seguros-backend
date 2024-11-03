package com.marek_kawalski.clinic_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.TimeZone;

@ComponentScan(basePackages = "com.marek_kawalski.clinic_system.*")
@SpringBootApplication
public class ClinicSystemApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(ClinicSystemApplication.class, args);
    }

}
