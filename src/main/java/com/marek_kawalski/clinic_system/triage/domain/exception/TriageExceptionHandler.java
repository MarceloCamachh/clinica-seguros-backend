package com.marek_kawalski.clinic_system.triage.domain.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TriageExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(TriageExceptionHandler.class);

    @ExceptionHandler(TriageNotFoundException.class)
    public ResponseEntity<String> handleTriageNotFoundException(TriageNotFoundException ex) {
        logger.error("Error: Triaje no encontrado", ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(TriageValidationException.class)
    public ResponseEntity<String> handleTriageValidationException(TriageValidationException ex) {
        logger.error("Error: Validación de triaje fallida", ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
}
