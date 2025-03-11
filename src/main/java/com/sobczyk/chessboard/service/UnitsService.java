package com.sobczyk.chessboard.service;

import com.sobczyk.chessboard.dto.request.ActionRequest;
import com.sobczyk.chessboard.dto.response.ActionResponse;
import com.sobczyk.chessboard.dto.response.UnitsResponse;
import com.sobczyk.chessboard.mapper.UnitMapper;
import com.sobczyk.chessboard.model.Unit;
import com.sobczyk.chessboard.model.UnitType;
import com.sobczyk.chessboard.repository.UnitRepository;
import com.sobczyk.chessboard.validator.UnitActionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UnitsService {

    private final UnitRepository unitRepository;

    public ResponseEntity<List<UnitsResponse>> getUnitsData(Long gameId, Unit.Player player) {
        List<UnitsResponse> unitsResponses = UnitMapper.INSTANCE.toResponseList(unitRepository.findByGameIdAndPlayer(gameId, player));
        return ResponseEntity.ok(unitsResponses);
    }

    public synchronized ResponseEntity<ActionResponse> unitAction(ActionRequest request) {
        Unit unit = unitRepository.findByIdAndDestroyedFalse(request.getUnitId())
                .orElseThrow(() -> new RuntimeException("Unit not found"));
        UnitActionValidator.validateAction(request, unit);
        int[] newPositions = PositionsGenerator.calculatePosition(unit.getWidthPosition(), unit.getHeightPosition(),
                request.getFirstCoordinateDto(), request.getSecondCoordinateDto());
        Optional<Unit> unitInDanger = unitRepository.findByGameIdAndWidthPositionAndHeightPositionAndDestroyedFalse(
                request.getGameId(), newPositions[0], newPositions[1]);
        if (unitInDanger.isPresent() && ActionRequest.ActionType.SHOT.equals(request.getActionType())) {
            return destroyUnit(unitInDanger);
        }
        if (ActionRequest.ActionType.MOVE.equals(request.getActionType())) {
            return moveUnit(unit, unitInDanger, newPositions);
        }
        return ResponseEntity.ok(new ActionResponse("Shot missed!", null));
    }

    private ResponseEntity<ActionResponse> moveUnit(Unit unit, Optional<Unit> unitInDanger, int[] newPositions) {
        UnitActionValidator.validateNewPosition(newPositions, unit, unitInDanger, unit.getGame());
        unit.setWidthPosition(newPositions[0]);
        unit.setHeightPosition(newPositions[1]);
        unitRepository.save(unit);
        if (unitInDanger.isPresent() && UnitType.TRANSPORT.equals(unit.getUnitType())
                && !unit.getPlayer().equals(unitInDanger.get().getPlayer())) {
            return destroyUnit(unitInDanger);
        }
        return ResponseEntity.ok(new ActionResponse("Unit has been moved", UnitMapper.INSTANCE.toUnitResponse(unit)));
    }

    private ResponseEntity<ActionResponse> destroyUnit(Optional<Unit> unitInDanger) {
        unitInDanger.get().setDestroyed(true);
        unitRepository.save(unitInDanger.get());
        return ResponseEntity.ok(new ActionResponse("Unit destroyed!", UnitMapper.INSTANCE.toUnitResponse(unitInDanger.get())));
    }
}
