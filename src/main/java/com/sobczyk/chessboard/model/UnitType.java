package com.sobczyk.chessboard.model;

import lombok.Getter;

@Getter
public enum UnitType {
    ARCHER(10, 5),
    TRANSPORT(null, 7),
    CANNON(13, null);

    private final Integer timeAfterShoot;
    private final Integer timeAfterMove;

    UnitType(Integer timeAfterShoot, Integer timeAfterMove) {
        this.timeAfterShoot = timeAfterShoot;
        this.timeAfterMove = timeAfterMove;
    }
}
