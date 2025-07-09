package com.soundtrack.authbackend.entity;

import com.soundtrack.authbackend.entity.FavoriteSong;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Document(collection = "users")
@Getter
@Setter
public class User implements UserDetails {
    @Id
    
    private ObjectId id;

    
    private String email;
    
    private String password;


    public User(String username, String password) {
        this.email = username;
        this.password = password;
        this.favoriteSongs = new ArrayList<>();
    }

    public User(){}

    
    private List<FavoriteSong>favoriteSongs;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
