package com.soundtrack.authbackend.controller;

import java.math.BigInteger;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
// import java.net.http.HttpHeaders; // Removed to avoid conflict with Spring's HttpHeaders
import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.soundtrack.authbackend.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${spotify.client.id}")
    private String spotifyClientId;

    @Autowired
    private AuthService authService;
    
    @GetMapping("/login")           // Redirige a Spotify OAuth
    public ResponseEntity<String> loginWithSpotify() {
        System.out.println("Login with Spotify called");
        String state = generateRandomString(16);        
        String redirectUri = "http://127.0.0.1:8080/api/auth/callback"; // Reemplaza con tu URI de redirección
        String scope = "user-read-private user-read-email user-top-read user-read-recently-played";        
        String encodedScope = URLEncoder.encode(scope, StandardCharsets.UTF_8);


        String url = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("accounts.spotify.com")
                .path("/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", spotifyClientId)
                .queryParam("scope", encodedScope)
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
    @GetMapping("/callback")
    public ResponseEntity<String> spotifyCallback(
        @RequestParam("code") String code,
        @RequestParam("state") String state) {

    // Aquí deberías intercambiar el code por un access_token con un POST a https://accounts.spotify.com/api/token

        return ResponseEntity.ok("Received code: " + code);
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
