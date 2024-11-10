package com.marek_kawalski.clinic_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.TimeZone;

@SpringBootApplication
@EnableWebMvc
public class ClinicSystemApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(ClinicSystemApplication.class, args);
    }

}
