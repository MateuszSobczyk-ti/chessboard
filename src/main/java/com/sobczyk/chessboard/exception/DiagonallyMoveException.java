package com.sobczyk.chessboard.exception;

import com.sobczyk.chessboard.model.UnitType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DiagonallyMoveException extends RuntimeException{
    private UnitType unitType;
}
