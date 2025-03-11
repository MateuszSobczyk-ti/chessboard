package com.sobczyk.chessboard.service;

import com.sobczyk.chessboard.dto.request.CoordinateDto;

import java.util.*;

public class PositionsGenerator {

    public static List<int[]> generateRandomPositions(int unitCounters, int width, int height) {
        Set<String> uniquePositions = new HashSet<>();
        List<int[]> positions = new ArrayList<>();
        Random random = new Random();

        while (positions.size() < unitCounters) {
            int row = random.nextInt(width);
            int col = random.nextInt(height);
            String key = row + "," + col;

            if (!uniquePositions.contains(key)) {
                uniquePositions.add(key);
                positions.add(new int[]{row, col});
            }
        }
        return positions;
    }

    public static int[] calculatePosition(int width, int height, CoordinateDto firstCoordinateDto, CoordinateDto secondCoordinateDto) {
        int[] position = {width, height};
        if (firstCoordinateDto != null) {
            calculateCoordinate(position, firstCoordinateDto);
        }
        if (secondCoordinateDto != null) {
            calculateCoordinate(position, secondCoordinateDto);
        }
        return position;
    }

    private static void calculateCoordinate(int[] position, CoordinateDto coordinateDto) {
        switch(coordinateDto.getDirection()) {
            case LEFT -> position[0] -= coordinateDto.getFields();
            case RIGHT -> position[0] += coordinateDto.getFields();
            case UP -> position[1] -= coordinateDto.getFields();
            case DOWN -> position[1] += coordinateDto.getFields();
        }
    }
}
