package com.soundtrack.authbackend.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.soundtrack.authbackend.entity.FavoriteSong;


public interface FavoriteSongRepository extends MongoRepository<FavoriteSong, ObjectId> {
    
    // Custom query methods can be defined here if needed
    // For example, to find favorite songs by user ID:
    // List<FavoriteSong> findByUserId(String userId);
}
