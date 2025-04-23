package com.soundtrack.authbackend.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FavoriteSong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    private String song;
    private String artist;
    private String album;

    public FavoriteSong(String song, String artist, String album) {
        this.song = song;
        this.artist = artist;
        this.album = album;
    }
    public FavoriteSong() {}

}
