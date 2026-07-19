package ru.darujo.max_bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.max.botapi.client.MaxBotAPI;
import ru.max.botapi.client.MaxClientConfig;
import ru.max.botapi.core.PollingErrorHandler;
import ru.max.botapi.spring.longpolling.MaxLongPollingProperties;
import ru.max.botapi.spring.webhook.MaxWebhookProperties;

import java.time.Duration;

@Slf4j
@Component
public class MaxBotConfig {
    @Bean
    @ConditionalOnProperty(prefix = "max.bot.longpolling", name = "token")
    public MaxBotAPI maxBotAPI(MaxLongPollingProperties properties) {
        log.info("longpolling https://platform-api2.max.ru");
        return maxBotAPI(properties.getToken());
    }

    @Bean
    @ConditionalOnProperty(prefix = "max.bot.webhook", name = "token")
    public MaxBotAPI maxBotAPI(MaxWebhookProperties properties) {
        log.info("webhook https://platform-api2.max.ru");
        return maxBotAPI(properties.getToken());
    }

    public MaxBotAPI maxBotAPI(String token) {
        MaxClientConfig config = MaxClientConfig.builder()

//                    .baseUrl("https://platform-api.max.ru")
                .baseUrl("https://platform-api2.max.ru")
                .connectTimeout(Duration.ofSeconds(10))
                .requestTimeout(Duration.ofSeconds(60))
                .longPollTimeout(Duration.ofSeconds(30))
                .maxRetries(5)
                .enableRateLimiting(true)
                .maxRequestsPerSecond(30)
                .build();
        return MaxBotAPI.create(token, config);
    }

    @Bean
    PollingErrorHandler pollingErrorHandler() {
        return e -> log.error("Ошибка опроса", e);
    }
}
