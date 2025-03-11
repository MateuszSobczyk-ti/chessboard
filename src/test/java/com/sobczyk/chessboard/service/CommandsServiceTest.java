package com.sobczyk.chessboard.service;

import com.sobczyk.chessboard.dto.request.ActionRequest;
import com.sobczyk.chessboard.dto.request.CoordinateDto;
import com.sobczyk.chessboard.dto.response.ActionResponse;
import com.sobczyk.chessboard.model.Command;
import com.sobczyk.chessboard.model.Game;
import com.sobczyk.chessboard.model.Unit;
import com.sobczyk.chessboard.model.UnitType;
import com.sobczyk.chessboard.repository.CommandRepository;
import com.sobczyk.chessboard.repository.UnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CommandsServiceTest {

    @Mock
    private CommandRepository commandRepository;

    @Mock
    private UnitRepository unitRepository;

    @Mock
    private UnitsService unitsService;

    @Mock
    private Random random;

    @InjectMocks
    private CommandsService commandsService;

    private ActionRequest actionRequest;
    private Unit unit;

    @BeforeEach
    void setUp() {
        when(random.nextInt(anyInt())).thenReturn(0);
        Game game = Game.builder().id(1L).width(5).height(5).build();
        unit = Unit.builder()
                .id(1L)
                .game(game)
                .player(Unit.Player.WHITE)
                .unitType(UnitType.ARCHER)
                .widthPosition(0)
                .heightPosition(0)
                .destroyed(false)
                .build();
        actionRequest = ActionRequest.builder()
                .unitId(1L)
                .gameId(1L)
                .player(Unit.Player.WHITE)
                .actionType(ActionRequest.ActionType.MOVE)
                .firstCoordinateDto(new CoordinateDto(CoordinateDto.Direction.UP, 1))
                .build();
    }

    @Test
    void shouldSaveCommand() {
        //given
        when(unitRepository.findByIdAndDestroyedFalse(1L)).thenReturn(Optional.of(unit));

        //when
        commandsService.saveCommand(actionRequest);

        //then
        verify(commandRepository, times(1)).save(any(Command.class));
    }

    @Test
    void shouldCreateRandomCommandArcherMove() {
        //given
        when(unitRepository.findByIdAndDestroyedFalse(1L)).thenReturn(Optional.of(unit));
        when(unitsService.unitAction(any(ActionRequest.class))).thenReturn(ResponseEntity.ok(new ActionResponse("Unit moved", null)));
        when(random.nextBoolean()).thenReturn(true);

        //when
        ResponseEntity<ActionResponse> response = commandsService.createRandomCommand(1L, 1L, Unit.Player.WHITE);

        //then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Unit moved", response.getBody().message());
        verify(commandRepository, times(1)).save(any(Command.class));
    }

    @Test
    void shouldCreateRandomCommandCannonShoot() {
        //given
        Unit cannonUnit = unit.toBuilder().unitType(UnitType.CANNON).build();
        when(unitRepository.findByIdAndDestroyedFalse(1L)).thenReturn(Optional.of(cannonUnit));
        when(unitsService.unitAction(any(ActionRequest.class))).thenReturn(ResponseEntity.ok(new ActionResponse("Unit shoot", null)));

        //when
        ResponseEntity<ActionResponse> response = commandsService.createRandomCommand(1L, 1L, Unit.Player.WHITE);

        //then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Unit shoot", response.getBody().message());
        verify(commandRepository, times(1)).save(any(Command.class));
    }

    @Test
    void shouldCreateRandomCommandTransportMove() {
        //given
        Unit transportUnit = unit.toBuilder().unitType(UnitType.TRANSPORT).build();
        when(unitRepository.findByIdAndDestroyedFalse(1L)).thenReturn(Optional.of(transportUnit));
        when(unitsService.unitAction(any(ActionRequest.class))).thenReturn(ResponseEntity.ok(new ActionResponse("Unit moved", null)));

        //when
        ResponseEntity<ActionResponse> response = commandsService.createRandomCommand(1L, 1L, Unit.Player.WHITE);

        //then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Unit moved", response.getBody().message());
        verify(commandRepository, times(1)).save(any(Command.class));
    }

    @Test
    void shouldThrowRuntimeExceptionUnitNotFound() {
        //given
        when(unitRepository.findByIdAndDestroyedFalse(1L)).thenReturn(Optional.empty());

        //when && then
        assertThrows(RuntimeException.class, () -> commandsService.createRandomCommand(1L, 1L, Unit.Player.WHITE));
    }

}