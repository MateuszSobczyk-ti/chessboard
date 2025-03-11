package com.sobczyk.chessboard.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class GameResponse {
    private Long gameId;
    private LocalDateTime start;
    private List<UnitsResponse> units;
}
