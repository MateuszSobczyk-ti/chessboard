package com.sobczyk.chessboard.exception;

public record ErrorResponse(int statusCode, String message) {
}