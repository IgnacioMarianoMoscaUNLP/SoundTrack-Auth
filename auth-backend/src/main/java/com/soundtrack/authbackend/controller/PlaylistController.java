package com.soundtrack.authbackend.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sound.midi.Track;

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
import com.soundtrack.authbackend.dto.TrackDTO;
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

    class totalCount{
        private int total;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }
    @GetMapping("/favs")
    public ResponseEntity<List<TrackDTO>> getPlaylistTracks(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String jwt = authorizationHeader.substring(7);
        String sessionId = jwtService.validateAndGetSessionId(jwt);
        SpotifySession session = sessionStore.get(sessionId);
        List<TrackDTO> tracks = new ArrayList<>();
        int offset=0;
        
         totalCount total= new totalCount();
         total.setTotal(1000000000);
        for(int i=0; i<total.getTotal();i+=50){
          try{               
            tracks.addAll(this.favsTraks(session.getAccessToken(), 50, total));
            System.out.println("pagina:: "+i/50); 
            offset+=50;
            }catch(Exception e){
                break;
            }
        }
        return ResponseEntity.ok(tracks);
    }

    private List<TrackDTO> favsTraks(String token, int limit,totalCount total) {
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try{            
        LinkedHashMap<String, Object> response = restTemplate.exchange(
                "https://api.spotify.com/v1/me/tracks?limit="+limit,
                HttpMethod.GET,
                entity,
                LinkedHashMap.class
            ).getBody(); 
                List<Object> items = (List<Object>) response.get("items");
                total.setTotal((Integer) response.get("total"));
                List<TrackDTO> tracks = new ArrayList<>();
            for(Object itemObj : items) {
                LinkedHashMap<String, Object> item = (LinkedHashMap<String, Object>) itemObj;
                LinkedHashMap<String, Object> track =  item.get("track") instanceof LinkedHashMap ? (LinkedHashMap<String, Object>) item.get("track") : null;
                 if (track == null) {
                    continue; // Omitir si no hay información de la pista
                }
                TrackDTO trackDTO = new TrackDTO();
                trackDTO.setName((String) track.get("name"));                
                Map<String, Object> album = (Map<String, Object>) track.get("album");
                Map<String, Object> artist = ((List<Map<String, Object>>) track.get("artists")).get(0);
                trackDTO.setAlbum((String) album.get("name"));
                trackDTO.setArtist((String) artist.get("name"));
                tracks.add(trackDTO);
            }
        return tracks;

        }catch(Exception e){
            System.out.println(e.getMessage());
            throw e;
        }

        }
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
