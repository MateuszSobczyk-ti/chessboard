package com.sobczyk.chessboard.validator;

import com.sobczyk.chessboard.dto.request.ActionRequest;
import com.sobczyk.chessboard.exception.ActionTimeIntervalException;
import com.sobczyk.chessboard.model.Command;
import com.sobczyk.chessboard.model.UnitType;

import java.time.Duration;
import java.time.LocalDateTime;

public class ActionTimeIntervalValidator {

    public static void validateActionTimeInterval(Command lastCommand) {
        long timeInterval = Duration.between(lastCommand.getExecutionDate(), LocalDateTime.now()).getSeconds();
        validateArcherTimeInterval(lastCommand, timeInterval);
        validateTransportTimeInterval(lastCommand, timeInterval);
        validateCannonTimeInterval(lastCommand, timeInterval);
    }

    private static void validateArcherTimeInterval(Command lastCommand, long timeInterval) {
        if (ActionRequest.ActionType.SHOOT.equals(lastCommand.getActionType())
                && UnitType.ARCHER.equals(lastCommand.getUnit().getUnitType()) && UnitType.ARCHER.getTimeAfterShoot() > timeInterval) {
            throw new ActionTimeIntervalException(lastCommand.getUnit().getUnitType(), lastCommand.getActionType());
        }
        if (ActionRequest.ActionType.MOVE.equals(lastCommand.getActionType())
                && UnitType.ARCHER.equals(lastCommand.getUnit().getUnitType()) && UnitType.ARCHER.getTimeAfterMove() > timeInterval) {
            throw new ActionTimeIntervalException(lastCommand.getUnit().getUnitType(), lastCommand.getActionType());
        }
    }

    private static void validateTransportTimeInterval(Command lastCommand, long timeInterval) {
        if (ActionRequest.ActionType.MOVE.equals(lastCommand.getActionType())
                && UnitType.TRANSPORT.equals(lastCommand.getUnit().getUnitType()) && UnitType.TRANSPORT.getTimeAfterMove() > timeInterval) {
            throw new ActionTimeIntervalException(lastCommand.getUnit().getUnitType(), lastCommand.getActionType());
        }
    }

    private static void validateCannonTimeInterval(Command lastCommand, long timeInterval) {
        if (ActionRequest.ActionType.SHOOT.equals(lastCommand.getActionType())
                && UnitType.CANNON.equals(lastCommand.getUnit().getUnitType()) && UnitType.CANNON.getTimeAfterShoot() > timeInterval) {
            throw new ActionTimeIntervalException(lastCommand.getUnit().getUnitType(), lastCommand.getActionType());
        }
    }
}
