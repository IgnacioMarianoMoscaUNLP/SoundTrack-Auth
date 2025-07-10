package com.soundtrack.authbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/timeline")
public class TimeLineController {
    
    @GetMapping("/recent")          // Últimas 2-4 semanas
    public ResponseEntity<String> recent() {
        // Aquí manejarías la lógica para las últimas semanas
        return ResponseEntity.ok("Recent tracks");
    }
    @GetMapping("/medium")          // Últimos 6 meses
    public ResponseEntity<String> medium() {
        // Aquí manejarías la lógica para los últimos 6 meses
        return ResponseEntity.ok("Medium term tracks");
    }
    @GetMapping("/long")            // Años de historial
    public ResponseEntity<String> longTerm() {
        // Aquí manejarías la lógica para el historial de años
        return ResponseEntity.ok("Long term tracks");
    }
    @GetMapping("/evolution")       // Evolución de gustos musicales
    public ResponseEntity<String> evolution() {
        // Aquí manejarías la lógica para la evolución de gustos
        return ResponseEntity.ok("Musical evolution");
    }
    @GetMapping("/rediscover")      // Canciones "olvidadas"
    public ResponseEntity<String> rediscover() {
        // Aquí manejarías la lógica para redescubrir canciones
        return ResponseEntity.ok("Rediscovered tracks");
    }
}