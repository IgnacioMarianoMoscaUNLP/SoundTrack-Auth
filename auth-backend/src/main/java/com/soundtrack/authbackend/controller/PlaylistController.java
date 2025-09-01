package com.soundtrack.authbackend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.soundtrack.authbackend.dto.ArtistDTO;
import com.soundtrack.authbackend.dto.PlaylistDTO;
import com.soundtrack.authbackend.entity.SpotifySession;
import com.soundtrack.authbackend.service.JwtService;
import com.soundtrack.authbackend.service.SessionStore;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {
    @Autowired
    private SessionStore sessionStore;
    @Autowired
    private JwtService jwtService;
    
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
    @GetMapping("/all")     
    public ResponseEntity<List> getPreferences(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
    
         String jwt = authorizationHeader.substring(7);
        String sessionId = jwtService.validateAndGetSessionId(jwt);
        SpotifySession session = sessionStore.get(sessionId);

    

        HttpHeaders headers = new HttpHeaders();
        String id = session.getUserId();
        headers.set("Authorization", "Bearer " + session.getAccessToken());
        
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Map<String, Object>> response =
          restTemplate.exchange(
        "https://api.spotify.com/v1/users/"+id+"/playlists",
             HttpMethod.GET,
             entity
                , (Class<Map<String, Object>>)(Class<?>)Map.class
        );
        List<Map<String, Object>> items = (List<Map<String, Object>>) response.getBody().get("items");
        List Playlists= new ArrayList<>();        
        for (Map<String, Object> item : items) {     
            PlaylistDTO playlistDTO = new PlaylistDTO();
            playlistDTO.setId((String) item.get("id"));
            playlistDTO.setName((String) item.get("name"));
            playlistDTO.setDescription((String) item.get("description"));
            Map<String, Object> owner = (Map<String, Object>) item.get("owner");
            playlistDTO.setOwner(owner.get("display_name").toString());
            Map<String, Object> tracks = (Map<String, Object>) item.get("tracks");
            playlistDTO.setTracksCount((Integer) tracks.get("total"));
            Playlists.add(playlistDTO);
        }
        return ResponseEntity.ok(Playlists);
    }
}
