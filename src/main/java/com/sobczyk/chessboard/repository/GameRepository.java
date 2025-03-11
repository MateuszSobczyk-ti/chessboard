package com.sobczyk.chessboard.repository;

import com.sobczyk.chessboard.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findById(Long id);
}
