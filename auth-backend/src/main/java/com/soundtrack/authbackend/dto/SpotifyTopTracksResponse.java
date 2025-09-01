package com.soundtrack.authbackend.dto;

import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
public class SpotifyTopTracksResponse {
    private List<TrackDTO> tracks;

    // getters y setters
}
