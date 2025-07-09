package com.soundtrack.authbackend.entity;



import org.springframework.data.mongodb.core.mapping.Document;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import lombok.Getter;
import lombok.Setter;

@Document(collection = "favs")
@Getter
@Setter
public class FavoriteSong {
    @Id
    private ObjectId id;
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
