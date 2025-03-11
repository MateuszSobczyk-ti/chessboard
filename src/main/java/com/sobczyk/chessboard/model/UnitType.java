package com.sobczyk.chessboard.model;

import lombok.Getter;

@Getter
public enum UnitType {
    ARCHER(10, 5),
    TRANSPORT(null, 7),
    CANNON(13, null);

    private final Integer timeAfterShot;
    private final Integer timeAfterMove;

    UnitType(Integer timeAfterShot, Integer timeAfterMove) {
        this.timeAfterShot = timeAfterShot;
        this.timeAfterMove = timeAfterMove;
    }
}
