package com.marek_kawalski.clinic_system.utils;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
@JacksonAnnotationsInside
@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss.SSS'Z'")
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomTimeFormat {
}
