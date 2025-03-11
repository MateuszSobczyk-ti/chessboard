package com.sobczyk.chessboard.service;

import com.sobczyk.chessboard.dto.request.CoordinateDto;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PositionsGeneratorTest {

    private final static int WIDTH = 5;
    private final static int HEIGHT = 6;
    private int unitCounters;

    @Test
    void shouldGenerateGivenNumberOfPositions() {
        //when
        List<int[]> positions = generateRandomPositions();

        //then
        assertEquals(unitCounters, positions.size(), "Incorrect number of positions generated");
    }

    @Test
    void shouldGenerateUniquePositions() {
        //when
        List<int[]> positions = generateRandomPositions();

        //then
        Set<String> uniqueCheck = new HashSet<>();
        for (int[] pos : positions) {
            String key = pos[0] + "," + pos[1];
            assertTrue(uniqueCheck.add(key), "Duplicate position found: " + key);
        }
    }

    @Test
    void shouldGeneratePositionsWithinBounds() {
        //when
        List<int[]> positions = generateRandomPositions();

        //then
        for (int[] pos : positions) {
            assertTrue(pos[0] >= 0 && pos[0] < WIDTH, "Row position out of bounds: " + pos[0]);
            assertTrue(pos[1] >= 0 && pos[1] < HEIGHT, "Column position out of bounds: " + pos[1]);
        }
    }

    private List<int[]> generateRandomPositions() {
        unitCounters = 10;
        return PositionsGenerator.generateRandomPositions(unitCounters, WIDTH, HEIGHT);
    }

    @Test
    void shouldCalculatePositionWithFirstCoordinateOnly() {
        //given
        CoordinateDto first = new CoordinateDto(CoordinateDto.Direction.RIGHT, 2);

        //when
        int[] result = PositionsGenerator.calculatePosition(WIDTH, HEIGHT, first, null);

        //then
        assertArrayEquals(new int[]{7, 6}, result);
    }

    @Test
    void shouldCalculatePositionWithBothCoordinates() {
        //given
        CoordinateDto first = new CoordinateDto(CoordinateDto.Direction.LEFT, 1);
        CoordinateDto second = new CoordinateDto(CoordinateDto.Direction.DOWN, 4);

        //when
        int[] result = PositionsGenerator.calculatePosition(WIDTH, HEIGHT, first, second);

        //then
        assertArrayEquals(new int[]{4, 10}, result);
    }

}