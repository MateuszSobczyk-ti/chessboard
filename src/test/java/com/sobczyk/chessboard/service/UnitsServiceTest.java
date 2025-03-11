package com.sobczyk.chessboard.service;

import com.sobczyk.chessboard.dto.request.ActionRequest;
import com.sobczyk.chessboard.dto.request.CoordinateDto;
import com.sobczyk.chessboard.dto.response.ActionResponse;
import com.sobczyk.chessboard.dto.response.UnitsResponse;
import com.sobczyk.chessboard.model.Game;
import com.sobczyk.chessboard.model.Unit;
import com.sobczyk.chessboard.model.UnitType;
import com.sobczyk.chessboard.repository.UnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UnitsServiceTest {

    @Mock
    private UnitRepository unitRepository;

    @InjectMocks
    private UnitsService unitsService;

    private Unit unit;
    private ActionRequest actionRequest;
    private Game game;

    @BeforeEach
    void setUp() {
        game = Game.builder().id(1L).width(5).height(5).build();
        unit = Unit.builder()
                .id(1L)
                .game(game)
                .player(Unit.Player.WHITE)
                .unitType(UnitType.ARCHER)
                .widthPosition(0)
                .heightPosition(0)
                .destroyed(false)
                .build();
        actionRequest = new ActionRequest(1L, 1L, Unit.Player.BLACK, ActionRequest.ActionType.MOVE,
                new CoordinateDto(CoordinateDto.Direction.DOWN, 1), null);
    }

    @Test
    void shouldReturnUnitsResponseList() {
        when(unitRepository.findByGameIdAndPlayer(1L, Unit.Player.WHITE)).thenReturn(List.of(unit));

        ResponseEntity<List<UnitsResponse>> response = unitsService.getUnitsData(1L, Unit.Player.WHITE);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(1L, response.getBody().getFirst().getId());
    }

    @Test
    void shouldMoveUnit() {
        try (MockedStatic<PositionsGenerator> mockedStatic = Mockito.mockStatic(PositionsGenerator.class)) {
            when(unitRepository.findByIdAndDestroyedFalse(1L)).thenReturn(Optional.of(unit));
            mockedStatic.when(() -> PositionsGenerator.calculatePosition(anyInt(), anyInt(), any(CoordinateDto.class), nullable(CoordinateDto.class)))
                    .thenReturn(new int[]{0, 1});
            when(unitRepository.findByGameIdAndWidthPositionAndHeightPositionAndDestroyedFalse(1L, 0, 1))
                    .thenReturn(Optional.empty());

            ResponseEntity<ActionResponse> response = unitsService.unitAction(actionRequest);

            assertNotNull(response);
            assertEquals(200, response.getStatusCodeValue());
            assertNotNull(response.getBody());
            assertEquals("Unit has been moved", response.getBody().message());
            assertEquals(0, unit.getWidthPosition());
            assertEquals(1, unit.getHeightPosition());
        }
    }

    @Test
    void shouldDestroyUnit() {
        actionRequest = new ActionRequest(1L, 1L, Unit.Player.BLACK, ActionRequest.ActionType.SHOOT,
                new CoordinateDto(CoordinateDto.Direction.DOWN, 1), null);
        Unit targetUnit = Unit.builder()
                .id(2L)
                .game(game)
                .player(Unit.Player.BLACK)
                .unitType(UnitType.ARCHER)
                .widthPosition(0)
                .heightPosition(1)
                .destroyed(false)
                .build();

        try (MockedStatic<PositionsGenerator> mockedStatic = Mockito.mockStatic(PositionsGenerator.class)) {
            when(unitRepository.findByIdAndDestroyedFalse(1L)).thenReturn(Optional.of(unit));
            mockedStatic.when(() -> PositionsGenerator.calculatePosition(anyInt(), anyInt(), any(CoordinateDto.class), nullable(CoordinateDto.class)))
                    .thenReturn(new int[]{0, 1});
            when(unitRepository.findByGameIdAndWidthPositionAndHeightPositionAndDestroyedFalse(1L, 0, 1))
                    .thenReturn(Optional.of(targetUnit));

            ResponseEntity<ActionResponse> response = unitsService.unitAction(actionRequest);

            assertNotNull(response);
            assertEquals(200, response.getStatusCodeValue());
            assertNotNull(response.getBody());
            assertEquals("Unit destroyed!", response.getBody().message());
            assertTrue(targetUnit.isDestroyed());
        }
    }

    @Test
    void shouldMiss() {
        actionRequest = new ActionRequest(1L, 1L, Unit.Player.BLACK, ActionRequest.ActionType.SHOOT,
                new CoordinateDto(CoordinateDto.Direction.DOWN, 1), null);
        try (MockedStatic<PositionsGenerator> mockedStatic = Mockito.mockStatic(PositionsGenerator.class)) {
            mockedStatic.when(() -> PositionsGenerator.calculatePosition(anyInt(), anyInt(), any(CoordinateDto.class), nullable(CoordinateDto.class)))
                    .thenReturn(new int[]{0, 1});
            when(unitRepository.findByIdAndDestroyedFalse(1L)).thenReturn(Optional.of(unit));
            when(unitRepository.findByGameIdAndWidthPositionAndHeightPositionAndDestroyedFalse(1L, 0, 1))
                    .thenReturn(Optional.empty());

            ResponseEntity<ActionResponse> response = unitsService.unitAction(actionRequest);

            assertNotNull(response);
            assertEquals(200, response.getStatusCodeValue());
            assertNotNull(response.getBody());
            assertEquals("Shoot missed!", response.getBody().message());
        }
    }

    @Test
    void unitAction_unitNotFound_shouldThrowRuntimeException() {
        when(unitRepository.findByIdAndDestroyedFalse(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> unitsService.unitAction(actionRequest));
    }

    @Test
    void shouldDestroyUnitByTransportMove() {
        Unit transportUnit = Unit.builder()
                .id(1L)
                .game(game)
                .player(Unit.Player.WHITE)
                .unitType(UnitType.TRANSPORT)
                .widthPosition(0)
                .heightPosition(0)
                .destroyed(false)
                .build();
        Unit targetUnit = Unit.builder()
                .id(2L)
                .game(game)
                .player(Unit.Player.BLACK)
                .unitType(UnitType.ARCHER)
                .widthPosition(0)
                .heightPosition(1)
                .destroyed(false)
                .build();

        try (MockedStatic<PositionsGenerator> mockedStatic = Mockito.mockStatic(PositionsGenerator.class)) {
            mockedStatic.when(() -> PositionsGenerator.calculatePosition(anyInt(), anyInt(), any(CoordinateDto.class), nullable(CoordinateDto.class)))
                    .thenReturn(new int[]{0, 1});
            when(unitRepository.findByIdAndDestroyedFalse(1L)).thenReturn(Optional.of(transportUnit));
            when(unitRepository.findByGameIdAndWidthPositionAndHeightPositionAndDestroyedFalse(1L, 0, 1)).thenReturn(Optional.of(targetUnit));

            ResponseEntity<ActionResponse> response = unitsService.unitAction(actionRequest);

            assertNotNull(response);
            assertEquals(200, response.getStatusCodeValue());
            assertNotNull(response.getBody());
            assertEquals("Unit destroyed!", response.getBody().message());
            assertTrue(targetUnit.isDestroyed());
        }
    }
}