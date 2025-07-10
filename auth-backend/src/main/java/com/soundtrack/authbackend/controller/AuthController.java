package com.soundtrack.authbackend.controller;

import com.soundtrack.authbackend.dto.UserDTO;
import com.soundtrack.authbackend.entity.User;
import com.soundtrack.authbackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/login")           // Redirige a Spotify OAuth
    public ResponseEntity<String> loginWithSpotify() {
        String authUrl = authService.getSpotifyAuthUrl();
        return ResponseEntity.status(302).header("Location", authUrl).build();
    }
    @GetMapping("/callback")        // Callback de Spotify
    public ResponseEntity<String> spotifyCallback() {
        // Aquí manejarías el callback de Spotify
        return ResponseEntity.ok("Callback handled");
    }
    @PostMapping("/refresh")        // Refresh token
    public ResponseEntity<String> refreshToken(@RequestBody String refreshToken) {
        // Aquí manejarías el refresh del token
        return ResponseEntity.ok("Token refreshed");
    }
    @GetMapping("/status")          // Estado de autenticación
    public ResponseEntity<String> authStatus() {
        // Aquí verificarías el estado de autenticación
        return ResponseEntity.ok("User is authenticated");
    }
}
