package com.sobczyk.chessboard.service;

import com.sobczyk.chessboard.dto.request.ConfigurationRequest;
import com.sobczyk.chessboard.dto.request.UnitsConfigurationDto;
import com.sobczyk.chessboard.dto.response.GameResponse;
import com.sobczyk.chessboard.exception.TooManyUnitsException;
import com.sobczyk.chessboard.model.Game;
import com.sobczyk.chessboard.repository.GameRepository;
import com.sobczyk.chessboard.repository.UnitRepository;
import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfigurationServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private UnitRepository unitRepository;

    @InjectMocks
    private ConfigurationService configurationService;

    private ConfigurationRequest request;
    private Game game;
    private UnitsConfigurationDto whiteUnits;
    private UnitsConfigurationDto blackUnits;

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        whiteUnits = new UnitsConfigurationDto(1, 1, 1);
        blackUnits = new UnitsConfigurationDto(1, 1, 1);
        request = new ConfigurationRequest(5, 5, whiteUnits, blackUnits);
        game = Game.builder().id(1L).width(5).height(5).start(LocalDateTime.now()).build();
    }

    @Test
    void shouldFailValidationForInvalidDimensions() {
        //given
        ConfigurationRequest invalidRequest = new ConfigurationRequest(
                0, -1,
                new UnitsConfigurationDto(1, 1, 1),
                new UnitsConfigurationDto(1, 1, 1)
        );

        //when
        Set<ConstraintViolation<ConfigurationRequest>> violations = validator.validate(invalidRequest);

        //then
        assertEquals(2, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("dimension value must be greater than 1")));
    }

    @Test
    void shouldThrowTooManyUnitsException() {
        //given
        request = new ConfigurationRequest(2, 2, whiteUnits, blackUnits);

        //when && then
        assertThrows(TooManyUnitsException.class, () -> configurationService.saveConfiguration(request, null));
    }

    @Test
    void shouldSaveNewGameAndUnits() {
        //given
        when(gameRepository.save(any(Game.class))).thenReturn(game);
        when(unitRepository.saveAll(anyList())).thenReturn(List.of());
        try (MockedStatic<PositionsGenerator> mockedStatic = Mockito.mockStatic(PositionsGenerator.class)) {
            mockedStatic.when(() -> PositionsGenerator.generateRandomPositions(anyInt(), anyInt(), anyInt()))
                    .thenReturn(generatePositions(getTotalUnitCount(request)));

            //when
            ResponseEntity<GameResponse> response = configurationService.saveConfiguration(request, null);

            //then
            assertNotNull(response);
            assertEquals(200, response.getStatusCodeValue());
            assertNotNull(response.getBody());
            assertEquals(getTotalUnitCount(request), response.getBody().getUnits().size());
            verify(gameRepository, times(1)).save(any(Game.class));
            verify(unitRepository, times(1)).saveAll(anyList());
        }
    }

    @Test
    void shouldArchiveOldGame() {
        //given
        request = new ConfigurationRequest(5, 5, whiteUnits, blackUnits);
        Game oldGame = Game.builder().id(1L).width(5).height(5).start(LocalDateTime.now()).build();

        when(gameRepository.findById(1L)).thenReturn(Optional.of(oldGame));
        when(gameRepository.save(any(Game.class))).thenReturn(game);
        when(unitRepository.saveAll(anyList())).thenReturn(List.of());
        try (MockedStatic<PositionsGenerator> mockedStatic = Mockito.mockStatic(PositionsGenerator.class)) {
            mockedStatic.when(() -> PositionsGenerator.generateRandomPositions(anyInt(), anyInt(), anyInt()))
                    .thenReturn(generatePositions(getTotalUnitCount(request)));

            //when
            ResponseEntity<GameResponse> response = configurationService.saveConfiguration(request, 1L);

            //then
            assertNotNull(response);
            assertEquals(200, response.getStatusCodeValue());
            verify(gameRepository, times(2)).save(any(Game.class));
            assertTrue(oldGame.isArchived());
            assertNotNull(oldGame.getStop());
        }
    }

    private List<int[]> generatePositions(int totalUnitCounter) {
        List<int[]> positions = new ArrayList<>();
        for (int i = 0; i < totalUnitCounter; i++) {
            positions.add(new int[]{i, 0});
        }
        return positions;
    }

    private int getTotalUnitCount(ConfigurationRequest request) {
        return request.whiteUnits().archer() + request.whiteUnits().cannon() + request.whiteUnits().transport()
                + request.blackUnits().archer() + request.blackUnits().cannon() + request.blackUnits().transport();
    }

}