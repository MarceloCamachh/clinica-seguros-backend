package com.marek_kawalski.clinic_system.logapi.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.marek_kawalski.clinic_system.logapi.Service.LogService;
import com.marek_kawalski.clinic_system.logapi.model.Log;

import java.util.List;

@RestController
@RequestMapping("/api/bitacora")
public class LogController {

    @Autowired
    private LogService logService;

    @PostMapping("/registrar")
    public void registrarAccion(@RequestParam String username, @RequestParam String action) {
        logService.registrarAccion(username, action);
    }

    @GetMapping("/listar")
    public List<Log> obtenerBitacoras() {
        return logService.obtenerBitacoras();
    }
}

