package com.everymatrix.demo.service;

import com.everymatrix.demo.config.AppProperties;
import com.everymatrix.demo.enums.Choose;
import com.everymatrix.demo.enums.GameMode;
import com.everymatrix.demo.enums.RoundResult;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class GameService {

    private final StrategyProvider provider;
    private final AppProperties properties;
    private final ResultService resultService;

    @EventListener(ApplicationStartedEvent.class)
    public void start() {
        Flux<Choose> firstPlayerMoves = provider.generateFairChoose();
        Flux<Choose> secondPlayerMoves = provideSecondPlayerStrategy();

        AtomicInteger firstPlayerScoreCount = new AtomicInteger();
        AtomicInteger secondPlayerScoreCount = new AtomicInteger();
        String firstPlayer = properties.getFirstPlayerName();
        String secondPlayer = properties.getSecondPlayerName();

        firstPlayerMoves.zipWith(secondPlayerMoves, this::getRoundResult)
                .doOnNext(roundResult -> {
                    String winner = "Draw";
                    if (roundResult.equals(RoundResult.win)) {
                        firstPlayerScoreCount.incrementAndGet();
                        winner = firstPlayer;
                    }
                    if (roundResult.equals(RoundResult.lose)) {
                        secondPlayerScoreCount.incrementAndGet();
                        winner = secondPlayer;
                    }
                    resultService.storeRoundResult(winner);
                })
                .doOnComplete(() -> resultService.storeFinalResult(firstPlayerScoreCount.get(), secondPlayerScoreCount.get()))
                .subscribe();
    }

    private RoundResult getRoundResult(Choose firstPlayer, Choose secondPlayer) {
        resultService.storeMoves(firstPlayer, secondPlayer);
        if (firstPlayer == secondPlayer) {
            return RoundResult.draw;
        }
        boolean player1Win = (firstPlayer.equals(Choose.Paper) && secondPlayer.equals(Choose.Rock)
                || (firstPlayer.equals(Choose.Scissors) && secondPlayer.equals(Choose.Paper))
                || firstPlayer.equals(Choose.Rock) && secondPlayer.equals(Choose.Scissors));
        return player1Win ? RoundResult.win : RoundResult.lose;
    }

    private Flux<Choose> provideSecondPlayerStrategy() {
        GameMode gameMode = properties.getGameMode();

        if (GameMode.fair.equals(gameMode)) {
            return provider.generateFairChoose();
        }
        if (GameMode.unfair.equals(gameMode)) {
            return provider.generateUnFairChoose();
        }
        return provider.generateOnlineChoose();
    }
}
