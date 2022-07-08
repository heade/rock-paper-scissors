package com.everymatrix.demo.service;

import com.everymatrix.demo.enums.GameMode;
import com.everymatrix.demo.enums.Move;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class StrategyProviderTest {

    private static StrategyProvider provider;
    private static MockWebServer mockWebServer = new MockWebServer();

    @BeforeAll
    static void setup() throws IOException {
        mockWebServer.start();
        WebClient client = WebClient.builder().baseUrl("localhost:" + mockWebServer.getPort()).build();
        provider = new StrategyProvider(client);
    }

    @Test
    void givenFairMode_whenGenerateFairMove_thenGetRandomMoves() {
        Flux<Move> randomMovesFlux = provider.generateFairMoves();
        StepVerifier.create(randomMovesFlux)
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    void givenUnFairMode_whenGenerateUnFairMove_thenGetRockMoves() {
        Flux<Move> rockMovesFlux = provider.generateUnFairMoves();
        StepVerifier.create(rockMovesFlux)
                .thenConsumeWhile(Move.Rock::equals)
                .verifyComplete();
    }

    @Test
    void givenOnlineMode_whenGenerateOnlineMove_thenGetRandomMoves() throws IOException {
        //prepare response
        ObjectMapper mapper = new ObjectMapper();
        List<Move> randomMoveList = IntStream.range(0, 10).mapToObj(e -> Move.randomMove()).collect(Collectors.toList());
        String apiCallResponse = mapper.writeValueAsString(randomMoveList);
        //mock response using MockWebServer
        mockWebServer.enqueue(new MockResponse().setBody(apiCallResponse).addHeader("Content-Type", "application/json"));

        Flux<Move> onlineFlux = provider.generateOnlineMoves();
        StepVerifier.create(onlineFlux)
                .expectNextCount(10)
                .verifyComplete();

        mockWebServer.shutdown();
    }

    @Test
    void givenGameMode_whenGenerateMove_thenGetMoves() {
        StepVerifier.create(provider.generate(GameMode.unfair))
                .thenConsumeWhile(Move.Rock::equals)
                .verifyComplete();
    }
}