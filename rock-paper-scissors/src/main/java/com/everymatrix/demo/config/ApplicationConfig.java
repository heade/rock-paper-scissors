package com.everymatrix.demo.config;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Configuration
@EnableConfigurationProperties({AppProperties.class})
public class ApplicationConfig {

    @Bean
    public WebClient webClient(@Value("${app.game.url}") String url) {
        return WebClient.builder().baseUrl(url).build();
    }

    @Bean
    @SneakyThrows
    @ConditionalOnProperty(prefix = "app", name = "outputMode", havingValue = "file")
    public BufferedWriter writer(AppProperties properties) {
        Path resultPath = Paths.get(String.format("%s/results-%d.txt", properties.getResultFilePath(), System.currentTimeMillis()));
        return Files.newBufferedWriter(resultPath, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

}
