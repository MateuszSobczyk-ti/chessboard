package com.sobczyk.chessboard.exception;

import com.sobczyk.chessboard.dto.request.ActionRequest;
import com.sobczyk.chessboard.model.UnitType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ActionTimeIntervalException extends RuntimeException{
    private UnitType unitType;
    private ActionRequest.ActionType actionType;
}
