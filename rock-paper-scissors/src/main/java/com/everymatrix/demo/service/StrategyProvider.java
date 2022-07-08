package com.everymatrix.demo.service;

import com.everymatrix.demo.enums.Choose;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class StrategyProvider {

    private final WebClient client;

    public Flux<Choose> generateFairChoose() {
        return Flux.range(1, 10)
                .map(e -> Choose.randomChoose());
    }

    public Flux<Choose> generateUnFairChoose() {
        return Flux.range(1, 20)
                .map(e -> Choose.Rock);
    }

    public Flux<Choose> generateOnlineChoose() {
        return client
                .get()
                .retrieve()
                .bodyToFlux(Choose.class);
    }
}
