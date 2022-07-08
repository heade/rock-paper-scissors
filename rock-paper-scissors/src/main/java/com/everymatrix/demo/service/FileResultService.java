package com.everymatrix.demo.service;

import com.everymatrix.demo.config.AppProperties;
import com.everymatrix.demo.enums.Move;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;

@Service
@Slf4j
@ConditionalOnProperty(prefix = "app", name = "outputMode", havingValue = "file")
public class FileResultService implements ResultService {

    private String firstPlayer;
    private String secondPlayer;
    private BufferedWriter bufferedWriter;

    public FileResultService(BufferedWriter bufferedWriter, AppProperties properties) {
        this.bufferedWriter = bufferedWriter;
        this.firstPlayer = properties.getFirstPlayerName();
        this.secondPlayer = properties.getSecondPlayerName();
    }

    @Override
    public void storeRoundResult(String result) {
        String row = String.format("Result: %s", result);
        writeRow(row);
    }

    @Override
    public void storeMoves(Move firstPlayerMove, Move secondPlayerMove) {
        String row = String.format("%s move: %s, %s move: %s", firstPlayer, firstPlayerMove, secondPlayer, secondPlayerMove);
        writeRow(row);
    }

    @Override
    @SneakyThrows
    public void storeFinalResult(int firstPlayerScoreCount, int secondPlayerScoreCount) {
        int drawScore = 10 - firstPlayerScoreCount - secondPlayerScoreCount;
        String row = String.format("Final score: %s %d - %d %s, draw: %d",
                firstPlayer, firstPlayerScoreCount, secondPlayerScoreCount, secondPlayer, drawScore);
        writeRow(row);
        bufferedWriter.close();
    }

    private void writeRow(String row) {
        try {
            bufferedWriter.write(row);
            bufferedWriter.newLine();
        } catch (IOException e) {
            log.error("Error during writing the result", e);
            throw new UncheckedIOException(e);
        }
    }

}
