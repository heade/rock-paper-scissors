package com.everymatrix.gameclient.controller;

import com.everymatrix.gameclient.enums.Move;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(PlayerController.class)
class PlayerControllerTest {

    @Autowired
    public WebTestClient client;

    @Test
    public void getPlayerMovesTest() {
        client.get()
                .uri("")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM_VALUE)
                .expectBodyList(Move.class)
                .hasSize(10);
    }

}