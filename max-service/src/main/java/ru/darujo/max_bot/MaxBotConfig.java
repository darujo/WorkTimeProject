package ru.darujo.max_bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.max.botapi.core.PollingErrorHandler;

@Slf4j
@Component
public class MaxBotConfig {
//    @Bean
//    @ConditionalOnProperty(prefix = "max.bot.longpolling", name = "token")
//    public MaxBotAPI maxBotAPI(MaxLongPollingProperties properties) {
//        MaxClientConfig config = MaxClientConfig.builder()

    //                .baseUrl("https://platform-api.max.ru")
//                .baseUrl("https://platform-api2.max.ru")
//                .connectTimeout(Duration.ofSeconds(5))
//                .requestTimeout(Duration.ofSeconds(60))
//                .longPollTimeout(Duration.ofSeconds(20))
//                .maxRetries(5)
//                .enableRateLimiting(true)
//                .maxRequestsPerSecond(30)
//                .build();
//        return MaxBotAPI.create(properties.getToken(),config);
//    }
    @Bean
    PollingErrorHandler pollingErrorHandler() {
        return e -> log.error("Ошибка опроса", e);
    }
}
