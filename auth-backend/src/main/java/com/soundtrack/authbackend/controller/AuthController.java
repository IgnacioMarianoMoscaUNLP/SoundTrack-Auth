package com.soundtrack.authbackend.controller;

import java.math.BigInteger;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
// import java.net.http.HttpHeaders; // Removed to avoid conflict with Spring's HttpHeaders
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
// import org.springframework.boot.web.servlet.server.Session; // Removed, use HttpSession instead
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Base64;

import com.soundtrack.authbackend.entity.SpotifySession;
import com.soundtrack.authbackend.service.AuthService;
import com.soundtrack.authbackend.service.JwtService;
import com.soundtrack.authbackend.service.SessionStore;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${spotify.client.id}")
    private String spotifyClientId;
    @Value("${spotify.client.secret}")
    private String spotifyClientSecret;
    @Autowired
    private SessionStore sessionStore;
    @Autowired
    private JwtService jwtService;


    @Autowired
    private AuthService authService;
    
    @GetMapping("/login")           // Redirige a Spotify OAuth
    public ResponseEntity<String> loginWithSpotify(HttpSession session) {        
        String state = generateRandomString(16);
        session.setAttribute("oauth_state", state);        
        
        String redirectUri = "http://127.0.0.1:8080/api/auth/callback"; // Reemplaza con tu URI de redirección
        String scope = "user-read-private " +
               "user-read-email " +
               "user-top-read " +
               "user-read-recently-played " +
               "user-library-read " +
               "user-library-modify " +
               "playlist-read-private " +
               "playlist-read-collaborative " +
               "user-follow-read " +
               "user-read-playback-state " +
               "user-modify-playback-state";      
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
public ResponseEntity callbackSpotify(
    @RequestParam(required = false) String code,
    @RequestParam(required = false) String state,
    HttpSession session) {

        
        
    System.out.println("Received code: " + code);
    /*if (!state.equals(session.getAttribute("oauth_state"))) {
        return redirectToErrorPage("Invalid state parameter");
    }*/

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
    System.out.println("Response from Spotify: " + response.getBody());
    System.out.println("Response status: " + response.getStatusCode());
    System.out.println("Response headers: " + response.getHeaders());
    if (response.getStatusCode().is2xxSuccessful()) {
        Map<String, Object> responseBody = response.getBody();
        System.out.println(responseBody.values());
        SpotifySession spotifySession = new SpotifySession();
        spotifySession.setAccessToken( responseBody.get("access_token").toString());        
        Integer expiresIn = (Integer) responseBody.get("expires_in");
        spotifySession.setExpiresAt(Instant.now().plus(expiresIn != null ? expiresIn : 3600, ChronoUnit.SECONDS));

        // 3. Crear un ID único de sesión
        String sessionId = UUID.randomUUID().toString();

        // 4. Guardar en el SessionStore
        sessionStore.save(sessionId, spotifySession);
        System.out.println("llego a la segunda");
        // 5. Generar un JWT con ese sessionId
        String jwt = jwtService.generateToken(sessionId);


    headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + spotifySession.getAccessToken());
    RestTemplate restTemplate1 = new RestTemplate();
    HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response2 = restTemplate.exchange(
            "https://api.spotify.com/v1/me",
            HttpMethod.GET,
            entity,
            Map.class
        );
        Map<String, Object> profile = response2.getBody();
        spotifySession.setUserId((String) profile.get("id"));
        sessionStore.save(sessionId, spotifySession); 
    String html = "<!DOCTYPE html>" +
                  "<html lang='es'>" +
                  "<head>" +
                  "  <meta charset='UTF-8'>" +
                  "  <title>Login exitoso</title>" +
                  "  <style>" +
                  "    body { font-family: Arial, sans-serif; text-align: center; margin-top: 50px; }" +
                  "    h1 { color: green; }" +
                  "    p.jwt { font-size: 24px; font-weight: bold; word-break: break-all; }" +
                  "  </style>" +
                  "</head>" +
                  "<body>" +
                  "  <h1>Login exitoso</h1>" +
                  "  <p>Usa este JWT en Postman o Docs para consumir la API:</p>" +
                  "  <p class='jwt'>" + jwt + "</p>" +
                  "</body>" +
                  "</html>";

    return ResponseEntity.ok()
            .contentType(MediaType.TEXT_HTML)
            .body(html);

    } else {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al obtener token de Spotify");
    }
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
