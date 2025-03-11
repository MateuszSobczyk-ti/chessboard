package com.sobczyk.chessboard.validator;

import com.sobczyk.chessboard.dto.request.ActionRequest;
import com.sobczyk.chessboard.dto.request.CoordinateDto;
import com.sobczyk.chessboard.exception.*;
import com.sobczyk.chessboard.model.Game;
import com.sobczyk.chessboard.model.Unit;
import com.sobczyk.chessboard.model.UnitType;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UnitActionValidatorTest {
    private final Game game = Game.builder().width(5).height(5).build();
    private final Unit archer = Unit.builder().unitType(UnitType.ARCHER).player(Unit.Player.WHITE).build();
    private final Unit transport = Unit.builder().unitType(UnitType.TRANSPORT).player(Unit.Player.WHITE).build();
    private final Unit cannon = Unit.builder().unitType(UnitType.CANNON).player(Unit.Player.WHITE).build();
    private final Unit enemyUnit = Unit.builder().unitType(UnitType.ARCHER).player(Unit.Player.BLACK).build();

    @Test
    void shouldThrowNewPositionOutOfChessboardException() {
        //given
        int[] newPositions = {6, 6};

        //when && then
        assertThrows(NewPositionOutOfChessboardException.class,
                () -> UnitActionValidator.validateNewPosition(newPositions, archer, Optional.empty(), game));
    }

    @Test
    void shouldThrowPositionIsTakenException() {
        //given
        int[] newPositions = {2, 2};

        //when && then
        assertThrows(PositionIsTakenException.class,
                () -> UnitActionValidator.validateNewPosition(newPositions, archer, Optional.of(archer), game));
    }

    @Test
    void transportShouldNotThrowPositionIsTakenException() {
        //given
        int[] newPositions = {2, 2};

        //when && then
        assertDoesNotThrow(() -> UnitActionValidator.validateNewPosition(newPositions, transport, Optional.of(enemyUnit), game));
    }

    @Test
    void archerShouldThrowMoveScopeExeception() {
        //given
        ActionRequest request = ActionRequest.builder()
                .actionType(ActionRequest.ActionType.MOVE)
                .firstCoordinateDto(new CoordinateDto(CoordinateDto.Direction.UP, 2))
                .build();

        //when && then
        assertThrows(MoveScopeExeception.class, () -> UnitActionValidator.validateAction(request, archer));
    }

    @Test
    void transportShouldNotThrowMoveScopeException() {
        //given
        ActionRequest request = ActionRequest.builder()
                .actionType(ActionRequest.ActionType.MOVE)
                .firstCoordinateDto(new CoordinateDto(CoordinateDto.Direction.UP, 3))
                .build();

        //when && then
        assertDoesNotThrow(() -> UnitActionValidator.validateAction(request, transport));
    }

    @Test
    void transportShouldThrowActionTypeException() {
        //given
        ActionRequest request = ActionRequest.builder()
                .actionType(ActionRequest.ActionType.SHOT)
                .build();

        //when && then
        assertThrows(ActionTypeException.class, () -> UnitActionValidator.validateAction(request, transport));
    }

    @Test
    void cannonShouldThrowActionTypeException() {
        //given
        ActionRequest request = ActionRequest.builder()
                .actionType(ActionRequest.ActionType.MOVE)
                .build();

        //when && then
        assertThrows(ActionTypeException.class, () -> UnitActionValidator.validateAction(request, cannon));
    }


    @Test
    void archerShouldThrowDiagonallyMoveException() {
        //given
        ActionRequest request = ActionRequest.builder()
                .firstCoordinateDto(new CoordinateDto(CoordinateDto.Direction.UP, 1))
                .secondCoordinateDto(new CoordinateDto(CoordinateDto.Direction.UP, 1))
                .build();

        //when && then
        assertThrows(DiagonallyMoveException.class, () -> UnitActionValidator.validateAction(request, archer));
    }
}