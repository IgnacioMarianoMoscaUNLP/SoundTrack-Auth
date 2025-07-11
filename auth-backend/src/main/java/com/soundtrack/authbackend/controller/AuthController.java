package com.soundtrack.authbackend.controller;

import java.math.BigInteger;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
// import java.net.http.HttpHeaders; // Removed to avoid conflict with Spring's HttpHeaders
import java.security.SecureRandom;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Base64;
import com.soundtrack.authbackend.service.AuthService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${spotify.client.id}")
    private String spotifyClientId;
    @Value("${spotify.client.secret}")
    private String spotifyClientSecret;


    @Autowired
    private AuthService authService;
    
    @GetMapping("/login")           // Redirige a Spotify OAuth
    public ResponseEntity<String> loginWithSpotify() {
        System.out.println("Login with Spotify called");
        String state = generateRandomString(16);        
        String redirectUri = "http://127.0.0.1:8080/api/auth/callback"; // Reemplaza con tu URI de redirección
        String scope = "user-read-private user-read-email user-top-read user-read-recently-played";        
        String encodedScope = URLEncoder.encode(scope, StandardCharsets.UTF_8);

        // Guardar el estado en la sesión para validación posterior
        //HttpSession session = authService.getCurrentSession();
        //session.setAttribute("oauth_state", state);
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
    public ResponseEntity<?> callbackSpotify(
        @RequestParam(required = false) String code,
        @RequestParam(required = false) String state,
        HttpSession session) {

    String sessionState = (String) session.getAttribute("oauth_state");

    if (state == null) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, "/#?error=state_mismatch")
                .build();
    }

    // Intercambio de code por access_token
    String credentials = spotifyClientId + ":" + spotifyClientSecret;
    String encodedCredentials = Base64.getEncoder()
            .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.set("Authorization", "Basic " + encodedCredentials);

    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type", "authorization_code");
    body.add("code", code);
    body.add("redirect_uri", "http://127.0.0.1:8080/api/auth/callback");

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<Map> response = restTemplate.postForEntity(
            "https://accounts.spotify.com/api/token",
            request,
            Map.class
    );

    if (response.getStatusCode().is2xxSuccessful()) {
        Map<String, Object> responseBody = response.getBody();

        // Por ejemplo, guardar token en sesión o devolver al frontend
        session.setAttribute("access_token", responseBody.get("access_token"));

        return ResponseEntity.ok(responseBody);
    } else {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al obtener token de Spotify");
    }
}


    @PostMapping("/token")          // Obtiene el token de acceso
    public ResponseEntity<String> getAccessToken(@RequestBody String code) {
        // Aquí manejarías el intercambio del código por un token de acceso
        return ResponseEntity.ok("Access token for code: " + code);
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
