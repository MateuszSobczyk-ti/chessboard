package com.sobczyk.chessboard.service;

import com.sobczyk.chessboard.dto.request.ActionRequest;
import com.sobczyk.chessboard.dto.request.CoordinateDto;
import com.sobczyk.chessboard.dto.response.ActionResponse;
import com.sobczyk.chessboard.mapper.CommandMapper;
import com.sobczyk.chessboard.model.Command;
import com.sobczyk.chessboard.model.Unit;
import com.sobczyk.chessboard.model.UnitType;
import com.sobczyk.chessboard.repository.CommandRepository;
import com.sobczyk.chessboard.repository.UnitRepository;
import com.sobczyk.chessboard.validator.ActionTimeIntervalValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CommandsService {

    private final CommandRepository commandRepository;
    private final UnitRepository unitRepository;
    private final UnitsService unitsService;

    public void saveCommand(ActionRequest request) {
        Optional<Command> lastCommand = commandRepository.findTopByPlayerAndGameIdOrderByExecutionDateDesc(
                request.getPlayer(), request.getGameId());
        Command command = CommandMapper.INSTANCE.toCommand(request);
        commandRepository.save(command);
        lastCommand.ifPresent(ActionTimeIntervalValidator::validateActionTimeInterval);
    }

    public ResponseEntity<ActionResponse> createRandomCommand(Long unitId, Long gameId, Unit.Player player) {
        Unit unit = unitRepository.findByIdAndDestroyedFalse(unitId)
                .orElseThrow(() -> new RuntimeException("Unit not found"));
        ActionRequest actionRequest = createAction(unit, gameId, player);
        saveCommand(actionRequest);
        return unitsService.unitAction(actionRequest);
    }

    private ActionRequest createAction(Unit unit, Long gameId, Unit.Player player) {
        int maxDimension = Math.max(unit.getGame().getHeight(), unit.getGame().getWidth());
        ActionRequest actionRequest = ActionRequest.builder()
                .unitId(unit.getId())
                .gameId(gameId)
                .player(player)
                .build();
        if (UnitType.CANNON.equals(unit.getUnitType())) {
            actionRequest.setActionType(ActionRequest.ActionType.SHOT);
            actionRequest.setSecondCoordinateDto(new CoordinateDto(getRandomDirection(),
                    getRandomStep(unit.getUnitType(), ActionRequest.ActionType.SHOT, maxDimension)));
        } else {
            actionRequest.setActionType((UnitType.ARCHER.equals(unit.getUnitType())) ? getRandomArcherAction() : ActionRequest.ActionType.MOVE);
        }
        actionRequest.setFirstCoordinateDto(new CoordinateDto(getRandomDirection(),
                getRandomStep(unit.getUnitType(), actionRequest.getActionType(), maxDimension)));
        return actionRequest;
    }

    private CoordinateDto.Direction getRandomDirection() {
        Random random = new Random();
        CoordinateDto.Direction[] directions = CoordinateDto.Direction.values();
        return directions[random.nextInt(directions.length)];
    }

    private Integer getRandomStep(UnitType unitType, ActionRequest.ActionType actionType, int maxDimension) {
        Random random = new Random();
        return ActionRequest.ActionType.MOVE.equals(actionType) ? getRandomMoveStep(unitType) : random.nextInt(maxDimension-1) + 1;
    }

    private Integer getRandomMoveStep(UnitType unitType) {
        Random random = new Random();
        return switch (unitType) {
            case ARCHER -> 1;
            case TRANSPORT -> random.nextInt(3) + 1;
            default -> 0;
        };
    }

    private ActionRequest.ActionType getRandomArcherAction() {
        return new Random().nextBoolean() ? ActionRequest.ActionType.MOVE : ActionRequest.ActionType.SHOT;
    }
}
