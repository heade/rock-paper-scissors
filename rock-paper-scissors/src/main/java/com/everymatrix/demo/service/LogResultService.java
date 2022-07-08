package com.everymatrix.demo.service;

import com.everymatrix.demo.config.AppProperties;
import com.everymatrix.demo.enums.Move;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConditionalOnProperty(prefix = "app", name = "outputMode", havingValue = "log")
public class LogResultService implements ResultService {

    private String firstPlayer;
    private String secondPlayer;

    public LogResultService(AppProperties properties) {
        this.firstPlayer = properties.getFirstPlayerName();
        this.secondPlayer = properties.getSecondPlayerName();
    }

    @Override
    public void storeRoundResult(String result) {
        log.info("Result: {}", result);
    }

    @Override
    public void storeMoves(Move firstPlayerMove, Move secondPlayerMove) {
        log.info("{} move: {}, {} move: {}", firstPlayer, firstPlayerMove, secondPlayer, secondPlayerMove);
    }

    @Override
    public void storeFinalResult(int firstPlayerScoreCount, int secondPlayerScoreCount) {
        int drawScore = 10 - firstPlayerScoreCount - secondPlayerScoreCount;
        log.info("Final score: {} {} - {} {}, draw: {}",
                firstPlayer, firstPlayerScoreCount, secondPlayerScoreCount, secondPlayer, drawScore);
    }

}
