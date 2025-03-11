package com.sobczyk.chessboard.controller;

import com.sobczyk.chessboard.dto.request.ActionRequest;
import com.sobczyk.chessboard.dto.request.ConfigurationRequest;
import com.sobczyk.chessboard.dto.response.ActionResponse;
import com.sobczyk.chessboard.dto.response.GameResponse;
import com.sobczyk.chessboard.dto.response.UnitsResponse;
import com.sobczyk.chessboard.model.Unit;
import com.sobczyk.chessboard.service.CommandsService;
import com.sobczyk.chessboard.service.ConfigurationService;
import com.sobczyk.chessboard.service.UnitsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/chessboard")
@Tag(name = "Chessboard Play", description = "APIs for chessboard fight simulation on chessboard")
public class Controller {

    private final ConfigurationService configurationService;
    private final UnitsService unitsService;
    private final CommandsService commandsService;

    @Operation(
            summary = "Start new game",
            description = "Set game configuration: chessboard dimension and units for every player. If you set oldGameId - this game will be archived." +
                    " In response you get new game id, game start date and units positions on chessboard.")
    @PostMapping(value = "/newGame", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameResponse> startNewGame(@RequestParam(required = false) Long oldGameId,
                                                     @Valid @RequestBody ConfigurationRequest request) {
        return configurationService.saveConfiguration(request, oldGameId);
    }

    @Operation(
            summary = "Get units",
            description = "Get list of units for specified player." +
                    " Response contains unit details: id, type, position on chessboard, status(destroyed or not) and commands execution counter.")
    @GetMapping(value = "/units", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UnitsResponse>> getUnits(@NotNull @RequestParam Long gameId,
                                                        @NotNull @RequestParam Unit.Player player) {
        return unitsService.getUnitsData(gameId, player);
    }

    @Operation(
            summary = "Command for unit",
            description = "Make a command(move or shoot) for unit. Specify target position." +
                    " In response you get result message of action and new position for moved unit or destroyed unit details.")
    @PostMapping(value = "/unit/action", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ActionResponse> actionUnit(@Valid @RequestBody ActionRequest request) {
        commandsService.saveCommand(request);
        return unitsService.unitAction(request);
    }

    @Operation(
            summary = "Random command for unit",
            description = "Make a random command for specified unit. Unit can shoot or move." +
                    " In response you get result message of action and new position for moved unit or destroyed unit details.")
    @PostMapping(value = "/unit/{unitId}/random-command", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ActionResponse> randomAction(@PathVariable Long unitId, @NotNull @RequestParam Long gameId,
                                                       @NotNull @RequestParam Unit.Player player) {
        return commandsService.createRandomCommand(unitId, gameId, player);
    }
}
