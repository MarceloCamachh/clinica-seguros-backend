package com.marek_kawalski.clinic_system.triage.domain.enums;

public enum TriagePriority {
    LEVEL_1("Resucitación", "Rojo"),      // Atención inmediata
    LEVEL_2("Emergencia", "Naranja"),      // Hasta 10 minutos
    LEVEL_3("Urgente", "Amarillo"),        // Hasta 60 minutos
    LEVEL_4("Menor Urgencia", "Verde"),    // Hasta 120 minutos
    LEVEL_5("No Urgente", "Azul");         // Hasta 240 minutos

    private final String description;
    private final String color;

    TriagePriority(String description, String color) {
        this.description = description;
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public String getColor() {
        return color;
    }
}