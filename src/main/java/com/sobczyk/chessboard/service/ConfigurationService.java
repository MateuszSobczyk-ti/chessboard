package com.sobczyk.chessboard.service;

import com.sobczyk.chessboard.dto.request.ConfigurationRequest;
import com.sobczyk.chessboard.dto.response.GameResponse;
import com.sobczyk.chessboard.dto.response.UnitsResponse;
import com.sobczyk.chessboard.exception.TooManyUnitsException;
import com.sobczyk.chessboard.mapper.UnitMapper;
import com.sobczyk.chessboard.model.Game;
import com.sobczyk.chessboard.model.Unit;
import com.sobczyk.chessboard.model.UnitType;
import com.sobczyk.chessboard.repository.GameRepository;
import com.sobczyk.chessboard.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConfigurationService {

    private final GameRepository gameRepository;
    private final UnitRepository unitRepository;

    public ResponseEntity<GameResponse> saveConfiguration(ConfigurationRequest request, Long oldGameId) {
        if (getTotalUnitCount(request) > request.height() * request.width()) {
            throw new TooManyUnitsException();
        }
        if (oldGameId != null) {
            archiveGame(oldGameId);
        }
        Game game = Game.builder()
                .width(request.width())
                .height(request.height())
                .build();
        gameRepository.save(game);
        List<UnitsResponse> unitsResponses = saveUnits(game, request);
        log.info("New game configuration set!");
        return ResponseEntity.ok(GameResponse.builder()
                .gameId(game.getId())
                .start(game.getStart())
                .units(unitsResponses)
                .build());
    }

    private int getTotalUnitCount(ConfigurationRequest request) {
        return request.whiteUnits().archer() + request.whiteUnits().cannon() + request.whiteUnits().transport()
                + request.blackUnits().archer() + request.blackUnits().cannon() + request.blackUnits().transport();
    }

    private List<UnitsResponse> saveUnits(Game game, ConfigurationRequest request) {
        List<int[]> positions = PositionsGenerator.generateRandomPositions(getTotalUnitCount(request), request.width(), request.height());
        List<Unit> units = new ArrayList<>();
        units.addAll(createUnits(game, request.whiteUnits().archer(), UnitType.ARCHER, Unit.Player.WHITE, positions));
        units.addAll(createUnits(game, request.whiteUnits().transport(), UnitType.TRANSPORT, Unit.Player.WHITE, positions));
        units.addAll(createUnits(game, request.whiteUnits().cannon(), UnitType.CANNON, Unit.Player.WHITE, positions));

        units.addAll(createUnits(game, request.blackUnits().archer(), UnitType.ARCHER, Unit.Player.BLACK, positions));
        units.addAll(createUnits(game, request.blackUnits().transport(), UnitType.TRANSPORT, Unit.Player.BLACK, positions));
        units.addAll(createUnits(game, request.blackUnits().cannon(), UnitType.CANNON, Unit.Player.BLACK, positions));
        unitRepository.saveAll(units);
        return UnitMapper.INSTANCE.toResponseList(units);
    }

    private List<Unit> createUnits(Game game, int quantity, UnitType unitType, Unit.Player player, List<int[]> positions) {
        List<Unit> units = new ArrayList<>();
        for (int i=0; i<quantity; i++) {
            units.add(Unit.builder()
                    .game(game)
                    .unitType(unitType)
                    .player(player)
                    .widthPosition(positions.getFirst()[0])
                    .heightPosition(positions.getFirst()[1])
                    .build());
            positions.removeFirst();
        }
        return units;
    }

    private void archiveGame(Long gameId) {
        Optional<Game> game = gameRepository.findById(gameId);
        if (game.isPresent()) {
            game.get().setStop(LocalDateTime.now());
            game.get().setArchived(true);
            gameRepository.save(game.get());
        }
    }
}
