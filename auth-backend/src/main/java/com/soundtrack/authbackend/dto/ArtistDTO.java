package com.soundtrack.authbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArtistDTO {
    private String name;
    private String genre;
    private Integer popularity;
    private Integer followers;

    }
