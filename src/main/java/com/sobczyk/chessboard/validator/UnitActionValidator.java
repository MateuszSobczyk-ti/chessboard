package com.sobczyk.chessboard.validator;

import com.sobczyk.chessboard.dto.request.ActionRequest;
import com.sobczyk.chessboard.exception.*;
import com.sobczyk.chessboard.model.Game;
import com.sobczyk.chessboard.model.Unit;
import com.sobczyk.chessboard.model.UnitType;

import java.util.Optional;

public class UnitActionValidator {

    private final static int ALLOWED_SCOPE_ARCHER = 1;
    private final static int ALLOWED_SCOPE_TRANSPORT = 3;

    public static void validateNewPosition(int[] newPositions, Unit unit, Optional<Unit> unitInDanger, Game game) {
        if (newPositions[0] < 0 || newPositions[0] > game.getWidth()
                || newPositions[1] < 0 || newPositions[1] > game.getHeight()) {
            throw new NewPositionOutOfChessboardException();
        }
        if (unitInDanger.isPresent() && ((!UnitType.TRANSPORT.equals(unit.getUnitType()))
                || unit.getPlayer().equals(unitInDanger.get().getPlayer()))) {
            throw new PositionIsTakenException();
        }
    }

    public static void validateAction(ActionRequest request, Unit unit) {
        switch(unit.getUnitType()) {
            case ARCHER -> validateArcherAction(request, unit);
            case TRANSPORT -> validateTransportAction(request, unit);
            case CANNON -> validateCannonAction(request, unit);
        }
    }

    private static void validateArcherAction(ActionRequest request, Unit unit) {
        validateDiagonallyAction(request, unit);
        validateMoveScope(request, ALLOWED_SCOPE_ARCHER, unit);
    }

    private static void validateTransportAction(ActionRequest request, Unit unit) {
        validateDiagonallyAction(request, unit);
        validateMoveScope(request, ALLOWED_SCOPE_TRANSPORT, unit);
        validateActionType(request, ActionRequest.ActionType.SHOOT, unit);
    }

    private static void validateCannonAction(ActionRequest request, Unit unit) {
        validateActionType(request, ActionRequest.ActionType.MOVE, unit);
    }

    private static void validateDiagonallyAction(ActionRequest request, Unit unit) {
        if (request.getFirstCoordinateDto() != null && request.getSecondCoordinateDto() != null) {
            throw new DiagonallyMoveException(unit.getUnitType());
        }
    }

    private static void validateMoveScope(ActionRequest request, int allowedScope, Unit unit) {
        if (ActionRequest.ActionType.MOVE.equals(request.getActionType()) && request.getFirstCoordinateDto() != null &&
                (request.getFirstCoordinateDto().getFields() > allowedScope || request.getFirstCoordinateDto().getFields() < -allowedScope)) {
            throw new MoveScopeExeception(allowedScope, unit.getUnitType());
        }
    }

    private static void validateActionType(ActionRequest request, ActionRequest.ActionType forbiddenAction, Unit unit) {
        if (forbiddenAction.equals(request.getActionType())) {
            throw new ActionTypeException(forbiddenAction, unit.getUnitType());
        }
    }
}
