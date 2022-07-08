package com.everymatrix.demo.service;

import com.everymatrix.demo.config.AppProperties;
import com.everymatrix.demo.enums.Move;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

class FileResultServiceTest {

    private FileResultService resultService;
    private Path resultPath;
    @Mock
    public AppProperties properties = Mockito.mock(AppProperties.class);

    @BeforeEach
    public void setup() throws IOException {
        Mockito.when(properties.getFirstPlayerName()).thenReturn("Alex");
        Mockito.when(properties.getSecondPlayerName()).thenReturn("Bob");
        resultPath = Paths.get(String.format("%s/results-%d.txt", "results", System.currentTimeMillis()));
        BufferedWriter bufferedWriter = Files.newBufferedWriter(resultPath, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        resultService = new FileResultService(bufferedWriter, properties);
    }

    @Test
    void fileResultServiceTest() throws IOException {
        String firstPlayerName = properties.getFirstPlayerName();
        String secondPlayerName = properties.getSecondPlayerName();
        int firstPlayerScore = 0;
        int secondPlayerScore = 0;
        int drawScore = 10;

        resultService.storeMoves(Move.Rock, Move.Rock);
        resultService.storeRoundResult("Draw");
        resultService.storeFinalResult(firstPlayerScore, secondPlayerScore);

        BufferedReader bufferedReader = Files.newBufferedReader(resultPath);

        String movesRow = bufferedReader.readLine();
        Assertions.assertEquals(String.format("%s move: %s, %s move: %s",
                firstPlayerName, Move.Rock, secondPlayerName, Move.Rock), movesRow);

        String roundResultRow = bufferedReader.readLine();
        Assertions.assertEquals("Result: Draw", roundResultRow);

        String finalResultRow = bufferedReader.readLine();
        Assertions.assertEquals(String.format("Final score: %s %d - %d %s, draw: %d",
                firstPlayerName, firstPlayerScore, secondPlayerScore, secondPlayerName, drawScore), finalResultRow);

        bufferedReader.close();

        Files.delete(resultPath);
    }
}