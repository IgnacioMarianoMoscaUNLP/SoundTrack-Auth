package com.soundtrack.authbackend.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users" +
        "")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.favoriteSongs = new ArrayList<>();
    }

    public User(){}

    @OneToMany(mappedBy = "user")
    @JoinTable(
            name="user_favoriteSongs",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "favorite_song_id")
    )
    private List<FavoriteSong>favoriteSongs;


}
