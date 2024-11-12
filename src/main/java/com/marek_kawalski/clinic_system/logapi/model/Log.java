package com.marek_kawalski.clinic_system.logapi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "bitacoras")
public class Log {
    @Id
    private String id;
    private String username;  // Nombre del usuario que realizó la acción
    private String action;    // Descripción de la acción realizada
    private LocalDateTime timestamp; // Fecha y hora de la acción

    // Constructor, getters y setters
    public Log(String username, String action, LocalDateTime timestamp) {
        this.username = username;
        this.action = action;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}