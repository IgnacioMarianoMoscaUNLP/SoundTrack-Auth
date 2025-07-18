package com.soundtrack.authbackend.entity;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpotifySession {
    
    private Object accessToken;
    private Instant expiresAt;
    private String displayName;
    // getters/setters
}
