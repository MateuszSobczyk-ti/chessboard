package com.sobczyk.chessboard.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UnitNotFoundException extends RuntimeException{
    private Long unitId;
}
