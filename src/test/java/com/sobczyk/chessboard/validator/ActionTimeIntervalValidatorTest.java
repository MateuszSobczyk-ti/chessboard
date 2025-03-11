package com.sobczyk.chessboard.validator;

import com.sobczyk.chessboard.dto.request.ActionRequest;
import com.sobczyk.chessboard.exception.ActionTimeIntervalException;
import com.sobczyk.chessboard.model.Command;
import com.sobczyk.chessboard.model.Unit;
import com.sobczyk.chessboard.model.UnitType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ActionTimeIntervalValidatorTest {

    @Test
    void archerShotValidTimeShouldNotThrowException() {
        //given
        Command lastCommand = Command.builder()
                .unit(Unit.builder().unitType(UnitType.ARCHER).build())
                .actionType(ActionRequest.ActionType.SHOT)
                .executionDate(LocalDateTime.now().minusSeconds(UnitType.ARCHER.getTimeAfterShot()))
                .build();

        //when && then
        assertDoesNotThrow(() -> ActionTimeIntervalValidator.validateActionTimeInterval(lastCommand));
    }

    @Test
    void archerShotInvalidTimeShouldThrowException() {
        //given
        Command lastCommand = Command.builder()
                .unit(Unit.builder().unitType(UnitType.ARCHER).build())
                .actionType(ActionRequest.ActionType.SHOT)
                .executionDate(LocalDateTime.now().minusSeconds(UnitType.ARCHER.getTimeAfterShot() - 1))
                .build();

        //when && then
        assertThrows(ActionTimeIntervalException.class, () -> ActionTimeIntervalValidator.validateActionTimeInterval(lastCommand));
    }

    @Test
    void archerMoveValidTimeShouldNotThrowException() {
        //given
        Command lastCommand = Command.builder()
                .unit(Unit.builder().unitType(UnitType.ARCHER).build())
                .actionType(ActionRequest.ActionType.MOVE)
                .executionDate(LocalDateTime.now().minusSeconds(UnitType.ARCHER.getTimeAfterMove()))
                .build();

        //when && then
        assertDoesNotThrow(() -> ActionTimeIntervalValidator.validateActionTimeInterval(lastCommand));
    }

    @Test
    void archerMoveInvalidTimeShouldThrowException() {
        //given
        Command lastCommand = Command.builder()
                .unit(Unit.builder().unitType(UnitType.ARCHER).build())
                .actionType(ActionRequest.ActionType.MOVE)
                .executionDate(LocalDateTime.now().minusSeconds(UnitType.ARCHER.getTimeAfterMove() - 1))
                .build();

        //when && then
        assertThrows(ActionTimeIntervalException.class, () -> ActionTimeIntervalValidator.validateActionTimeInterval(lastCommand));
    }

    @Test
    void transportMoveValidTimeShouldNotThrowException() {
        //given
        Command lastCommand = Command.builder()
                .unit(Unit.builder().unitType(UnitType.TRANSPORT).build())
                .actionType(ActionRequest.ActionType.MOVE)
                .executionDate(LocalDateTime.now().minusSeconds(UnitType.TRANSPORT.getTimeAfterMove()))
                .build();

        //when && then
        assertDoesNotThrow(() -> ActionTimeIntervalValidator.validateActionTimeInterval(lastCommand));
    }

    @Test
    void transportMoveInvalidTimeShouldThrowException() {
        //given
        Command lastCommand = Command.builder()
                .unit(Unit.builder().unitType(UnitType.TRANSPORT).build())
                .actionType(ActionRequest.ActionType.MOVE)
                .executionDate(LocalDateTime.now().minusSeconds(UnitType.TRANSPORT.getTimeAfterMove() - 1))
                .build();

        //when && then
        assertThrows(ActionTimeIntervalException.class, () -> ActionTimeIntervalValidator.validateActionTimeInterval(lastCommand));
    }

    @Test
    void cannonShotValidTimeShouldNotThrowException() {
        //given
        Command lastCommand = Command.builder()
                .unit(Unit.builder().unitType(UnitType.CANNON).build())
                .actionType(ActionRequest.ActionType.SHOT)
                .executionDate(LocalDateTime.now().minusSeconds(UnitType.CANNON.getTimeAfterShot()))
                .build();

        //when && then
        assertDoesNotThrow(() -> ActionTimeIntervalValidator.validateActionTimeInterval(lastCommand));
    }

    @Test
    void cannonShotInvalidTimeShouldThrowException() {
        //given
        Command lastCommand = Command.builder()
                .unit(Unit.builder().unitType(UnitType.CANNON).build())
                .actionType(ActionRequest.ActionType.SHOT)
                .executionDate(LocalDateTime.now().minusSeconds(UnitType.CANNON.getTimeAfterShot() - 1))
                .build();

        //when && then
        assertThrows(ActionTimeIntervalException.class, () -> ActionTimeIntervalValidator.validateActionTimeInterval(lastCommand));
    }

    @Test
    void differentActionTypeShouldNotThrowException() {
        //given
        Command lastCommand = Command.builder()
                .unit(Unit.builder().unitType(UnitType.ARCHER).build())
                .actionType(ActionRequest.ActionType.MOVE)
                .executionDate(LocalDateTime.now().minusSeconds(UnitType.ARCHER.getTimeAfterShot() - 1))
                .build();

        //when && then
        assertDoesNotThrow(() -> ActionTimeIntervalValidator.validateActionTimeInterval(lastCommand));
    }

    @Test
    void differentUnitTypeShouldNotThrowException() {
        //given
        Command lastCommand = Command.builder()
                .unit(Unit.builder().unitType(UnitType.TRANSPORT).build())
                .actionType(ActionRequest.ActionType.SHOT)
                .executionDate(LocalDateTime.now().minusSeconds(UnitType.ARCHER.getTimeAfterShot() - 1))
                .build();

        //when && then
        assertDoesNotThrow(() -> ActionTimeIntervalValidator.validateActionTimeInterval(lastCommand));
    }

}