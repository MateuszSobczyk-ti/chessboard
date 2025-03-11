package com.sobczyk.chessboard.repository;

import com.sobczyk.chessboard.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UnitRepository extends JpaRepository<Unit, Long> {

    List<Unit> findByGameIdAndPlayer(long gameId, Unit.Player player);

    Optional<Unit> findByIdAndDestroyedFalse(Long id);

    Optional<Unit> findByGameIdAndWidthPositionAndHeightPositionAndDestroyedFalse(Long gameId, int width, int height);
}
