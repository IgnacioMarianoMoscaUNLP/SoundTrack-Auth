package com.soundtrack.authbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlaylistDTO {
    private String id;
    private String name;
    private String description;
    private String owner;
    private Integer tracksCount;
    
    
}
