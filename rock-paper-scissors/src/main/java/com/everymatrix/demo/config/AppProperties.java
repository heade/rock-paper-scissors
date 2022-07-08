package com.everymatrix.demo.config;

import com.everymatrix.demo.enums.GameMode;
import com.everymatrix.demo.enums.OutputMode;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private GameMode gameMode;
    private OutputMode outputMode;
    private String resultFilePath;
    private String firstPlayerName;
    private String secondPlayerName;
}
