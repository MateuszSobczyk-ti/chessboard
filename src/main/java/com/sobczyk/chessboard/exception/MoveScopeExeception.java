package com.sobczyk.chessboard.exception;

import com.sobczyk.chessboard.model.UnitType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MoveScopeExeception extends RuntimeException{
    private int allowedScope;
    private UnitType unitType;
}
