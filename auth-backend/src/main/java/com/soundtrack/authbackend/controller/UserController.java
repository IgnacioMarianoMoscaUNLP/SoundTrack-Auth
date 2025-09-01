package com.soundtrack.authbackend.controller;

import java.net.http.HttpRequest;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import com.soundtrack.authbackend.dto.ArtistDTO;

import com.soundtrack.authbackend.dto.TrackDTO;
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
    @GetMapping(value="/topTracks")//, produces = MediaType.APPLICATION_JSON_VALUE)         // Endpoint de prueba
    public ResponseEntity<List<TrackDTO>> topTracks(HttpServletRequest request, RestTemplate restTemplate2) {
        String authorizationHeader = request.getHeader("Authorization");
    
         String jwt = authorizationHeader.substring(7);
        String sessionId = jwtService.validateAndGetSessionId(jwt);
        SpotifySession session = sessionStore.get(sessionId);

    

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + session.getAccessToken());
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Map<String, Object>> response =
          restTemplate.exchange(
        "https://api.spotify.com/v1/me/top/tracks",
             HttpMethod.GET,
             entity
                , (Class<Map<String, Object>>)(Class<?>)Map.class
        );
        List<Map<String, Object>> items = (List<Map<String, Object>>) response.getBody().get("items");
        List<TrackDTO> topTracks = new ArrayList<>();
        for (Map<String, Object> item : items) {     
            TrackDTO track = new TrackDTO();       
            track.setName((String) item.get("name"));
            Map<String, Object> album = (Map<String, Object>) item.get("album");
            track.setAlbum((String) album.get("name"));
            List<Map<String, Object>> artists = (List<Map<String, Object>>) item.get("artists");
            if (!artists.isEmpty()) {
                track.setArtist((String) artists.get(0).get("name"));
            }
            topTracks.add(track);
        }
        return ResponseEntity.ok(topTracks);        
    }

    @GetMapping("/profile")
    public ResponseEntity<Map> getProfile(HttpServletRequest request) {
    String authorizationHeader = request.getHeader("Authorization");
    
    String jwt = authorizationHeader.substring(7);
    String sessionId = jwtService.validateAndGetSessionId(jwt);
    SpotifySession session = sessionStore.get(sessionId);

    

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + session.getAccessToken());
    RestTemplate restTemplate = new RestTemplate();
    HttpEntity<Void> entity = new HttpEntity<>(headers);

        System.out.println("Fetching profile with access token: " + session.getAccessToken()); 
        ResponseEntity<Map> response = restTemplate.exchange(
            "https://api.spotify.com/v1/me",
            HttpMethod.GET,
            entity,
            Map.class
        );
        return ResponseEntity.ok(response.getBody());
}


    @GetMapping("/topArtists")     
    public ResponseEntity<List<ArtistDTO>> getPreferences(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
    
         String jwt = authorizationHeader.substring(7);
        String sessionId = jwtService.validateAndGetSessionId(jwt);
        SpotifySession session = sessionStore.get(sessionId);

    

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + session.getAccessToken());
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Map<String, Object>> response =
          restTemplate.exchange(
        "https://api.spotify.com/v1/me/top/artists",
             HttpMethod.GET,
             entity
                , (Class<Map<String, Object>>)(Class<?>)Map.class
        );
        List<Map<String, Object>> items = (List<Map<String, Object>>) response.getBody().get("items");
        List<ArtistDTO> topArtist= new ArrayList<>();
        for (Map<String, Object> item : items) {     
            ArtistDTO artist = new ArtistDTO();
            artist.setName((String) item.get("name"));
            artist.setGenre((String) item.get("genres").toString());
            artist.setPopularity((Integer) item.get("popularity"));

            Map<String, Object> followers = (Map<String, Object>) item.get("followers");
            artist.setFollowers((Integer) followers.get("total"));            
            topArtist.add(artist);
        }
        return ResponseEntity.ok(topArtist);
    }

    @PutMapping("/preferences")     // Actualizar preferencias
    public ResponseEntity<UserDTO> updatePreferences(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userDTO);
    }
}

