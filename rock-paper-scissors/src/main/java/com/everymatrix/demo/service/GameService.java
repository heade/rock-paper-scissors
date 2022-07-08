package com.everymatrix.demo.service;

import com.everymatrix.demo.config.AppProperties;
import com.everymatrix.demo.enums.Move;
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
        Flux<Move> firstPlayerMoves = provider.generateFairMoves();
        Flux<Move> secondPlayerMoves = provider.generate(properties.getGameMode());

        AtomicInteger firstPlayerScoreCount = new AtomicInteger();
        AtomicInteger secondPlayerScoreCount = new AtomicInteger();
        String firstPlayer = properties.getFirstPlayerName();
        String secondPlayer = properties.getSecondPlayerName();


        Flux.zip(firstPlayerMoves, secondPlayerMoves, this::getRoundResult)
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

    private RoundResult getRoundResult(Move firstPlayer, Move secondPlayer) {
        resultService.storeMoves(firstPlayer, secondPlayer);
        if (firstPlayer == secondPlayer) {
            return RoundResult.draw;
        }
        boolean player1Win = (firstPlayer.equals(Move.Paper) && secondPlayer.equals(Move.Rock)
                || (firstPlayer.equals(Move.Scissors) && secondPlayer.equals(Move.Paper))
                || firstPlayer.equals(Move.Rock) && secondPlayer.equals(Move.Scissors));
        return player1Win ? RoundResult.win : RoundResult.lose;
    }

}
