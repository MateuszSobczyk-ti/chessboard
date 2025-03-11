package com.sobczyk.chessboard.dto.response;

import com.sobczyk.chessboard.model.Unit;
import com.sobczyk.chessboard.model.UnitType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitsResponse {
    private Long id;
    private Unit.Player player;
    private UnitType unitType;
    private Integer widthPosition;
    private Integer heightPosition;
    private boolean destroyed;
    private int commandCounter;
}
