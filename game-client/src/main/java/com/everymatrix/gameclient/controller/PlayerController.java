package com.everymatrix.gameclient.controller;

import com.everymatrix.gameclient.enums.Move;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class PlayerController {

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Move> getMoves() {
        return Flux.range(1, 10)
                .map(e -> Move.randomMove());
    }
}
