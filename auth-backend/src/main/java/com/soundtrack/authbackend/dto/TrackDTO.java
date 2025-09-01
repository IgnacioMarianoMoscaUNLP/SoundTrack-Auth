package com.soundtrack.authbackend.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
public class TrackDTO {
    private String name;
    private String artist;
    private String album;
    private String releaseDate;
}
