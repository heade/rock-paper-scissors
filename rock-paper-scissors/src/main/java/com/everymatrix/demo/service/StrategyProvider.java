package com.everymatrix.demo.service;

import com.everymatrix.demo.enums.Move;
import com.everymatrix.demo.enums.GameMode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class StrategyProvider {

    private final WebClient client;

    public Flux<Move> generateFairMoves() {
        return Flux.range(1, 10)
                .map(e -> Move.randomMove());
    }

    public Flux<Move> generateUnFairMoves() {
        return Flux.range(1, 20)
                .map(e -> Move.Rock);
    }

    public Flux<Move> generateOnlineMoves() {
        return client
                .get()
                .retrieve()
                .bodyToFlux(Move.class);
    }

    public Flux<Move> generate(GameMode gameMode) {
        if (GameMode.fair.equals(gameMode)) {
            return generateFairMoves();
        }
        if (GameMode.unfair.equals(gameMode)) {
            return generateUnFairMoves();
        }
        return generateOnlineMoves();
    }
}
