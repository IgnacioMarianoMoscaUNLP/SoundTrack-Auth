package com.soundtrack.authbackend.controller;

import com.soundtrack.authbackend.dto.UserDTO;
import com.soundtrack.authbackend.entity.User;
import com.soundtrack.authbackend.service.AuthService;
import com.soundtrack.authbackend.service.SpotifyAuthService;

import java.math.BigInteger;
import java.net.URI;
// import java.net.http.HttpHeaders; // Removed to avoid conflict with Spring's HttpHeaders
import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private SpotifyAuthService  spotifyAuthService;

    @GetMapping("/login")           // Redirige a Spotify OAuth
    public ResponseEntity<String> loginWithSpotify() {
        String state = generateRandomString(16);
        String clientId = "your_spotify_client_id"; // Reemplaza con tu Client ID
        String redirectUri = "http://localhost:8080/api/auth/callback"; // Reemplaza con tu URI de redirección
        String scope = "user-read-private user-read-email user-top-read user-read-recently-played";
        

        String url = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("accounts.spotify.com")
                .path("/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("scope", scope)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("state", state)
                .build()
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url));

        return ResponseEntity.status(HttpStatus.FOUND) // 302
                .headers(headers)
                .build();
    }
    private String generateRandomString(int length) {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32).substring(0, length);
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
