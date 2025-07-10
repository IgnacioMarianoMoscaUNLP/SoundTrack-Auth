package com.soundtrack.authbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    
    @GetMapping("/mood-trends")     // Tendencias de estado de ánimo
    public ResponseEntity<String> moodTrends() {
        // Aquí manejarías la lógica para las tendencias de estado de ánimo
        return ResponseEntity.ok("Mood trends data");
    }
    @GetMapping("/genre-evolution") // Evolución de géneros
    public ResponseEntity<String> genreEvolution() {
        // Aquí manejarías la lógica para la evolución de géneros
        return ResponseEntity.ok("Genre evolution data");
    }
    @GetMapping("/listening-patterns") // Patrones de escucha
    public ResponseEntity<String> listeningPatterns() {
        // Aquí manejarías la lógica para los patrones de escucha
        return ResponseEntity.ok("Listening patterns data");
    }
    @GetMapping("/artist-journey")  // Viaje por artistas
    public ResponseEntity<String> artistJourney() {
        // Aquí manejarías la lógica para el viaje por artistas
        return ResponseEntity.ok("Artist journey data");
    }
}
