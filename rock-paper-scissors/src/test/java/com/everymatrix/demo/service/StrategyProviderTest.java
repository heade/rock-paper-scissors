package com.everymatrix.demo.service;

import com.everymatrix.demo.enums.Choose;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    void givenFairMode_whenGenerate_thenGetRandomMoves() {
        Flux<Choose> randomMovesFlux = provider.generateFairChoose();
        StepVerifier.create(randomMovesFlux)
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    void givenUnFairMode_whenGenerate_thenGetRockMoves() {
        Flux<Choose> rockMovesFlux = provider.generateUnFairChoose();
        StepVerifier.create(rockMovesFlux)
                .thenConsumeWhile(Choose.Rock::equals)
                .verifyComplete();
    }

    @Test
    void givenOnlineMode_whenGenerate_thenGetRandomMoves() throws IOException {
        //prepare response
        ObjectMapper mapper = new ObjectMapper();
        List<Choose> randomMoveList = IntStream.range(0, 10).mapToObj(e -> Choose.randomChoose()).collect(Collectors.toList());
        String apiCallResponse = mapper.writeValueAsString(randomMoveList);
        //mock response using MockWebServer
        mockWebServer.enqueue(new MockResponse().setBody(apiCallResponse).addHeader("Content-Type", "application/json"));

        Flux<Choose> onlineFlux = provider.generateOnlineChoose();
        StepVerifier.create(onlineFlux)
                .expectNextCount(10)
                .verifyComplete();

        mockWebServer.shutdown();
    }
}