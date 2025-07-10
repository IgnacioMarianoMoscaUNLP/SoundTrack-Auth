package com.soundtrack.authbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {
    
    @GetMapping("/throwback")       // Playlist de canciones antiguas
    public ResponseEntity<String> throwbackPlaylist() {
        // Aquí manejarías la lógica para la playlist de canciones antiguas
        return ResponseEntity.ok("Throwback playlist data");
    }
    @PostMapping("/create-from-period") // Crear playlist de un período
    public ResponseEntity<String> createPlaylistFromPeriod(@RequestBody String period) {
        // Aquí manejarías la lógica para crear una playlist a partir de un período
        return ResponseEntity.ok("Playlist created from period: " + period);
    }
    @GetMapping("/recommendations") // Recomendaciones basadas en timeline
    public ResponseEntity<String> recommendations() {
        // Aquí manejarías la lógica para las recomendaciones basadas en el timeline
        return ResponseEntity.ok("Recommendations data");
    }
    @GetMapping("/mood-based")      // Playlist basada en estado de ánimo
    public ResponseEntity<String> moodBasedPlaylist() {
        // Aquí manejarías la lógica para la playlist basada en el estado de ánimo
        return ResponseEntity.ok("Mood-based playlist data");   
    }
}
