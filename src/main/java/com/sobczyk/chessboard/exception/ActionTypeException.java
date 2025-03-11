package com.sobczyk.chessboard.exception;

import com.sobczyk.chessboard.dto.request.ActionRequest;
import com.sobczyk.chessboard.model.UnitType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ActionTypeException extends RuntimeException{
    private ActionRequest.ActionType actionType;
    private UnitType unitType;
}
