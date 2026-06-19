package ru.darujo.max_bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.max.botapi.core.PollingErrorHandler;

@Slf4j
@Component
public class MaxBotConfig {

    @Bean
    PollingErrorHandler pollingErrorHandler() {
        return e -> log.error("Ошибка опроса", e);
    }
}
