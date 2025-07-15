package com.soundtrack.authbackend.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.soundtrack.authbackend.entity.SpotifySession;

@Component
public class SessionStore {
    private final Map<String, SpotifySession> sessions = new ConcurrentHashMap<>();

    public void save(String sessionId, SpotifySession session) {
        sessions.put(sessionId, session);
    }

    public SpotifySession get(String sessionId) {
        return sessions.get(sessionId);
    }
}
