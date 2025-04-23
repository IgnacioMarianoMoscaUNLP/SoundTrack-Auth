package com.soundtrack.authbackend.repository;

import com.soundtrack.authbackend.entity.FavoriteSong;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteSongRepository extends JpaRepository<FavoriteSong, Long> {
}
