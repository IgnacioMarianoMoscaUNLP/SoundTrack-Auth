package com.soundtrack.authbackend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    
    @GetMapping("/mood-trends")     // Tendencias de estado de ánimo
    @GetMapping("/genre-evolution") // Evolución de géneros
    @GetMapping("/listening-patterns") // Patrones de escucha
    @GetMapping("/artist-journey")  // Viaje por artistas
}
