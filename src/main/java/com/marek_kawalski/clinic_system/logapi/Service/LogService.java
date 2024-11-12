package com.marek_kawalski.clinic_system.logapi.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marek_kawalski.clinic_system.logapi.model.Log;
import com.marek_kawalski.clinic_system.logapi.repository.LogRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    public void registrarAccion(String username, String action) {
        Log log = new Log(username, action, LocalDateTime.now());
        System.out.println("Registrando acción: " + log);
        logRepository.save(log);
        System.out.println("Acción registrada con éxito");
    }

    public List<Log> obtenerBitacoras() {
        return logRepository.findAll();
    }
}
