package com.sobczyk.chessboard.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = TooManyUnitsException.class)
    public ResponseEntity<ErrorResponse> tooManyUnitsException() {
        String errorMessage = "Too many units for configured chessboard dimension!";
        log.error(HttpStatus.BAD_REQUEST + ": " + errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage));
    }

    @ExceptionHandler(value = NewPositionOutOfChessboardException.class)
    public ResponseEntity<ErrorResponse> newPositionOutOfChessboardException() {
        String errorMessage = "New unit position is out of chessboard dimension!";
        log.error(HttpStatus.BAD_REQUEST + ": " + errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage));
    }

    @ExceptionHandler(value = PositionIsTakenException.class)
    public ResponseEntity<ErrorResponse> positionIsTakenException() {
        String errorMessage = "New position on chessboard is already taken!";
        log.error(HttpStatus.BAD_REQUEST + ": " + errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage));
    }

    @ExceptionHandler(value = ActionTypeException.class)
    public ResponseEntity<ErrorResponse> actionTypeException(ActionTypeException ex) {
        String errorMessage = "Action " + ex.getActionType() + " is not allowed for " + ex.getUnitType() + " unit!";
        log.error(HttpStatus.BAD_REQUEST + ": " + errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage));
    }

    @ExceptionHandler(value = MoveScopeExeception.class)
    public ResponseEntity<ErrorResponse> moveScopeExeception(MoveScopeExeception ex) {
        String errorMessage = "Too big move scope! For " + ex.getUnitType() + " allowed scope is " + ex.getAllowedScope();
        log.error(HttpStatus.BAD_REQUEST + ": " + errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage));
    }

    @ExceptionHandler(value = DiagonallyMoveException.class)
    public ResponseEntity<ErrorResponse> diagonallyMoveException(DiagonallyMoveException ex) {
        String errorMessage = "For " + ex.getUnitType() + " diagonally move is not allowed!";
        log.error(HttpStatus.BAD_REQUEST + ": " + errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage));
    }

    @ExceptionHandler(value = ActionTimeIntervalException.class)
    public ResponseEntity<ErrorResponse> actionTimeIntervalException(ActionTimeIntervalException ex) {
        String errorMessage = ex.getUnitType() +  " cannot " + ex.getActionType() + " yet!";
        log.error(HttpStatus.BAD_REQUEST + ": " + errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ExceptionHandler(value = UnitNotFoundException.class)
    public ResponseEntity<ErrorResponse> unitNotFoundException(UnitNotFoundException ex) {
        String errorMessage = "Unit with id " + ex.getUnitId() + " not found!";
        log.error(HttpStatus.BAD_REQUEST + ": " + errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage));
    }
}
