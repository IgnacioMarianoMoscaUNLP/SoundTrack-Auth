package com.soundtrack.authbackend.service;

public class SpotifyAuthService {
    public String generateAuthUrl() {
        return "https://accounts.spotify.com/authorize" +
               "?response_type=code" +
               "&client_id=" + clientId +
               "&scope=user-read-private user-read-email user-top-read user-read-recently-played" +
               "&redirect_uri=" + redirectUri;
    }
}
