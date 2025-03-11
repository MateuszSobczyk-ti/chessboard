package com.sobczyk.chessboard.dto.request;

import com.sobczyk.chessboard.model.Unit;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ActionRequest {
    @NotNull
    private Long gameId;
    @NotNull
    private Long unitId;
    @NotNull
    private Unit.Player player;
    @NotNull
    private ActionType actionType;
    @NotNull
    private CoordinateDto firstCoordinateDto;
    private CoordinateDto secondCoordinateDto;

    public enum ActionType {
        MOVE, SHOT
    }
}
