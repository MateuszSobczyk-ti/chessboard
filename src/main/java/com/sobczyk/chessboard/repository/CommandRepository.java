package com.sobczyk.chessboard.repository;

import com.sobczyk.chessboard.model.Command;
import com.sobczyk.chessboard.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommandRepository extends JpaRepository<Command, Long> {
    Optional<Command> findTopByPlayerAndGameIdOrderByExecutionDateDesc(Unit.Player player, Long gameId);
}
