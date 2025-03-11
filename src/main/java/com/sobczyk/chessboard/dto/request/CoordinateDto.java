package com.sobczyk.chessboard.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CoordinateDto {
    private Direction direction;
    private Integer fields;

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
}
