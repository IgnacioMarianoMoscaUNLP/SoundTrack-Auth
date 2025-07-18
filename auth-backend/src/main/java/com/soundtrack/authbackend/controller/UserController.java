package com.soundtrack.authbackend.controller;

import java.time.Instant;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.soundtrack.authbackend.dto.UserDTO;
import com.soundtrack.authbackend.entity.SpotifySession;
import com.soundtrack.authbackend.service.JwtService;
import com.soundtrack.authbackend.service.SessionStore;

import jakarta.servlet.http.HttpServletRequest;

// import io.jsonwebtoken.Jwts;
import org.springframework.security.oauth2.jwt.Jwt;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private SessionStore sessionStore;
    @Autowired
    private JwtService jwtService;
    @GetMapping(value="/random")//, produces = MediaType.APPLICATION_JSON_VALUE)         // Endpoint de prueba
    public ResponseEntity<UserDTO> random() {
        return ResponseEntity.ok(new UserDTO());
    }

    @GetMapping("/profile")         // Perfil básico del usuario
    public ResponseEntity getProfile(HttpServletRequest  request) {
        String authorizationHeader = request.getHeader("Authorization");
        String jwt = authorizationHeader.substring(7);
        String sessionId = jwtService.validateAndGetSessionId(jwt);
        SpotifySession session = sessionStore.get(sessionId);
    
        if (session == null || session.getExpiresAt().isBefore(Instant.now())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        System.out.println("Session: " + session.getAccessToken());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + session.getAccessToken());

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(
            "https://api.spotify.com/v1/me",
            org.springframework.http.HttpMethod.GET,
            entity,
            Map.class
        );
        System.out.println("Response from Spotify: " + response);

        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping("/preferences")     // Preferencias de la app
    public ResponseEntity<UserDTO> getPreferences() {
        return ResponseEntity.ok(new UserDTO());
    }

    @PutMapping("/preferences")     // Actualizar preferencias
    public ResponseEntity<UserDTO> updatePreferences(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userDTO);
    }
}

